package treeModel;

import java.awt.*;
import javax.swing.*;

/**
 * Program demonstruj¹cy zastosowanie w³asnego modelu drzewa do wyœwietlania pól obiektu.
 * @version 1.04 2012-01-26
 * @author Cay Horstmann
 */
public class ObjectInspectorTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new ObjectInspectorFrame();
               frame.setTitle("ObjectInspectorTest");               
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}



