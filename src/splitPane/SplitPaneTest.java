package splitPane;

import java.awt.*;
import javax.swing.*;

/**
 * Program demonstruj¹cy wykorzystanie komponentu panelu dzielonego.
 * @version 1.03 2007-08-01
 * @author Cay Horstmann
 */
public class SplitPaneTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new SplitPaneFrame();
               frame.setTitle("SplitPaneTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}