package retire;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.text.*;
import javax.swing.*;

/**
 * Kalkulator emerytalny posiadaj�cy
 * interfejs u�ytkownika w j�zyku angielskim, 
 * niemieckim i chi�skim.
 * @version 1.23 2012-06-07
 * @author Cay Horstmann
 */
public class Retire
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new RetireFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

class RetireFrame extends JFrame
{
   private JTextField savingsField = new JTextField(10);
   private JTextField contribField = new JTextField(10);
   private JTextField incomeField = new JTextField(10);
   private JTextField currentAgeField = new JTextField(4);
   private JTextField retireAgeField = new JTextField(4);
   private JTextField deathAgeField = new JTextField(4);
   private JTextField inflationPercentField = new JTextField(6);
   private JTextField investPercentField = new JTextField(6);
   private JTextArea retireText = new JTextArea(10, 25);
   private RetireComponent retireCanvas = new RetireComponent();
   private JButton computeButton = new JButton();
   private JLabel languageLabel = new JLabel();
   private JLabel savingsLabel = new JLabel();
   private JLabel contribLabel = new JLabel();
   private JLabel incomeLabel = new JLabel();
   private JLabel currentAgeLabel = new JLabel();
   private JLabel retireAgeLabel = new JLabel();
   private JLabel deathAgeLabel = new JLabel();
   private JLabel inflationPercentLabel = new JLabel();
   private JLabel investPercentLabel = new JLabel();
   private RetireInfo info = new RetireInfo();
   private Locale[] locales = { Locale.US, Locale.CHINA, Locale.GERMANY };
   private Locale currentLocale;
   private JComboBox<Locale> localeCombo = new LocaleCombo(locales);
   private ResourceBundle res;
   private ResourceBundle resStrings;
   private NumberFormat currencyFmt;
   private NumberFormat numberFmt;
   private NumberFormat percentFmt;

   public RetireFrame()
   {
      setLayout(new GridBagLayout());
      add(languageLabel, new GBC(0, 0).setAnchor(GBC.EAST));
      add(savingsLabel, new GBC(0, 1).setAnchor(GBC.EAST));
      add(contribLabel, new GBC(2, 1).setAnchor(GBC.EAST));
      add(incomeLabel, new GBC(4, 1).setAnchor(GBC.EAST));
      add(currentAgeLabel, new GBC(0, 2).setAnchor(GBC.EAST));
      add(retireAgeLabel, new GBC(2, 2).setAnchor(GBC.EAST));
      add(deathAgeLabel, new GBC(4, 2).setAnchor(GBC.EAST));
      add(inflationPercentLabel, new GBC(0, 3).setAnchor(GBC.EAST));
      add(investPercentLabel, new GBC(2, 3).setAnchor(GBC.EAST));
      add(localeCombo, new GBC(1, 0, 3, 1));
      add(savingsField, new GBC(1, 1).setWeight(100, 0).setFill(GBC.HORIZONTAL));
      add(contribField, new GBC(3, 1).setWeight(100, 0).setFill(GBC.HORIZONTAL));
      add(incomeField, new GBC(5, 1).setWeight(100, 0).setFill(GBC.HORIZONTAL));
      add(currentAgeField, new GBC(1, 2).setWeight(100, 0).setFill(GBC.HORIZONTAL));
      add(retireAgeField, new GBC(3, 2).setWeight(100, 0).setFill(GBC.HORIZONTAL));
      add(deathAgeField, new GBC(5, 2).setWeight(100, 0).setFill(GBC.HORIZONTAL));
      add(inflationPercentField, new GBC(1, 3).setWeight(100, 0).setFill(GBC.HORIZONTAL));
      add(investPercentField, new GBC(3, 3).setWeight(100, 0).setFill(GBC.HORIZONTAL));
      add(retireCanvas, new GBC(0, 4, 4, 1).setWeight(100, 100).setFill(GBC.BOTH));
      add(new JScrollPane(retireText), new GBC(4, 4, 2, 1).setWeight(0, 100).setFill(GBC.BOTH));

      computeButton.setName("computeButton");
      computeButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               getInfo();
               updateData();
               updateGraph();
            }
         });
      add(computeButton, new GBC(5, 3));

      retireText.setEditable(false);
      retireText.setFont(new Font("Monospaced", Font.PLAIN, 10));

      info.setSavings(0);
      info.setContrib(9000);
      info.setIncome(60000);
      info.setCurrentAge(35);
      info.setRetireAge(65);
      info.setDeathAge(85);
      info.setInvestPercent(0.1);
      info.setInflationPercent(0.05);

      int localeIndex = 0; // lokalizator USA jest domy�lny
      for (int i = 0; i < locales.length; i++)
         // je�li bie��cy lokalizator jest jednym z mo�liwych do wyboru, to zostaje 
         // wybrany
         if (getLocale().equals(locales[i])) localeIndex = i;
      setCurrentLocale(locales[localeIndex]);

      localeCombo.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               setCurrentLocale((Locale) localeCombo.getSelectedItem());
               validate();
            }
         });
      pack();
   }

   /**
    * Wybiera bie��cy lokalizator.
    * @param locale lokalizator
    */
   public void setCurrentLocale(Locale locale)
   {
      currentLocale = locale;
      localeCombo.setSelectedItem(currentLocale);
      localeCombo.setLocale(currentLocale);

      res = ResourceBundle.getBundle("retire.RetireResources", currentLocale);
      resStrings = ResourceBundle.getBundle("retire.RetireStrings", currentLocale);
      currencyFmt = NumberFormat.getCurrencyInstance(currentLocale);
      numberFmt = NumberFormat.getNumberInstance(currentLocale);
      percentFmt = NumberFormat.getPercentInstance(currentLocale);

      updateDisplay();
      updateInfo();
      updateData();
      updateGraph();
   }

   /**
    * Aktualizuje wszystkie wy�wietlane etykiety.
    */
   public void updateDisplay()
   {
      languageLabel.setText(resStrings.getString("language"));
      savingsLabel.setText(resStrings.getString("savings"));
      contribLabel.setText(resStrings.getString("contrib"));
      incomeLabel.setText(resStrings.getString("income"));
      currentAgeLabel.setText(resStrings.getString("currentAge"));
      retireAgeLabel.setText(resStrings.getString("retireAge"));
      deathAgeLabel.setText(resStrings.getString("deathAge"));
      inflationPercentLabel.setText(resStrings.getString("inflationPercent"));
      investPercentLabel.setText(resStrings.getString("investPercent"));
      computeButton.setText(resStrings.getString("computeButton"));
   }

   /**
    * Aktualizuje zawarto�� p�l tekstowych.
    */
   public void updateInfo()
   {
      savingsField.setText(currencyFmt.format(info.getSavings()));
      contribField.setText(currencyFmt.format(info.getContrib()));
      incomeField.setText(currencyFmt.format(info.getIncome()));
      currentAgeField.setText(numberFmt.format(info.getCurrentAge()));
      retireAgeField.setText(numberFmt.format(info.getRetireAge()));
      deathAgeField.setText(numberFmt.format(info.getDeathAge()));
      investPercentField.setText(percentFmt.format(info.getInvestPercent()));
      inflationPercentField.setText(percentFmt.format(info.getInflationPercent()));
   }

   /**
    * Aktualizuje zawarto�� obszaru tekstowego.
    */
   public void updateData()
   {
      retireText.setText("");
      MessageFormat retireMsg = new MessageFormat("");
      retireMsg.setLocale(currentLocale);
      retireMsg.applyPattern(resStrings.getString("retire"));

      for (int i = info.getCurrentAge(); i <= info.getDeathAge(); i++)
      {
         Object[] args = { i, info.getBalance(i) };
         retireText.append(retireMsg.format(args) + "\n");
      }
   }

   /**
    * Aktualizuje wykres.
    */
   public void updateGraph()
   {
      retireCanvas.setColorPre((Color) res.getObject("colorPre"));
      retireCanvas.setColorGain((Color) res.getObject("colorGain"));
      retireCanvas.setColorLoss((Color) res.getObject("colorLoss"));
      retireCanvas.setInfo(info);
      repaint();
   }

   /**
    * Odczytuje dane wprowadzone przez u�ytkownika.
    */
   public void getInfo()
   {
      try
      {
         info.setSavings(currencyFmt.parse(savingsField.getText()).doubleValue());
         info.setContrib(currencyFmt.parse(contribField.getText()).doubleValue());
         info.setIncome(currencyFmt.parse(incomeField.getText()).doubleValue());
         info.setCurrentAge(numberFmt.parse(currentAgeField.getText()).intValue());
         info.setRetireAge(numberFmt.parse(retireAgeField.getText()).intValue());
         info.setDeathAge(numberFmt.parse(deathAgeField.getText()).intValue());
         info.setInvestPercent(percentFmt.parse(investPercentField.getText()).doubleValue());
         info.setInflationPercent(percentFmt.parse(inflationPercentField.getText()).doubleValue());
      }
      catch (ParseException ex)
      {
         ex.printStackTrace();
      }
   }
}

/**
 * Klasa zawieraj�ca informacje potrzebne do obliczenia emerytury.
 */
class RetireInfo
{
   private double savings;
   private double contrib;
   private double income;
   private int currentAge;
   private int retireAge;
   private int deathAge;
   private double inflationPercent;
   private double investPercent;
   private int age;
   private double balance;

   /**
    * Zwraca stan konta dla podanego roku.
    * @param year rok, dla kt�rego obliczany jest stan konta
    * @return dost�pna lub wymagana kwota w danym roku
    */
   public double getBalance(int year)
   {
      if (year < currentAge) return 0;
      else if (year == currentAge)
      {
         age = year;
         balance = savings;
         return balance;
      }
      else if (year == age) return balance;
      if (year != age + 1) getBalance(year - 1);
      age = year;
      if (age < retireAge) balance += contrib;
      else balance -= income;
      balance = balance * (1 + (investPercent - inflationPercent));
      return balance;
   }

   /**
    * Zwraca wysoko�� oszcz�dno�ci.
    * @return kwota oszcz�dno�ci
    */
   public double getSavings()
   {
      return savings;
   }

   /**
    * Okre�la wysoko�� oszcz�dno�ci.
    * @param newValue kwota oszcz�dno�ci
    */
   public void setSavings(double newValue)
   {
      savings = newValue;
   }

   /**
    * Zwraca roczny przych�d na koncie emerytalnym.
    * @return kwota przychodu
    */
   public double getContrib()
   {
      return contrib;
   }

   /**
    * Okre�la roczny przych�d na koncie emerytalnym.
    * @param newValue kwota przychodu
    */
   public void setContrib(double newValue)
   {
      contrib = newValue;
   }

   /**
    * Zwraca roczny przych�d.
    * @return wysoko�� przychodu
    */
   public double getIncome()
   {
      return income;
   }

   /**
    * Okre�la roczny przych�d.
    * @param newValue wysoko�� przychodu
    */
   public void setIncome(double newValue)
   {
      income = newValue;
   }

   /**
    * Zwraca aktualny wiek.
    * @return wiek
    */
   public int getCurrentAge()
   {
      return currentAge;
   }

   /**
    * Okre�la aktualny wiek.
    * @param newValue wiek
    */
   public void setCurrentAge(int newValue)
   {
      currentAge = newValue;
   }

   /**
    * Zwraca oczekiwany wiek przej�cia na emerytur�.
    * @return wiek
    */
   public int getRetireAge()
   {
      return retireAge;
   }

   /**
    * Okre�la oczekiwany wiek przej�cia na emerytur�.
    * @param newValue wiek
    */
   public void setRetireAge(int newValue)
   {
      retireAge = newValue;
   }

   /**
    * Zwraca spodziewany wiek w momencie �mierci.
    * @return wiek
    */
   public int getDeathAge()
   {
      return deathAge;
   }

   /**
    * Okre�la spodziewany wiek w momencie �mierci.
    * @param newValue wiek
    */
   public void setDeathAge(int newValue)
   {
      deathAge = newValue;
   }

   /**
    * Zwraca spodziewan� wysoko�� inflacji.
    * @return wysoko�� inflacji w procentach
    */
   public double getInflationPercent()
   {
      return inflationPercent;
   }

   /**
    * Okre�la spodziewan� wysoko�� inflacji.
    * @param newValue wysoko�� inflacji w procentach
    */
   public void setInflationPercent(double newValue)
   {
      inflationPercent = newValue;
   }

   /**
    * Zwraca oczekiwany zwrot inwestycji.
    * @return zwrot inwestycji w procentach
    */
   public double getInvestPercent()
   {
      return investPercent;
   }

   /**
    * Okre�la oczekiwany zwrot inwestycji.
    * @param newValue zwrot inwestycji w procentach
    */
   public void setInvestPercent(double newValue)
   {
      investPercent = newValue;
   }
}

/**
 * Komponent rysuj�cy wykres inwestycji.
 */
class RetireComponent extends JComponent
{
   private static final int DEFAULT_WIDTH = 800;
   private static final int DEFAULT_HEIGHT = 600;
   private static final int PANEL_WIDTH = 400;
   private static final int PANEL_HEIGHT = 200;

   private RetireInfo info = null;
   private Color colorPre;
   private Color colorGain;
   private Color colorLoss;

   public RetireComponent()
   {
      setSize(PANEL_WIDTH, PANEL_HEIGHT);
   }

   /**
    * Okre�la informacj� prezentowan� na wykresie.
    * @param newInfo informacja o emeryturze
    */
   public void setInfo(RetireInfo newInfo)
   {
      info = newInfo;
      repaint();
   }

   public void paintComponent(Graphics g)
   {
      Graphics2D g2 = (Graphics2D) g;
      if (info == null) return;

      double minValue = 0;
      double maxValue = 0;
      int i;
      for (i = info.getCurrentAge(); i <= info.getDeathAge(); i++)
      {
         double v = info.getBalance(i);
         if (minValue > v) minValue = v;
         if (maxValue < v) maxValue = v;
      }
      if (maxValue == minValue) return;

      int barWidth = getWidth() / (info.getDeathAge() - info.getCurrentAge() + 1);
      double scale = getHeight() / (maxValue - minValue);

      for (i = info.getCurrentAge(); i <= info.getDeathAge(); i++)
      {
         int x1 = (i - info.getCurrentAge()) * barWidth + 1;
         int y1;
         double v = info.getBalance(i);
         int height;
         int yOrigin = (int) (maxValue * scale);

         if (v >= 0)
         {
            y1 = (int) ((maxValue - v) * scale);
            height = yOrigin - y1;
         }
         else
         {
            y1 = yOrigin;
            height = (int) (-v * scale);
         }

         if (i < info.getRetireAge()) g2.setPaint(colorPre);
         else if (v >= 0) g2.setPaint(colorGain);
         else g2.setPaint(colorLoss);
         Rectangle2D bar = new Rectangle2D.Double(x1, y1, barWidth - 2, height);
         g2.fill(bar);
         g2.setPaint(Color.black);
         g2.draw(bar);
      }
   }

   /**
    * Okre�la kolor s�upk�w wykresu przed przej�ciem na emerytur�.
    * @param color wymagany kolor
    */
   public void setColorPre(Color color)
   {
      colorPre = color;
      repaint();
   }

   /**
    * Okre�la kolor s�upk�w wykresu po przej�ciu na emerytur�,
    * gdy stan konta jest dodatni.
    * @param color wymagany kolor
    */
   public void setColorGain(Color color)
   {
      colorGain = color;
      repaint();
   }

   /**
    * Okre�la kolor s�upk�w wykresu po przej�ciu na emerytur�,
    * gdy stan konta jest ujemny.
    * @param color wymagany kolor
    */
   public void setColorLoss(Color color)
   {
      colorLoss = color;
      repaint();
   }
   
   public Dimension getPreferredSize() { return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT); }   
}
