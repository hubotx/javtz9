package write;

import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.nio.file.*;
import javax.swing.*;
import javax.xml.stream.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

/**
 * Ramka zawierająca panel prezentacji grafiki.
 */
public class XMLWriteFrame extends JFrame
{
    private RectangleComponent comp;
    private JFileChooser chooser;

    public XMLWriteFrame()
    {
        chooser = new JFileChooser();

        // dodaje komponent do ramki
        comp = new RectangleComponent();
        add(comp);

        // tworzy menu
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        JMenuItem newItem = new JMenuItem("New");
        menu.add(newItem);
        newItem.addActionListener(EventHandler.create(ActionListener.class, comp, "newDrawing"));

        JMenuItem saveItem = new JMenuItem("Save with DOM/XSLT");
        menu.add(saveItem);
        newItem.addActionListener(EventHandler.create(ActionListener.class, this, "saveDocument"));

        JMenuItem saveStAXItem = new JMenuItem("Save with StAX");
        menu.add(saveStAXItem);
        newItem.addActionListener(EventHandler.create(ActionListener.class, this, "saveStAX"));

        JMenuItem exitItem = new JMenuItem("Exit");
        menu.add(exitItem);
        exitItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
        pack();
    }

    /**
     * Zapisuje grafikę w formacie SVG, używając DOM/XSLT.
     */
    public void saveDocument() throws TransformerException, IOException
    {
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        Document doc = comp.buildDocument();
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.w3.org/TR/2000/CR-SVG-20000802/DTD/svg-20000802.dtd");
        t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//W3C//DTD SVG 20000802//EN");
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty(OutputKeys.METHOD, "xml");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        t.transform(new DOMSource(doc), new StreamResult(Files.newOutputStream(file.toPath())));
    }

    /**
     * Zapisuje grafikę w formacie SVG, używając StAX.
     */
    public void saveStAX() throws IOException, XMLStreamException
    {
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = factory.createXMLStreamWriter(Files.newOutputStream(file.toPath()));
        try
        {
            comp.writeDocument(writer);
        }
        finally
        {
            writer.close(); // nie implementuje AutoCloseable
        }
    }
}
