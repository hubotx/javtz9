package read;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/**
 * Program prezentujący możliwość wykorzystywania dokumentów XML
 * do opisu układu komponentów interfejsu użytkownika.
 * @version 1.11 2012-06-03
 * @author Cay Horstmann
 */
public class GridBagTest
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFileChooser chooser = new JFileChooser(".");
                chooser.showOpenDialog(null);
                File file = chooser.getSelectedFile();
                JFrame frame = new FontFrame(file);
                frame.setTitle("GridBagTest");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}

/**
 * Ramka zawierająca okno dialogowe wyboru czcionki
 * opisane przez dokument XML.
 * @param filename nazwa pliku zawierającego opis komponentów interfejsu użytkownika
 */
class FontFrame extends JFrame
{
    private GridBagPane gridbag;
    private JComboBox<String> face;
    private JComboBox<String> size;
    private JCheckBox bold;
    private JCheckBox italic;

    @SuppressWarnings("unchecked")
    public FontFrame(File file)
    {
        gridbag = new GridBagPane(file);
        add(gridbag);

        face = (JComboBox<String>) gridbag.get("face");
        size = (JComboBox<String>) gridbag.get("size");
        bold = (JCheckBox) gridbag.get("bold");
        italic = (JCheckBox) gridbag.get("italic");

        face.setModel(new DefaultComboBoxModel<String>(new String[] { "Serif", "SansSerif", "Monospaced", "Dialog", "DialogInput" }));

        size.setModel(new DefaultComboBoxModel<String>(new String[] { "8", "10", "12", "15", "18", "24", "36", "48" }));

        ActionListener listener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setSample();
            }
        };

        face.addActionListener(listener);
        size.addActionListener(listener);
        bold.addActionListener(listener);
        italic.addActionListener(listener);

        setSample();
        pack();
    }

    /**
     * Metoda konfigurująca tekst prezentowany
     * za pomocą wybranej czcionki.
     */
    public void setSample()
    {
        String fontFace = face.getItemAt(face.getSelectedIndex());
        int fontSize = Integer.parseInt(size.getItemAt(size.getSelectedIndex()));
        JTextArea sample = (JTextArea) gridbag.get("sample");
        int fontStyle = (bold.isSelected() ? Font.BOLD : 0)
                + (italic.isSelected() ? Font.ITALIC : 0);

        sample.setFont(new Font(fontFace, fontStyle, fontSize));
        sample.repaint();
    }
}