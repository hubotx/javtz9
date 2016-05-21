package randomAccess;

import java.io.*;
import java.util.*;

/**
 * @version 1.12 2012-05-30
 * @author Cay Horstmann
 */
public class RandomAccessTest
{
    public static void main(String[] args) throws IOException
    {
        Employee[] staff = new Employee[3];

        staff[0] = new Employee("Carl Cracker", 75000, 1987, 12, 15);
        staff[1] = new Employee("Harry Hacker", 50000, 1989, 10, 1);
        staff[2] = new Employee("Tony Tester", 40000, 1990, 3, 15);

        try (DataOutputStream out = new DataOutputStream(new FileOutputStream("employee.dat")))
        {
            // zapisuje rekordy wszystkich pracowników w pliku employee.dat
            for (Employee e : staff)
                writeData(out, e);
        }

        try (RandomAccessFile in = new RandomAccessFile("employee.dat", "r"))
        {
            // wczytuje rekordy do nowej tablicy

            // oblicza rozmiar tablicy
            int n = (int)(in.length() / Employee.RECORD_SIZE);
            Employee[] newStaff = new Employee[n];

            // wczytuje rekordy pracowników w odwrotnej kolejności
            for (int i = n - 1; i >= 0; i--)
            {
                newStaff[i] = new Employee();
                in.seek(i * Employee.RECORD_SIZE);
                newStaff[i] = readData(in);
            }

            // wyświetla wczytane rekordy
            for (Employee e : newStaff)
                System.out.println(e);
        }
    }

    /**
     * Zapisuje dane pracownika
     * @param out obiekt typu DataOutput
     * @param e pracownik
     */
    public static void writeData(DataOutput out, Employee e) throws IOException
    {
        DataIO.writeFixedString(e.getName(), Employee.NAME_SIZE, out);
        out.writeDouble(e.getSalary());

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(e.getHireDay());
        out.writeInt(calendar.get(Calendar.YEAR));
        out.writeInt(calendar.get(Calendar.MONTH) + 1);
        out.writeInt(calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Wczytuje dane pracownika
     * @param in obiekt typu DataInput
     * @return pracownik
     */
    public static Employee readData(DataInput in) throws IOException
    {
        String name = DataIO.readFixedString(Employee.NAME_SIZE, in);
        double salary = in.readDouble();
        int y = in.readInt();
        int m = in.readInt();
        int d = in.readInt();
        return new Employee(name, salary, y, m - 1, d);
    }
}
