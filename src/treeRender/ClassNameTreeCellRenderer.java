package treeRender;

import java.awt.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.swing.tree.*;

/**
 * Klasa opisuj¹ca wêz³y drzewa czcionk¹ zwyk³¹ lub pochylon¹
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
      // pobiera obiekt u¿ytkownika
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
      Class<?> c = (Class<?>) node.getUserObject();

      // przy pierwszym u¿yciu tworzy czcionkê
      // pochy³¹ odpowiadaj¹c¹ danej czcionce prostej
      if (plainFont == null)
      {
         plainFont = getFont();
         // obiekt rysuj¹cy komórkê drzewa wywo³ywany jest czasami
         // dla etykiety, która nie posiada okreœlonej czcionki (null).
         if (plainFont != null) italicFont = plainFont.deriveFont(Font.ITALIC);
      }

      // wybiera czcionkê pochy³¹, jeœli klasa jest abstrakcyjna
      if ((c.getModifiers() & Modifier.ABSTRACT) == 0) setFont(plainFont);
      else setFont(italicFont);
      return this;
   }
}
