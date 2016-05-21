package internalFrame;

import java.awt.*;
import javax.swing.*;

/**
 * Program demonstruj¹cy u¿ycie ramek wewnêtrznych.
 * @version 1.11 2007-08-01
 * @author Cay Horstmann
 */
public class InternalFrameTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new DesktopFrame();
               frame.setTitle("InternalFrameTest");               
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}