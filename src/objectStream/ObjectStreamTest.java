package objectStream;

import java.io.*;

/**
 * @version 1.10 17 Aug 1998
 * @author Cay Horstmann
 */
class ObjectStreamTest
{
    public static void main(String[] args) throws IOException, ClassNotFoundException
    {
        Employee harry = new Employee("Harry Hacker", 50000, 1989, 10, 1);
        Manager carl = new Manager("Carl Cracker", 80000, 1987, 12, 15);
        carl.setSecretary(harry);
        Manager tony = new Manager("Tony Tester", 40000, 1990, 3, 15);
        tony.setSecretary(harry);

        Employee[] staff = new Employee[3];
        staff[0] = carl;
        staff[1] = harry;
        staff[2] = tony;

        // zapisuje rekordy wszystkich pracowników w pliku employee.dat
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("employee.dat")))
        {
            out.writeObject(staff);
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("employee.dat")))
        {
            // wczytuje wszystkie rekordy do nowej tablicy

            Employee[] newStaff = (Employee[]) in.readObject();

            // podnosi wynagrodzenie asystenta
            newStaff[1].raiseSalary(10);

            // wyświetla wszystkie wczytane rekordy
            for (Employee e : newStaff)
                System.out.println(e);
        }
    }
}
