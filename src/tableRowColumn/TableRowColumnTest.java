package tableRowColumn;

import java.awt.*;
import javax.swing.*;

/**
 * Program demonstruj¹cy dzia³anie wierszy i kolumn tabeli.
 * @version 1.21 2012-01-26
 * @author Cay Horstmann
 */
public class TableRowColumnTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new PlanetTableFrame();
               frame.setTitle("TableRowColumnTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}