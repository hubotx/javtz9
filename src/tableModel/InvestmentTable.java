package tableModel;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

/**
 * Program tworz¹cy tabelê w oparciu o w³asny model.
 * @version 1.02 2007-08-01
 * @author Cay Horstmann
 */
public class InvestmentTable
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new InvestmentTableFrame();
               frame.setTitle("InvestmentTable");               
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * Ramka zawieraj¹ca tabelê inwestycji.
 */
class InvestmentTableFrame extends JFrame
{
   public InvestmentTableFrame()
   {
      TableModel model = new InvestmentTableModel(30, 5, 10);
      JTable table = new JTable(model);
      add(table);
      pack();
   }

}

/**
 * Model tabeli wyliczaj¹cy wartoœci komórek na ¿¹danie.
 * Tabela pokazuje przyrost inwestycji w kolejnych latach
 * w ró¿nych scenariuszach.
 */
class InvestmentTableModel extends AbstractTableModel
{
   private static double INITIAL_BALANCE = 100000.0;
   
   private int years;
   private int minRate;
   private int maxRate;

   /**
    * Tworzy model tabeli inwestycji.
    * @param y liczba lat
    * @param r1 najni¿sza stopa oprocentowania
    * @param r2 najwy¿sza stopa oprocentowania
    */
   public InvestmentTableModel(int y, int r1, int r2)
   {
      years = y;
      minRate = r1;
      maxRate = r2;
   }

   public int getRowCount()
   {
      return years;
   }

   public int getColumnCount()
   {
      return maxRate - minRate + 1;
   }

   public Object getValueAt(int r, int c)
   {
      double rate = (c + minRate) / 100.0;
      int nperiods = r;
      double futureBalance = INITIAL_BALANCE * Math.pow(1 + rate, nperiods);
      return String.format("%.2f", futureBalance);
   }

   public String getColumnName(int c)
   {
      return (c + minRate) + "%";
   }
}
