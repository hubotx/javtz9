package treeModel;

import java.lang.reflect.*;
import java.util.*;

/**
 * Klasa reprezentuj�ca zmienn� posiadaj�c� typ, nazw� i warto��.
 */
public class Variable
{
   private Class<?> type;
   private String name;
   private Object value;
   private ArrayList<Field> fields;

   /**
    * Tworzy obiekt reprezentuj�cy zmienn�.
    * @param aType typ zmiennej
    * @param aName nazwa zmiennej
    * @param aValue warto�� zmiennej
    */
   public Variable(Class<?> aType, String aName, Object aValue)
   {
      type = aType;
      name = aName;
      value = aValue;
      fields = new ArrayList<>();

      // znajduje wszystkie pola, je�li zmienna jest typu klasy,
      // nie rozwija jedynie �a�cuch�w znak�w i warto�ci null

      if (!type.isPrimitive() && !type.isArray() && !type.equals(String.class) && value != null)
      {
         // pobiera pola klasy i pola wszystkich jej klas bazowych
         for (Class<?> c = value.getClass(); c != null; c = c.getSuperclass())
         {
            Field[] fs = c.getDeclaredFields();
            AccessibleObject.setAccessible(fs, true);

            // pobiera wszystkie pola, kt�re nie s� statyczne
            for (Field f : fs)
               if ((f.getModifiers() & Modifier.STATIC) == 0) fields.add(f);
         }
      }
   }

   /**
    * Zwraca warto�� zmiennej.
    * @return warto��
    */
   public Object getValue()
   {
      return value;
   }

   /**
    * Zwraca wszystkie pola zmiennej, kt�re nie s� statyczne.
    * @return tablica zmiennych opisuj�cych pola
    */
   public ArrayList<Field> getFields()
   {
      return fields;
   }

   public String toString()
   {
      String r = type + " " + name;
      if (type.isPrimitive()) r += "=" + value;
      else if (type.equals(String.class)) r += "=" + value;
      else if (value == null) r += "=null";
      return r;
   }
}
