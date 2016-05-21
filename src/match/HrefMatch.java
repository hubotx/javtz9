package match;

import java.io.*;
import java.net.*;
import java.util.regex.*;

/**
 * Program wyświetlający wszystkie adresy URL na stronie WWW
 * poprzez dopasowanie wyrażenia regularnego
 * opisującego znacznik <a href=...> języka HTML.
 * Uruchamianie: java match.HrefMatch adresURL
 * @version 1.01 2004-06-04
 * @author Cay Horstmann
 */
public class HrefMatch
{
    public static void main(String[] args)
    {
        try
        {
            // pobiera URL z wiersza poleceń lub używa domyślnego
            String urlString;
            if (args.length > 0) urlString = args[0];
            else urlString = "http://java.sun.com";

            // otwiera InputStreamReader dla podanego URL
            InputStreamReader in = new InputStreamReader(new URL(urlString).openStream());

            // wczytuje zawartość do obiektu klasy StringBuilder
            StringBuilder input = new StringBuilder();
            int ch;
            while ((ch = in.read()) != -1)
                input.append((char) ch);

            // poszukuje wszystkich wystąpień wzorca
            String patternString = "<a\\s+href\\s*=\\s*(\"[^\"]*\"|[^\\s>]*)\\s*>";
            Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);

            while (matcher.find())
            {
                int start = matcher.start();
                int end = matcher.end();
                String match = input.substring(start, end);
                System.out.println(match);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (PatternSyntaxException e)
        {
            e.printStackTrace();
        }
    }
}
