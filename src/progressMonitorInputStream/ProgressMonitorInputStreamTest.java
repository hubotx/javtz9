package progressMonitorInputStream;

import java.awt.*;
import javax.swing.*;

/**
 * Program testuj¹cy dzia³anie monitora postêpu strumienia wejœciowego.
 * @version 1.05 2012-01-26
 * @author Cay Horstmann
 */
public class ProgressMonitorInputStreamTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new TextFrame();
               frame.setTitle("ProgressMonitorInputStreamTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}