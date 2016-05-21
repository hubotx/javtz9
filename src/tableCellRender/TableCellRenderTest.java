package tableCellRender;

import java.awt.*;
import javax.swing.*;

/**
 * Program demonstruj¹cy wykorzystanie obiektów rysuj¹cych komórki
 * i edytorów komórek tabeli.
 * @version 1.03 2012-06-08
 * @author Cay Horstmann
 */
public class TableCellRenderTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {

               JFrame frame = new TableCellRenderFrame();
               frame.setTitle("TableCellRenderTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}