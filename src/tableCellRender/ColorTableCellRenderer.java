package tableCellRender;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Klasa obiektu rysuj¹cego kolorowy panel wewn¹trz komórki.
 */
public class ColorTableCellRenderer extends JPanel implements TableCellRenderer
{
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
         boolean hasFocus, int row, int column)
   {
      setBackground((Color) value);
      if (hasFocus) setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
      else setBorder(null);
      return this;
   }
}