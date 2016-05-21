package treeModel;

import java.lang.reflect.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * Model drzewa opisuj¹cego strukturê powi¹zañ obiektów w jêzyku Java.
 * Wêz³y podrzêdne reprezentuj¹ sk³adowe obiektu.
 */
public class ObjectTreeModel implements TreeModel
{
   private Variable root;
   private EventListenerList listenerList = new EventListenerList();

   /**
    * Tworzy puste drzewo.
    */
   public ObjectTreeModel()
   {
      root = null;
   }

   /**
    * Umieszcza zmienn¹ w korzeniu drzewa.
    * @param v zmienna opisywana przez drzewo
    */
   public void setRoot(Variable v)
   {
      Variable oldRoot = v;
      root = v;
      fireTreeStructureChanged(oldRoot);
   }

   public Object getRoot()
   {
      return root;
   }

   public int getChildCount(Object parent)
   {
      return ((Variable) parent).getFields().size();
   }

   public Object getChild(Object parent, int index)
   {
      ArrayList<Field> fields = ((Variable) parent).getFields();
      Field f = (Field) fields.get(index);
      Object parentValue = ((Variable) parent).getValue();
      try
      {
         return new Variable(f.getType(), f.getName(), f.get(parentValue));
      }
      catch (IllegalAccessException e)
      {
         return null;
      }
   }

   public int getIndexOfChild(Object parent, Object child)
   {
      int n = getChildCount(parent);
      for (int i = 0; i < n; i++)
         if (getChild(parent, i).equals(child)) return i;
      return -1;
   }

   public boolean isLeaf(Object node)
   {
      return getChildCount(node) == 0;
   }

   public void valueForPathChanged(TreePath path, Object newValue)
   {
   }

   public void addTreeModelListener(TreeModelListener l)
   {
      listenerList.add(TreeModelListener.class, l);
   }

   public void removeTreeModelListener(TreeModelListener l)
   {
      listenerList.remove(TreeModelListener.class, l);
   }

   protected void fireTreeStructureChanged(Object oldRoot)
   {
      TreeModelEvent event = new TreeModelEvent(this, new Object[] { oldRoot });
      EventListener[] listeners = listenerList.getListeners(TreeModelListener.class);
      for (int i = 0; i < listeners.length; i++)
         ((TreeModelListener) listeners[i]).treeStructureChanged(event);
   }
}
