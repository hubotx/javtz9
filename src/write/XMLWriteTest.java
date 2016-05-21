package write;

import java.awt.*;
import javax.swing.*;

/**
 * Program demonstrujący sposób tworzenia dokumentu XML.
 * Zapisuje grafikę w pliku formatu SVG
 * @version 1.11 2012-01-26
 * @author Cay Horstmann
 */
public class XMLWriteTest
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFrame frame = new XMLWriteFrame();
                frame.setTitle("XMLWriteTest");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}
