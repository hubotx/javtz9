package treeRender;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * Ramka zawieraj�ca drzewo klas, pole tekstowe pokazuj�ce
 * sk�adowe wybranej klasy oraz pole tekstowe umo�liwiaj�ce
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

      // w korzeniu drzewa znajduje si� klasa Object
      root = new DefaultMutableTreeNode(java.lang.Object.class);
      model = new DefaultTreeModel(root);
      tree = new JTree(model);

      // dodaje klas� do drzewa
      addClass(getClass());

      // tworzy ikony w�z��w
      ClassNameTreeCellRenderer renderer = new ClassNameTreeCellRenderer();
      renderer.setClosedIcon(new ImageIcon(getClass().getResource("red-ball.gif")));
      renderer.setOpenIcon(new ImageIcon(getClass().getResource("yellow-ball.gif")));
      renderer.setLeafIcon(new ImageIcon(getClass().getResource("blue-ball.gif")));
      tree.setCellRenderer(renderer);
      
      // konfiguruje spos�b wyboru w�z��w
      tree.addTreeSelectionListener(new TreeSelectionListener()
         {
            public void valueChanged(TreeSelectionEvent event)
            {
               // u�ytkownik wybra� inny w�ze� drzewa
               // i nale�y zaktualizowa� opis klasy
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

      // obszar tekstowy zawieraj�cy opis klasy
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
    * umo�liwiaj�ce dodanie nowej klasy do drzewa.
    */
   public void addTextField()
   {
      JPanel panel = new JPanel();

      ActionListener addListener = new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               // dodaje do drzewa klas�, kt�rej nazwa
               // znajduje si� w polu tekstowym
               try
               {
                  String text = textField.getText();
                  addClass(Class.forName(text)); // opr�nia zawarto�� 
                  textField.setText("");
               }
               catch (ClassNotFoundException e)
               {
                  JOptionPane.showMessageDialog(null, "Class not found");
               }
            }
         };

      // pole tekstowe, w kt�rym wprowadzane s� nazwy nowych klas
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
    * @return w�ze� zawieraj�cy szukany obiekt lub null,
    * je�li obiekt nie znajduje si� w drzewie
    */
   @SuppressWarnings("unchecked")
   public DefaultMutableTreeNode findUserObject(Object obj)
   {
      // szuka w�z�a zawieraj�cego obiekt u�ytkownika
      Enumeration<TreeNode> e = (Enumeration<TreeNode>) root.breadthFirstEnumeration();
      while (e.hasMoreElements())
      {
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
         if (node.getUserObject().equals(obj)) return node;
      }
      return null;
   }

   /**
    * Dodaje do drzewa klas� i jej klasy bazowe,
    * kt�rych nie ma jeszcze w drzewie.
    * @param c dodawana klasa
    * @return nowo dodany w�ze�.
    */
   public DefaultMutableTreeNode addClass(Class<?> c)
   {
      // dodaje klas� do drzewa

      // pomija typy, kt�re nie s� klasami
      if (c.isInterface() || c.isPrimitive()) return null;

      // je�li klasa znajduje si� ju� w drzewie, to zwraca jej w�ze�
      DefaultMutableTreeNode node = findUserObject(c);
      if (node != null) return node;

      // je�li klasa nie znajduje si� w drzewie,
      // to najpierw nale�y doda� rekurencyjnie do drzewa jej klasy bazowe

      Class<?> s = c.getSuperclass();

      DefaultMutableTreeNode parent;
      if (s == null) parent = root;
      else parent = addClass(s);

      // dodaje klas� jako w�ze� podrz�dny
      DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(c);
      model.insertNodeInto(newNode, parent, parent.getChildCount());

      // sprawia, �e w�ze� jest widoczny
      TreePath path = new TreePath(model.getPathToRoot(newNode));
      tree.makeVisible(path);

      return newNode;
   }

   /**
    * Zwraca opis sk�adowych klasy.
    * @param klasa
    * @return �a�cuch znak�w zawieraj�cy nazwy i typy zmiennych
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
