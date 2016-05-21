package treeRender;

import java.awt.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.swing.tree.*;

/**
 * Klasa opisuj�ca w�z�y drzewa czcionk� zwyk�� lub pochylon�
 * (w przypadku klas abstrakcyjnych).
 */
public class ClassNameTreeCellRenderer extends DefaultTreeCellRenderer
{
   private Font plainFont = null;
   private Font italicFont = null;

   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
         boolean expanded, boolean leaf, int row, boolean hasFocus)
   {
      super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
      // pobiera obiekt u�ytkownika
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
      Class<?> c = (Class<?>) node.getUserObject();

      // przy pierwszym u�yciu tworzy czcionk�
      // pochy�� odpowiadaj�c� danej czcionce prostej
      if (plainFont == null)
      {
         plainFont = getFont();
         // obiekt rysuj�cy kom�rk� drzewa wywo�ywany jest czasami
         // dla etykiety, kt�ra nie posiada okre�lonej czcionki (null).
         if (plainFont != null) italicFont = plainFont.deriveFont(Font.ITALIC);
      }

      // wybiera czcionk� pochy��, je�li klasa jest abstrakcyjna
      if ((c.getModifiers() & Modifier.ABSTRACT) == 0) setFont(plainFont);
      else setFont(italicFont);
      return this;
   }
}
