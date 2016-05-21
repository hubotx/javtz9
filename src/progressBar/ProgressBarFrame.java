package progressBar;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

/**
 * Ramka zawierająca przycisk uruchamiający
 * symulację czasochłonnej operacji oraz pasek postępu
 * i pole tekstowe.
 */
public class ProgressBarFrame extends JFrame
{
    public static final int TEXT_ROWS = 10;
    public static final int TEXT_COLUMNS = 40;

    private JButton startButton;
    private JProgressBar progressBar;
    private JCheckBox checkBox;
    private JTextArea textArea;
    private SimulatedActivity activity;

    public ProgressBarFrame()
    {
        // pole tekstowe, w którym prezentowane jest działanie wątku

        textArea = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);

        // panel zawierający przycisk i pasek postępu

        final int MAX = 1000;
        JPanel panel = new JPanel();
        startButton = new JButton("Start");
        progressBar = new JProgressBar(0, MAX);
        progressBar.setStringPainted(true);
        panel.add(startButton);
        panel.add(progressBar);

        checkBox = new JCheckBox("indeterminate");
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                progressBar.setIndeterminate(checkBox.isSelected());
                progressBar.setStringPainted(!progressBar.isIndeterminate());
            }
        });
        panel.add(checkBox);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        // dodaje obiekt nasłuchujący przycisku

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                startButton.setEnabled(false);
                activity = new SimulatedActivity(MAX);
                activity.execute();
            }
        });
        pack();
    }

    class SimulatedActivity extends SwingWorker<Void, Integer>
    {
        private int current;
        private int target;

        /**
         * Tworzy wątek symulowanej operacji. Zwiększa on wartość licznika
         * do momentu osiągnięcia wartości docelowej.
         * @param t wartość docelowa licznika.
         */
        public SimulatedActivity(int t)
        {
            current = 0;
            target = t;
        }

        protected Void doInBackground() throws Exception
        {
            try
            {
                while (current < target)
                {
                    Thread.sleep(100);
                    current++;
                    publish(current);
                }
            }
            catch (InterruptedException e)
            {

            }
            return null;
        }

        protected void process(List<Integer> chunks)
        {
            for (Integer chunk : chunks)
            {
                textArea.append(chunk + "\n");
                progressBar.setValue(chunk);
            }
        }

        protected void done()
        {
            startButton.setEnabled(true);
        }
    }
}
