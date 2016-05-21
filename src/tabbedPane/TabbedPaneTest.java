package tabbedPane;

import java.awt.*;
import javax.swing.*;

/**
 * Program demonstruj¹cy zastosowanie panelu z zak³adkami.
 * @version 1.03 2007-08-01
 * @author Cay Horstmann
 */
public class TabbedPaneTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {

               JFrame frame = new TabbedPaneFrame();
               frame.setTitle("TabbedPaneTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

