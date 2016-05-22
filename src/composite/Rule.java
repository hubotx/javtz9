package composite;

import java.awt.*;

/**
 * Klasa reprezentująca regułę Portera-Duffa.
 */
class Rule
{
    private String name;
    private String porterDuff1;
    private String porterDuff2;

    /**
     * Tworzy obiekt reprezentujący regułę Portera-Duffa.
     * @param n nazwa reguły
     * @param pd1 pierwszy wiersz diagramu Portera-Duffa
     * @param pd2 drugi wiersz diagramu Portera-Duffa
     */
    public Rule(String n, String pd1, String pd2)
    {
        name = n;
        porterDuff1 = pd1;
        porterDuff2 = pd2;
    }

    /**
     * Zwraca objaśnienie sposobu działania reguły.
     * @return objaśnienie
     */
    public String getExplanation()
    {
        StringBuilder r = new StringBuilder("Source ");
        if (porterDuff2.equals("  ")) r.append("clears");
        if (porterDuff2.equals(" S")) r.append("overwrites");
        if (porterDuff2.equals("DS")) r.append("blends with");
        if (porterDuff2.equals(" D")) r.append("alpha modifies");
        if (porterDuff2.equals("D ")) r.append("alpha complement modifies");
        if (porterDuff2.equals("DD")) r.append("does not affect");
        r.append(" destination");
        if (porterDuff1.equals(" S")) r.append(" and overwrites empty pixels");
        r.append(".");
        return r.toString();
    }

    @Override
    public String toString()
    {
        return name;
    }

    /**
     * Zwraca wartość wyznaczoną przez regułę
     * jako obiekt klasy AlphaComposite.
     * @return stała AlphaComposite lub -1, jeśli nie istnieje odpowiednia stała.
     */
    public int getValue()
    {
        try
        {
            return (Integer) AlphaComposite.class.getField(name).get(null);
        }
        catch (Exception e)
        {
            return -1;
        }
    }
}
