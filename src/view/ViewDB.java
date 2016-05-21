package view;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.sql.rowset.*;
import javax.swing.*;

/**
 * Program wykorzystujący metadane
 * do prezentacji dowolnych tabel bazy danych.
 * @version 1.32 2012-01-26
 * @author Cay Horstmann
 */
public class ViewDB
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFrame frame = new ViewDBFrame();
                frame.setTitle("ViewDB");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}

/**
 * Ramka zawierająca panel danych i przyciski nawigacji.
 */
class ViewDBFrame extends JFrame
{
    private JButton previousButton;
    private JButton nextButton;
    private JButton deleteButton;
    private JButton saveButton;
    private DataPanel dataPanel;
    private Component scrollPane;
    private JComboBox<String> tableNames;
    private Properties props;
    private CachedRowSet crs;

    public ViewDBFrame()
    {
        tableNames = new JComboBox<String>();
        tableNames.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                showTable((String) tableNames.getSelectedItem());
            }
        });
        add(tableNames, BorderLayout.NORTH);

        try
        {
            readDatabaseProperties();
            try (Connection conn = getConnection())
            {
                DatabaseMetaData meta = conn.getMetaData();
                ResultSet mrs = meta.getTables(null, null, null, new String[] { "TABLE" });
                while (mrs.next())
                    tableNames.addItem(mrs.getString(3));
            }
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this, e);
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(this, e);
        }

        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        previousButton = new JButton("Previous");
        previousButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                showPreviousRow();
            }
        });
        buttonPanel.add(previousButton);

        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                showNextRow();
            }
        });
        buttonPanel.add(nextButton);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                deleteRow();
            }
        });
        buttonPanel.add(deleteButton);

        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                saveChanges();
            }
        });
        buttonPanel.add(saveButton);
        pack();
    }

    /**
     * Przygotowuje pola tekstowe do prezentacji nowej tabeli
     * i wyświetla zawartość jej pierwszego rekordu.
     * @param tableName nazwa prezentowanej tabeli
     */
    public void showTable(String tableName)
    {
        try
        {
            try (Connection conn = getConnection())
            {
                // pobiera zbiór wyników
                Statement stat = conn.createStatement();
                ResultSet result = stat.executeQuery("SELECT * FROM " + tableName);
                // kopiuje je do buforowanego zbioru rekordów
                RowSetFactory factory = RowSetProvider.newFactory();
                crs = factory.createCachedRowSet();
                crs.setTableName(tableName);
                crs.populate(result);
            }

            if (scrollPane != null) remove(scrollPane);
            dataPanel = new DataPanel(crs);
            scrollPane = new JScrollPane(dataPanel);
            add(scrollPane, BorderLayout.CENTER);
            validate();
            showNextRow();
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    /**
     * Pokazuje poprzedni rekord tabeli.
     */
    public void showPreviousRow()
    {
        try
        {
            if (crs == null || crs.isFirst()) return;
            crs.previous();
            dataPanel.showRow(crs);
        }
        catch (SQLException e)
        {
            for (Throwable t : e)
                t.printStackTrace();
        }
    }

    /**
     * Pokazuje następny rekord tabeli.
     */
    public void showNextRow()
    {
        try
        {
            if (crs == null || crs.isLast()) return;
            crs.next();
            dataPanel.showRow(crs);
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    /**
     * Usuwa bieżący rekord tabeli.
     */
    public void deleteRow()
    {
        try
        {
            try (Connection conn = getConnection())
            {
                crs.deleteRow();
                crs.acceptChanges(conn);
                if (crs.isAfterLast())
                    if (!crs.last()) crs = null;
                dataPanel.showRow(crs);
            }
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    /**
     * Zapisuje wszystkie modyfikacje.
     */
    public void saveChanges()
    {
        try
        {
            try (Connection conn = getConnection())
            {
                dataPanel.setRow(crs);
                crs.acceptChanges(conn);
            }
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    private void readDatabaseProperties() throws IOException
    {
        props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("/home/hubert/IdeaProjects/javtz9/src/database.properties")))
        {
            props.load(in);
        }
        String drivers = props.getProperty("jdbc.drivers");
        if (drivers != null) System.setProperty("jdbc.drivers", drivers);
    }

    /**
     * Nawiązuje połączenie z bazą danych,
     * korzystając z właściwości zapisanych
     * w pliku database.properties.
     * @return połączenie z bazą danych
     */
    private Connection getConnection() throws SQLException
    {
        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);
    }
}

/**
 * Panel wyświetlający zawartość zbioru rekordów.
 */
class DataPanel extends JPanel
{
    private java.util.List<JTextField> fields;

    /**
     * Tworzy panel danych.
     * @param rs zbiór rekordów prezentowany przez panel
     */
    public DataPanel(RowSet rs) throws SQLException
    {
        fields = new ArrayList<>();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        ResultSetMetaData rsmd = rs.getMetaData();
        for (int i = 1; i <= rsmd.getColumnCount(); i++)
        {
            gbc.gridy = i - 1;

            String columnName = rsmd.getColumnLabel(i);
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.EAST;
            add(new JLabel(columnName), gbc);

            int columnWidth = rsmd.getColumnDisplaySize(i);
            JTextField tb = new JTextField(columnWidth);
            if (!rsmd.getColumnClassName(i).equals("java.lang.String"))
                tb.setEditable(false);

            fields.add(tb);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            add(tb, gbc);
        }
    }

    /**
     * Pokazuje rekord bazy danych,
     * wypełniając pola tekstowe danymi kolejnych kolumn.
     */
    public void showRow(ResultSet rs) throws SQLException
    {
        for (int i = 1; i <= fields.size(); i++)
        {
            String field = rs == null ? "" : rs.getString(i);
            JTextField tb = fields.get(i - 1);
            tb.setText(field);
        }
    }

    /**
     * Aktualizuje dane zmodyfikowane w bieżącym rekordzie.
     */
    public void setRow(RowSet rs) throws SQLException
    {
        for (int i = 1; i <= fields.size(); i++)
        {
            String field = rs.getString(i);
            JTextField tb = fields.get(i - 1);
            if (!field.equals(tb.getText()))
                rs.updateString(i, tb.getText());
        }
        rs.updateRow();
    }
}