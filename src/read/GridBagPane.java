package read;

import java.awt.*;
import java.beans.*;
import java.io.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * Panel wykorzystujący plik XML zawierający
 * opis komponentów i ich układu na siatce typu GridBagLayout.
 */
public class GridBagPane extends JPanel
{
    private GridBagConstraints constraints;

    /**
     * Tworzy obiekt klasy GridBagPane.
     * @param filename nazwa pliku XML opisującego komponenty
     * i ich układ
     */
    public GridBagPane(File file)
    {
        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);

            if (file.toString().contains("-schema"))
            {
                factory.setNamespaceAware(true);
                final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
                final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
                factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            }

            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            parseGridbag(doc.getDocumentElement());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Zwraca komponent o podanej nazwie
     * @param name nazwa komponentu
     * @return komponent o podanej nazwie lub wartość null,
     * jeśli żaden z komponentów panelu nie posiada takiej nazwy
     */
    public Component get(String name)
    {
        Component[] components = getComponents();
        for (int i = 0; i < components.length; i++)
        {
            if (components[i].getName().equals(name)) return components[i];
        }
        return null;
    }

    /**
     * Parsuje element panelu.
     * @param e element panelu
     */
    private void parseGridbag(Element e)
    {
        NodeList rows = e.getChildNodes();
        for (int i = 0; i < rows.getLength(); i++)
        {
            Element row = (Element) rows.item(i);
            NodeList cells = row.getChildNodes();
            for (int j = 0; j < cells.getLength(); j++)
            {
                Element cell = (Element) cells.item(j);
                parseCell(cell, i, j);
            }
        }
    }

    /**
     * Parsuje element komórki.
     * @param e element komórki
     * @param r wiersz komórki
     * @param c kolumna komórki
     */
    private void parseCell(Element e, int r, int c)
    {
        // pobiera atrybuty

        String value = e.getAttribute("gridx");
        if (value.length() == 0) // stosuje wartość domyślną
        {
            if (c == 0) constraints.gridx = 0;
            else constraints.gridx += constraints.gridwidth;
        }
        else constraints.gridx = Integer.parseInt(value);

        value = e.getAttribute("gridy");
        if (value.length() == 0) // stosuje wartość domyślną
        constraints.gridy = r;
        else constraints.gridy = Integer.parseInt(value);

        constraints.gridwidth = Integer.parseInt(e.getAttribute("gridwidth"));
        constraints.gridheight = Integer.parseInt(e.getAttribute("gridheight"));
        constraints.weightx = Integer.parseInt(e.getAttribute("weightx"));
        constraints.weighty = Integer.parseInt(e.getAttribute("weighty"));
        constraints.ipadx = Integer.parseInt(e.getAttribute("ipadx"));
        constraints.ipady = Integer.parseInt(e.getAttribute("ipady"));

        // stosuje refleksję dla uzyskania wartości całkowitych pól statycznych
        Class<GridBagConstraints> cl = GridBagConstraints.class;

        try
        {
            String name = e.getAttribute("fill");
            Field f = cl.getField(name);
            constraints.fill = f.getInt(cl);

            name = e.getAttribute("anchor");
            f = cl.getField(name);
            constraints.anchor = f.getInt(cl);
        }
        catch (Exception ex) // metody refleksji mogą wyrzucać różne wyjątki
        {
            ex.printStackTrace();
        }

        Component comp = (Component) parseBean((Element) e.getFirstChild());
        add(comp, constraints);
    }

    /**
     * Parsuj element komponentu.
     * @param e element komponentu
     */
    private Object parseBean(Element e)
    {
        try
        {
            NodeList children = e.getChildNodes();
            Element classElement = (Element) children.item(0);
            String className = ((Text) classElement.getFirstChild()).getData();

            Class<?> cl = Class.forName(className);

            Object obj = cl.newInstance();

            if (obj instanceof Component) ((Component) obj).setName(e.getAttribute("id"));

            for (int i = 1; i < children.getLength(); i++)
            {
                Node propertyElement = children.item(i);
                Element nameElement = (Element) propertyElement.getFirstChild();
                String propertyName = ((Text) nameElement.getFirstChild()).getData();

                Element valueElement = (Element) propertyElement.getLastChild();
                Object value = parseValue(valueElement);
                BeanInfo beanInfo = Introspector.getBeanInfo(cl);
                PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
                boolean done = false;
                for (int j = 0; !done && j < descriptors.length; j++)
                {
                    if (descriptors[j].getName().equals(propertyName))
                    {
                        descriptors[j].getWriteMethod().invoke(obj, value);
                        done = true;
                    }
                }
            }
            return obj;
        }
        catch (Exception ex) // metody refleksji mogą wyrzucać różne wyjątki
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Parsuje element wartości.
     * @param e element wartości
     */
    private Object parseValue(Element e)
    {
        Element child = (Element) e.getFirstChild();
        if (child.getTagName().equals("bean")) return parseBean(child);
        String text = ((Text) child.getFirstChild()).getData();
        if (child.getTagName().equals("int")) return new Integer(text);
        else if (child.getTagName().equals("boolean")) return new Boolean(text);
        else if (child.getTagName().equals("string")) return text;
        else return null;
    }
}
