package tree;

import javax.swing.*;
import javax.swing.tree.*;

/**
 * Ramka zawieraj¹ca drzewo wyœwietlaj¹ce dane
 * "rêcznie" utworzonego modelu drzewa.
 */
public class SimpleTreeFrame extends JFrame
{
   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 200;

   public SimpleTreeFrame()
   {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // tworzy model drzewa

      DefaultMutableTreeNode root = new DefaultMutableTreeNode("World");
      DefaultMutableTreeNode country = new DefaultMutableTreeNode("USA");
      root.add(country);
      DefaultMutableTreeNode state = new DefaultMutableTreeNode("California");
      country.add(state);
      DefaultMutableTreeNode city = new DefaultMutableTreeNode("San Jose");
      state.add(city);
      city = new DefaultMutableTreeNode("Cupertino");
      state.add(city);
      state = new DefaultMutableTreeNode("Michigan");
      country.add(state);
      city = new DefaultMutableTreeNode("Ann Arbor");
      state.add(city);
      country = new DefaultMutableTreeNode("Germany");
      root.add(country);
      state = new DefaultMutableTreeNode("Schleswig-Holstein");
      country.add(state);
      city = new DefaultMutableTreeNode("Kiel");
      state.add(city);

      // tworzy drzewo i umieszcza je w przewijalnym panelu

      JTree tree = new JTree(root);
      add(new JScrollPane(tree));
   }
}
