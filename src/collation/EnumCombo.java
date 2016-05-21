package collation;

import java.util.*;
import javax.swing.*;

/**
   Lista rozwijalna pozwalaj¹ca u¿ytkownikowi wybraæ
   jedn¹ spoœród wartoœci pól statycznych,
   których nazwy zosta³y przekazane konstruktorowi.
   @version 1.14 2012-01-26
   @author Cay Horstmann
*/
public class EnumCombo extends JComboBox<String>
{ 
   private Map<String, Integer> table = new TreeMap<>();

   /**
      Tworzy EnumCombo.
      @param cl klasa
      @param labels tablica nazw pól statycznych klasy cl
   */
   public EnumCombo(Class<?> cl, String... labels)
   {  
      for (String label : labels)
      {  
         String name = label.toUpperCase().replace(' ', '_');
         int value = 0;
         try
         {  
            java.lang.reflect.Field f = cl.getField(name);
            value = f.getInt(cl);
         }
         catch (Exception e)
         {  
            label = "(" + label + ")";
         }
         table.put(label, value);
         addItem(label);
      }
      setSelectedItem(labels[0]);
   }

   /**
      Zwraca wartoœæ pola wybranego przez u¿ytkownika.
      @return wartoœæ pola statycznego
   */
   public int getValue()
   {  
      return table.get(getSelectedItem());
   }
}
