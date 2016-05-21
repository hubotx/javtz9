package test;

import java.nio.file.*;
import java.sql.*;
import java.io.*;
import java.util.*;

/**
 * Program sprawdzający poprawność konfiguracji
 * bazy danych i sterownika JDBC.
 * @version 1.02 2012-06-05
 * @author Cay Horstmann
 */
public class TestDB
{
    public static void main(String[] args) throws IOException
    {
        try
        {
            runTest(args[0]);
        }
        catch (SQLException ex)
        {
            for (Throwable t : ex)
                t.printStackTrace();
        }
    }

    /**
     * Wykonuje test polegający na utworzeniu tabeli, wstawieniu do niej wartości,
     * prezentacji zawartości, usunięciu tabeli.
     */
    public static void runTest(String filepath) throws SQLException, IOException
    {
        try (Connection conn = getConnection(filepath))
        {
            Statement stat = conn.createStatement();
            stat.executeUpdate("CREATE TABLE Greetings (Message CHAR(20))");
            stat.executeUpdate("INSERT INTO Greetings VALUES ('Hello, World!')");

            try (ResultSet result = stat.executeQuery("SELECT * FROM Greetings"))
            {
                if (result.next())
                    System.out.println(result.getString(1));
            }
            stat.executeUpdate("DROP TABLE Greetings");
        }
    }

    /**
     * Nawiązuje połączenie, korzystając
     * z właściwości w pliku database.properties
     * @return połączenie z bazą danych
     */
    public static Connection getConnection(String filepath) throws SQLException, IOException
    {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get(filepath)))
        {
            props.load(in);
        }
        String drivers = props.getProperty("jdbc.drivers");
        if (drivers != null) System.setProperty("jdbc.drivers", drivers);

        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);
    }
}
