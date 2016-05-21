package tableCellRender;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Ramka zawieraj¹ca tabelê danych o planetach.
 */
public class TableCellRenderFrame extends JFrame
{
   private static final int DEFAULT_WIDTH = 600;
   private static final int DEFAULT_HEIGHT = 400;

   public TableCellRenderFrame()
   {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      TableModel model = new PlanetTableModel();
      JTable table = new JTable(model);
      table.setRowSelectionAllowed(false);

      // instaluje obiekty rysuj¹ce i edytory

      table.setDefaultRenderer(Color.class, new ColorTableCellRenderer());
      table.setDefaultEditor(Color.class, new ColorTableCellEditor());

      JComboBox<Integer> moonCombo = new JComboBox<>();
      for (int i = 0; i <= 20; i++)
         moonCombo.addItem(i);

      TableColumnModel columnModel = table.getColumnModel();
      TableColumn moonColumn = columnModel.getColumn(PlanetTableModel.MOONS_COLUMN);
      moonColumn.setCellEditor(new DefaultCellEditor(moonCombo));
      moonColumn.setHeaderRenderer(table.getDefaultRenderer(ImageIcon.class));
      moonColumn.setHeaderValue(new ImageIcon(getClass().getResource("Moons.gif")));

      // pokazuje tabelê

      table.setRowHeight(100);
      add(new JScrollPane(table), BorderLayout.CENTER);
   }
}

