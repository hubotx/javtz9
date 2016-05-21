package treeRender;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * Ramka zawieraj¹ca drzewo klas, pole tekstowe pokazuj¹ce
 * sk³adowe wybranej klasy oraz pole tekstowe umo¿liwiaj¹ce
 * dodawanie nowych klas do drzewa.
 */
public class ClassTreeFrame extends JFrame
{
   private static final int DEFAULT_WIDTH = 400;
   private static final int DEFAULT_HEIGHT = 300;

   private DefaultMutableTreeNode root;
   private DefaultTreeModel model;
   private JTree tree;
   private JTextField textField;
   private JTextArea textArea;

   public ClassTreeFrame()
   {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // w korzeniu drzewa znajduje siê klasa Object
      root = new DefaultMutableTreeNode(java.lang.Object.class);
      model = new DefaultTreeModel(root);
      tree = new JTree(model);

      // dodaje klasê do drzewa
      addClass(getClass());

      // tworzy ikony wêz³ów
      ClassNameTreeCellRenderer renderer = new ClassNameTreeCellRenderer();
      renderer.setClosedIcon(new ImageIcon(getClass().getResource("red-ball.gif")));
      renderer.setOpenIcon(new ImageIcon(getClass().getResource("yellow-ball.gif")));
      renderer.setLeafIcon(new ImageIcon(getClass().getResource("blue-ball.gif")));
      tree.setCellRenderer(renderer);
      
      // konfiguruje sposób wyboru wêz³ów
      tree.addTreeSelectionListener(new TreeSelectionListener()
         {
            public void valueChanged(TreeSelectionEvent event)
            {
               // u¿ytkownik wybra³ inny wêze³ drzewa
               // i nale¿y zaktualizowaæ opis klasy
               TreePath path = tree.getSelectionPath();
               if (path == null) return;
               DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path
                     .getLastPathComponent();
               Class<?> c = (Class<?>) selectedNode.getUserObject();
               String description = getFieldDescription(c);
               textArea.setText(description);
            }
         });
      int mode = TreeSelectionModel.SINGLE_TREE_SELECTION;
      tree.getSelectionModel().setSelectionMode(mode);

      // obszar tekstowy zawieraj¹cy opis klasy
      textArea = new JTextArea();

      // dodaje komponenty drzewa i pola tekstowego do panelu
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(1, 2));
      panel.add(new JScrollPane(tree));
      panel.add(new JScrollPane(textArea));

      add(panel, BorderLayout.CENTER);

      addTextField();
   }

   /**
    * Dodaje pole tekstowe i przycisk "Add"
    * umo¿liwiaj¹ce dodanie nowej klasy do drzewa.
    */
   public void addTextField()
   {
      JPanel panel = new JPanel();

      ActionListener addListener = new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               // dodaje do drzewa klasê, której nazwa
               // znajduje siê w polu tekstowym
               try
               {
                  String text = textField.getText();
                  addClass(Class.forName(text)); // opró¿nia zawartoœæ 
                  textField.setText("");
               }
               catch (ClassNotFoundException e)
               {
                  JOptionPane.showMessageDialog(null, "Class not found");
               }
            }
         };

      // pole tekstowe, w którym wprowadzane s¹ nazwy nowych klas
      textField = new JTextField(20);
      textField.addActionListener(addListener);
      panel.add(textField);

      JButton addButton = new JButton("Add");
      addButton.addActionListener(addListener);
      panel.add(addButton);

      add(panel, BorderLayout.SOUTH);
   }

   /**
    * Wyszukuje obiekt w drzewie.
    * @param obj szukany obiekt
    * @return wêze³ zawieraj¹cy szukany obiekt lub null,
    * jeœli obiekt nie znajduje siê w drzewie
    */
   @SuppressWarnings("unchecked")
   public DefaultMutableTreeNode findUserObject(Object obj)
   {
      // szuka wêz³a zawieraj¹cego obiekt u¿ytkownika
      Enumeration<TreeNode> e = (Enumeration<TreeNode>) root.breadthFirstEnumeration();
      while (e.hasMoreElements())
      {
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
         if (node.getUserObject().equals(obj)) return node;
      }
      return null;
   }

   /**
    * Dodaje do drzewa klasê i jej klasy bazowe,
    * których nie ma jeszcze w drzewie.
    * @param c dodawana klasa
    * @return nowo dodany wêze³.
    */
   public DefaultMutableTreeNode addClass(Class<?> c)
   {
      // dodaje klasê do drzewa

      // pomija typy, które nie s¹ klasami
      if (c.isInterface() || c.isPrimitive()) return null;

      // jeœli klasa znajduje siê ju¿ w drzewie, to zwraca jej wêze³
      DefaultMutableTreeNode node = findUserObject(c);
      if (node != null) return node;

      // jeœli klasa nie znajduje siê w drzewie,
      // to najpierw nale¿y dodaæ rekurencyjnie do drzewa jej klasy bazowe

      Class<?> s = c.getSuperclass();

      DefaultMutableTreeNode parent;
      if (s == null) parent = root;
      else parent = addClass(s);

      // dodaje klasê jako wêze³ podrzêdny
      DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(c);
      model.insertNodeInto(newNode, parent, parent.getChildCount());

      // sprawia, ¿e wêze³ jest widoczny
      TreePath path = new TreePath(model.getPathToRoot(newNode));
      tree.makeVisible(path);

      return newNode;
   }

   /**
    * Zwraca opis sk³adowych klasy.
    * @param klasa
    * @return ³añcuch znaków zawieraj¹cy nazwy i typy zmiennych
    */
   public static String getFieldDescription(Class<?> c)
   {
      // korzysta z mechanizmu refleksji
      StringBuilder r = new StringBuilder();
      Field[] fields = c.getDeclaredFields();
      for (int i = 0; i < fields.length; i++)
      {
         Field f = fields[i];
         if ((f.getModifiers() & Modifier.STATIC) != 0) r.append("static ");
         r.append(f.getType().getName());
         r.append(" ");
         r.append(f.getName());
         r.append("\n");
      }
      return r.toString();
   }
}
