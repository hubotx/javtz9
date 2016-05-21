package treeModel;

import java.awt.*;
import javax.swing.*;

/**
 * Ramka zawieraj¹ca drzewo.
 */
public class ObjectInspectorFrame extends JFrame
{
   private JTree tree;
   private static final int DEFAULT_WIDTH = 400;
   private static final int DEFAULT_HEIGHT = 300;

   public ObjectInspectorFrame()
   {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // jako pierwszy inspekcji poddany jest obiekt ramki

      Variable v = new Variable(getClass(), "this", this);
      ObjectTreeModel model = new ObjectTreeModel();
      model.setRoot(v);

      // tworzy i prezentuje drzewo

      tree = new JTree(model);
      add(new JScrollPane(tree), BorderLayout.CENTER);
   }
}
