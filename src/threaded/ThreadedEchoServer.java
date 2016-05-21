package threaded;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Program implementujący wielowątkowy serwer
 * nasłuchujący na porcie 8189
 * i wysyłający echo informacji otrzymanej od klientów.
 * @author Cay Horstmann
 * @version 1.21 2012-06-04
 */
public class ThreadedEchoServer
{
    public static void main(String[] args)
    {
        try
        {
            int i = 1;
            ServerSocket s = new ServerSocket(8189);
            s.bind(new InetSocketAddress("25.113.24.168", 8189));

            while (true)
            {
                Socket incoming = s.accept();
                System.out.println("Spawning " + i);
                Runnable r = new ThreadedEchoHandler(incoming);
                Thread t = new Thread(r);
                t.start();
                i++;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

/**
 * Klasa obsługująca komunikację z pojedynczym klientem.
 */
class ThreadedEchoHandler implements Runnable
{
    private Socket incoming;

    /**
     * Tworzy obiekt obsługi.
     * @param i gniazdo sieciowe
     */
    public ThreadedEchoHandler(Socket i)
    {
        incoming = i;
    }

    public void run()
    {
        try
        {
            try
            {
                InputStream inStream = incoming.getInputStream();
                OutputStream outStream = incoming.getOutputStream();

                Scanner in = new Scanner(inStream);
                PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);

                out.println("Hello! Enter BYE to exit.");

                // wyświetla echo
                boolean done = false;
                while (!done && in.hasNextLine())
                {
                    String line = in.nextLine();
                    out.println("Echo: " + line);
                    if (line.trim().equals("BYE"))
                        done = true;
                }
            }
            finally
            {
                incoming.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}