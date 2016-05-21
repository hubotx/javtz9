package editorPane;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Ramka zawierająca panel edytora, pole tekstowe i przycisk Load
 * umożliwiające wprowadzenie adresu URL i załadowanie strony,
 * a także przycisk Back umożliwiający powrót do poprzedniej strony.
 */
public class EditorPaneFrame extends JFrame
{
    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 400;

    public EditorPaneFrame()
    {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        final Stack<String> urlStack = new Stack<>();
        final JEditorPane editorPane = new JEditorPane();
        final JTextField url = new JTextField(30);

        // instaluje obiekt nasłuchujący hiperłącza

        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent event)
            {
                if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                {
                    try
                    {
                        // zapamiętuje adres URL dla potrzeb przycisku Back
                        urlStack.push(event.getURL().toString());
                        // prezentuje adres URL w polu tekstowym
                        url.setText(event.getURL().toString());
                        editorPane.setPage(event.getURL());
                    }
                    catch (IOException e)
                    {
                        editorPane.setText("Exception: " + e);
                    }
                }
            }
        });

        // konfiguruje pole wyboru umożliwiające włączenie trybu edycji

        final JCheckBox editable = new JCheckBox();
        editable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                editorPane.setEditable(editable.isSelected());
            }
        });

        // tworzy obiekt nasłuchujący przycisku Load

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                try
                {
                    // zapamiętuje adres URL dla potrzeb przycisku Back
                    urlStack.push(url.getText());
                    editorPane.setPage(url.getText());
                }
                catch (IOException e)
                {
                    editorPane.setText("Exception: " + e);
                }
            }
        };

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(listener);
        url.addActionListener(listener);

        // Konfiguruje przycisk Back i związaną z nim akcję

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                if (urlStack.size() <= 1) return;
                try
                {
                    // zapamiętuje adres URL dla potrzeb przycisku Back
                    urlStack.pop();
                    // prezentuje adres URL w polu tekstowym
                    String urlString = urlStack.peek();
                    url.setText(urlString);
                    editorPane.setPage(urlString);
                }
                catch (IOException e)
                {
                    editorPane.setText("Exception: " + e);
                }
            }
        });

        add(new JScrollPane(editorPane), BorderLayout.CENTER);

        // umieszcza wszystkie komponenty na głównym panelu okna

        JPanel panel = new JPanel();
        panel.add(new JLabel("URL"));
        panel.add(url);
        panel.add(loadButton);
        panel.add(backButton);
        panel.add(new JLabel("Editable"));
        panel.add(editable);

        add(panel, BorderLayout.SOUTH);
    }
}
