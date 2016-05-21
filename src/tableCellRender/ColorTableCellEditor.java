package tableCellRender;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Edytor otwieraj�cy okno dialogowe wyboru koloru.
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
      //Tutaj uzyskujemy bie��c� warto�� Color.
      // Przechowujemy j� w obiekcie okna dialogowego.
      colorChooser.setColor((Color) value);
      return panel;
   }

   public boolean shouldSelectCell(EventObject anEvent)
   {
      // rozpocz�cie edycji
      colorDialog.setVisible(true);

      // informuje metod� wywo�uj�c� o rozpocz�ciu edycji
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
      // edycja zako�czona - zamyka okno dialogowe
      colorDialog.setVisible(false);
      super.stopCellEditing();

      // informuje metod� wywo�uj�c�, �e warto�� koloru jest dozwolona
      return true;
   }

   public Object getCellEditorValue()
   {
      return colorChooser.getColor();
   }
}
