package inetAddress;

import java.io.*;
import java.net.*;

/**
 * Program demonstrujący zastosowanie klasy InetAddress.
 * W wierszu poleceń przekazujemy mu jako parametr nazwę hosta.
 * W przeciwnym razie uzyskamy adres lokalnego hosta.
 * @version 1.02 2012-06-05
 * @author Cay Horstmann
 */
public class InetAddressTest
{
    public static void main(String[] args) throws IOException
    {
        if (args.length > 0)
        {
            String host = args[0];
            InetAddress[] addresses = InetAddress.getAllByName(host);
            for (InetAddress a : addresses)
                System.out.println(a);
        }
        else
        {
            InetAddress localHostAddress = InetAddress.getLocalHost();
            System.out.println(localHostAddress);
        }
    }
}
