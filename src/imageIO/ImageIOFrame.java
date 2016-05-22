package imageIO;

import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.imageio.stream.*;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * Ramka wyświetlająca zawartość pliku graficznego.
 * Jej menu umożliwia odczyt i zapis plików.
 */
public class ImageIOFrame extends JFrame
{
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;

    private static Set<String> writerFormats = getWriterFormats();

    private BufferedImage[] images;

    public ImageIOFrame()
    {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                openFile();
            }
        });
        fileMenu.add(openItem);

        JMenu saveMenu = new JMenu("Save");
        fileMenu.add(saveMenu);
        Iterator<String> iter = writerFormats.iterator();
        while (iter.hasNext())
        {
            final String formatName = iter.next();
            JMenuItem formatItem = new JMenuItem(formatName);
            saveMenu.add(formatItem);
            formatItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event)
                {
                    saveFile(formatName);
                }
            });
        }

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                System.exit(0);
            }
        });
        fileMenu.add(exitItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Otwiera plik i ładuje obrazek.
     */
    public void openFile()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        String[] extensions = ImageIO.getReaderFileSuffixes();
        chooser.setFileFilter(new FileNameExtensionFilter("Image files", extensions));
        int r = chooser.showOpenDialog(this);
        if (r != JFileChooser.APPROVE_OPTION) return;
        File f = chooser.getSelectedFile();
        Box box = Box.createVerticalBox();
        try
        {
            String name = f.getName();
            String suffix = name.substring(name.lastIndexOf('.') + 1);
            Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
            ImageReader reader = iter.next();
            ImageInputStream imageIn = ImageIO.createImageInputStream(f);
            reader.setInput(imageIn);
            int count = reader.getNumImages(true);
            images = new BufferedImage[count];
            for (int i = 0; i < count; i++)
            {
                images[i] = reader.read(i);
                box.add(new JLabel(new ImageIcon(images[i])));
            }
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(this, e);
        }
        setContentPane(new JScrollPane(box));
        validate();
    }

    /**
     * Zapisuje bieżący obrazek w pliku.
     * @param formatName format pliku
     */
    public void saveFile(final String formatName)
    {
        if (images == null) return;
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(formatName);
        ImageWriter writer = iter.next();
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        String[] extensions = writer.getOriginatingProvider().getFileSuffixes();
        chooser.setFileFilter(new FileNameExtensionFilter("Image files", extensions));

        int r = chooser.showSaveDialog(this);
        if (r != JFileChooser.APPROVE_OPTION) return;
        File f = chooser.getSelectedFile();
        try
        {
            ImageOutputStream imageOut = ImageIO.createImageOutputStream(f);
            writer.setOutput(imageOut);

            writer.write(new IIOImage(images[0], null, null));
            for (int i = 1; i < images.length; i++)
            {
                IIOImage iioImage = new IIOImage(images[i], null, null);
                if (writer.canInsertImage(i)) writer.writeInsert(i, iioImage, null);
            }
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    /**
     * Tworzy zbiór "preferowanych" nazw formatów plików graficznych,
     * dla których istnieją obiekty odczytu. Preferowaną nazwą formatu
     * zostaje pierwsza nazwa dla danego obiektu odczytu.
     * @return zbiór nazw formatów
     */
    public static Set<String> getWriterFormats()
    {
        Set<String> writerFormats = new TreeSet<>();
        Set<String> formatNames = new TreeSet<>(Arrays.asList(ImageIO.getWriterFormatNames()));
        while (formatNames.size() > 0)
        {
            String name = formatNames.iterator().next();
            Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(name);
            ImageWriter writer = iter.next();
            String[] names = writer.getOriginatingProvider().getFormatNames();
            String format = names[0];
            if (format.equals(format.toLowerCase())) format = format.toUpperCase();
            writerFormats.add(format);
            formatNames.removeAll(Arrays.asList(names));
        }
        return writerFormats;
    }
}
