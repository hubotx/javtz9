package serialClone;

/**
 * @version 1.20 17 Aug 1998
 * @author Cay Horstmann
 */

import java.io.*;
import java.util.*;

public class SerialCloneTest
{
    public static void main(String[] args)
    {
        Employee harry = new Employee("Harry Hacker", 35000, 1989, 10, 1);
        // klonuje obiekt harry
        Employee harry2 = (Employee) harry.clone();

        // modyfikuje obiekt harry
        harry.raiseSalary(10);

        // teraz obiekt harry i jego klon różnią się
        System.out.println(harry);
        System.out.println(harry2);
    }
}

class SerialCloneable implements Cloneable, Serializable
{
    public Object clone()
    {
        try
        {
            // zapisuje obiekt w tablicy bajtów
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bout);
            out.writeObject(this);
            out.close();

            // wczytuje klon obiektu z tablicy bajtów
            ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bin);
            Object ret = in.readObject();
            in.close();

            return ret;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}

/**
 * Znana już klasa Employee,
 * tym razem jako pochodna klasy SerialCloneable.
 */
class Employee extends SerialCloneable
{
    private String name;
    private double salary;
    private Date hireDay;

    public Employee(String n, double s, int year, int month, int day)
    {
        name = n;
        salary = s;
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
        hireDay = calendar.getTime();
    }

    public String getName()
    {
        return name;
    }

    public double getSalary()
    {
        return salary;
    }

    public Date getHireDay()
    {
        return hireDay;
    }

    public void raiseSalary(double byPercent)
    {
        double raise = salary * byPercent / 100;
        salary += raise;
    }

    public String toString()
    {
        return getClass().getName()
                + "[name=" + name
                + ",salary=" + salary
                + ",hireDay=" + hireDay
                + "]";
    }
}