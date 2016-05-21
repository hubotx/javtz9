package editorPane;

import java.awt.*;
import javax.swing.*;

/**
 * Program demonstruj¹cy wyœwietlanie dokumentów HTML w panelu edytora.
 * @version 1.04 2012-01-26
 * @author Cay Horstmann
 */
public class EditorPaneTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new EditorPaneFrame();
               frame.setTitle("EditorPaneTest");               
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}
