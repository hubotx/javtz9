package tableCellRender;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Edytor otwieraj¹cy okno dialogowe wyboru koloru.
 */
public class ColorTableCellEditor extends AbstractCellEditor implements TableCellEditor
{
   private JColorChooser colorChooser;
   private JDialog colorDialog;
   private JPanel panel;

   public ColorTableCellEditor()
   {
      panel = new JPanel();
      // przygotowuje okno dialogowe

      colorChooser = new JColorChooser();
      colorDialog = JColorChooser.createDialog(null, "Planet Color", false, colorChooser,
         EventHandler.create(ActionListener.class, this, "stopCellEditing"),
         EventHandler.create(ActionListener.class, this, "cancelCellEditing"));
   }

   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
         int row, int column)
   {
      //Tutaj uzyskujemy bie¿¹c¹ wartoœæ Color.
      // Przechowujemy j¹ w obiekcie okna dialogowego.
      colorChooser.setColor((Color) value);
      return panel;
   }

   public boolean shouldSelectCell(EventObject anEvent)
   {
      // rozpoczêcie edycji
      colorDialog.setVisible(true);

      // informuje metodê wywo³uj¹c¹ o rozpoczêciu edycji
      return true;
   }

   public void cancelCellEditing()
   {
      // edycja przerwana - zamyka okno dialogowe
      colorDialog.setVisible(false);
      super.cancelCellEditing();
   }

   public boolean stopCellEditing()
   {
      // edycja zakoñczona - zamyka okno dialogowe
      colorDialog.setVisible(false);
      super.stopCellEditing();

      // informuje metodê wywo³uj¹c¹, ¿e wartoœæ koloru jest dozwolona
      return true;
   }

   public Object getCellEditorValue()
   {
      return colorChooser.getColor();
   }
}
