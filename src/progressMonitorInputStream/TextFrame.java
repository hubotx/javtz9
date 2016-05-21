package progressMonitorInputStream;

import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.*;

/**
 * Ramka wyposażona w menu umożliwiające załadowanie
 * pliku tekstowego i obszar tekstowy pokazujący jego zawartość.
 * Obszar tekstowy jest tworzony i inicjowany po zakończeniu
 * wczytywania pliku, aby uniknąć migania.
 */
public class TextFrame extends JFrame
{
    public static final int TEXT_ROWS = 10;
    public static final int TEXT_COLUMNS = 40;

    private JMenuItem openItem;
    private JMenuItem exitItem;
    private JTextArea textArea;
    private JFileChooser chooser;

    public TextFrame()
    {
        textArea = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
        add(new JScrollPane(textArea));

        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                try
                {
                    openFile();
                }
                catch (IOException exception)
                {
                    exception.printStackTrace();
                }
            }
        });

        fileMenu.add(openItem);
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                System.exit(0);
            }
        });
        fileMenu.add(exitItem);
        pack();
    }

    /**
     * Umożliwia użytkownikowi wybranie pliku, którego zawartość
     * zostanie pokazana w obszarze tekstowym.
     */
    public void openFile() throws IOException
    {
        int r = chooser.showOpenDialog(this);
        if (r != JFileChooser.APPROVE_OPTION) return;
        final File f = chooser.getSelectedFile();

        // tworzy strumień i sekwencję filtrów odczytu

        InputStream fileIn = Files.newInputStream(f.toPath());
        final ProgressMonitorInputStream progressIn = new ProgressMonitorInputStream(this,
                                                                                     "Reading " + f.getName(),
                                                                                     fileIn);
        textArea.setText("");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception
            {
                try (Scanner in = new Scanner(progressIn))
                {
                    while (in.hasNextLine())
                    {
                        String line = in.nextLine();
                        textArea.append(line);
                        textArea.append("\n");
                    }
                }
                return null;
            }
        };
        worker.execute();
    }
}
