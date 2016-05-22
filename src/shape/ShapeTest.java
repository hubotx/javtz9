package shape;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

/**
 * Program demonstrujący tworzenie figur za pomocą Java 2D.
 * @version 1.02 2007-08-16
 * @author Cay Horstmann
 */
public class ShapeTest
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                JFrame frame = new ShapeTestFrame();
                frame.setTitle("ShapeTest");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}


/**
 * Ramka zawierająca listę rozwijalną wyboru figury
 * oraz panel, na którym rysowana jest jej reprezentacja.
 */
class ShapeTestFrame extends JFrame
{
    public ShapeTestFrame()
    {
        final ShapeComponent comp = new ShapeComponent();
        add(comp, BorderLayout.CENTER);
        final JComboBox<ShapeMaker> comboBox = new JComboBox<>();
        comboBox.addItem(new LineMaker());
        comboBox.addItem(new RectangleMaker());
        comboBox.addItem(new RoundRectangleMaker());
        comboBox.addItem(new EllipseMaker());
        comboBox.addItem(new ArcMaker());
        comboBox.addItem(new PolygonMaker());
        comboBox.addItem(new QuadCurveMaker());
        comboBox.addItem(new CubicCurveMaker());
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                ShapeMaker shapeMaker = comboBox.getItemAt(comboBox.getSelectedIndex());
                comp.setShapeMaker(shapeMaker);
            }
        });
        add(comboBox, BorderLayout.NORTH);
        comp.setShapeMaker((ShapeMaker) comboBox.getItemAt(0));
        pack();
    }
}

/**
 * Komponent rysujący figurę
 * i umożliwiający przesuwanie definiujących ją punktów.
 */
class ShapeComponent extends JComponent
{
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 200;

    private Point2D[] points;
    private static Random generator = new Random();
    private static int SIZE = 10;
    private int current;
    private ShapeMaker shapeMaker;

    public ShapeComponent()
    {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event)
            {
                Point p = event.getPoint();
                for (int i = 0; i < points.length; i++)
                {
                    double x = points[i].getX() - SIZE / 2;
                    double y = points[i].getY() - SIZE / 2;
                    Rectangle2D r = new Rectangle2D.Double(x, y, SIZE, SIZE);
                    if (r.contains(p))
                    {
                        current = i;
                        return;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent event)
            {
                current = -1;
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent event)
            {
                if (current == -1) return;
                points[current] = event.getPoint();
                repaint();
            }
        });
        current = -1;
    }

    /**
     * Inicjalizuje obiekt ShapeMaker losowo wybranym zbiorem punktów kontrolnych.
     * @param aShapeMaker obiekt klasy ShapeMaker definiujący figurę
     * za pomocą zbioru punktów kontrolnych.
     */
    public void setShapeMaker(ShapeMaker aShapeMaker)
    {
        shapeMaker = aShapeMaker;
        int n = shapeMaker.getPointCount();
        points = new Point2D[n];
        for (int i = 0; i < n; i++)
        {
            double x = generator.nextDouble() * getWidth();
            double y = generator.nextDouble() * getHeight();
            points[i] = new Point2D.Double(x, y);
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        if (points == null) return;
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < points.length; i++)
        {
            double x = points[i].getX() - SIZE / 2;
            double y = points[i].getY() - SIZE / 2;
            g2.fill(new Rectangle2D.Double(x, y, SIZE, SIZE));
        }

        g2.draw(shapeMaker.makeShape(points));
    }

    public Dimension getPrefferedSize() { return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT); }
}

/**
 * Klasa abstrakcyjna reprezentująca figurę
 * za pomocą zbioru punktów kontrolnych.
 * Jej konkretne klasy pochodne muszą implementować
 * metodę makeShape zwracającą dany rodzaj figury.
 */
abstract class ShapeMaker
{
    public abstract Shape makeShape(Point2D[] p);
    private int pointCount;

    /**
     * Tworzy obiekt klasy ShapeMaker.
     * @param aPointCount liczba punktów kontrolnych definiujących figurę.
     */
    public ShapeMaker(int aPointCount)
    {
        pointCount = aPointCount;
    }

    /**
     * Zwraca liczbę punktów kontrolnych definiujących figurę.
     * @return liczba punktów kontrolnych
     */
    public int getPointCount()
    {
        return pointCount;
    }

    /**
     * Tworzy figurę na podstawie zbioru punktów.
     * @param p punkty definiujące figurę
     * @return zdefiniowana figura
     */
    @Override
    public String toString()
    {
        return getClass().getName();
    }
}

/**
 * Tworzy odcinek linii prostej łączącej dwa punkty.
 */
class LineMaker extends ShapeMaker
{
    public LineMaker()
    {
        super(2);
    }

    public Shape makeShape(Point2D[] p)
    {
        return new Line2D.Double(p[0], p[1]);
    }
}

/**
 * Tworzy prostokąt rozpięty za pomocą dwu naróżników.
 */
class RectangleMaker extends ShapeMaker
{
    public RectangleMaker()
    {
        super(2);
    }

    public Shape makeShape(Point2D[] p)
    {
        Rectangle2D s = new Rectangle2D.Double();
        s.setFrameFromDiagonal(p[0], p[1]);
        return s;
    }
}

/**
 * Tworzy zaokrąglony prostokąt rozpięty za pomocą dwu narożników.
 */
class RoundRectangleMaker extends ShapeMaker
{
    public RoundRectangleMaker()
    {
        super(2);
    }

    public Shape makeShape(Point2D[] p)
    {
        RoundRectangle2D s = new RoundRectangle2D.Double(0, 0, 0, 0, 20, 20);
        s.setFrameFromDiagonal(p[0], p[1]);
        return s;
    }
}

/**
 * Tworzy elipsę zawartą w prostokącie ograniczającym
 * rozpiętym za pomocą dwu naróżników.
 */
class EllipseMaker extends ShapeMaker
{
    public EllipseMaker()
    {
        super(2);
    }

    public Shape makeShape(Point2D[] p)
    {
        Ellipse2D s = new Ellipse2D.Double();
        s.setFrameFromDiagonal(p[0], p[1]);
        return s;
    }
}

/**
 * Tworzy łuk zawart w prostokącie ograniczającym
 * rozpięty za pomocą dwu narożników. Dodatkowe dwa punkty
 * kontrolne umożliwiają określenie wartości początkowej i końcowej
 * kąta łuku. Aby zilustrować poprawnośc obliczeń kątów łuku, metoda makeShape
 * zwraca figurę złożoną z łuku, prostokąta ograniczającego i linii
 * łączących środek łuku z punktami kontrolnymi kąta.
 */
class ArcMaker extends ShapeMaker
{
    public ArcMaker()
    {
        super(4);
    }

    @Override
    public Shape makeShape(Point2D[] p)
    {
        double centerX = (p[0].getX() + p[1].getX()) / 2;
        double centerY = (p[0].getY() + p[1].getY()) / 2;
        double width = Math.abs(p[1].getX() - p[0].getX());
        double height = Math.abs(p[1].getY() - p[0].getY());

        double skewedStartAngle = Math.toDegrees(Math.atan2(-(p[2].getY() - centerY) * width, (p[2].getX() - centerX) * height));
        double skewedEndAngle = Math.toDegrees(Math.atan2(-(p[3].getY() - centerY) * width, (p[3].getX() - centerX) * height));
        double skewedAngleDifference = skewedEndAngle - skewedStartAngle;
        if (skewedStartAngle < 0) skewedStartAngle += 360;
        if (skewedAngleDifference < 0) skewedAngleDifference += 360;

        Arc2D s = new Arc2D.Double(0, 0, 0, 0, skewedStartAngle, skewedAngleDifference, Arc2D.OPEN);
        s.setFrameFromDiagonal(p[0], p[1]);

        GeneralPath g = new GeneralPath();
        g.append(s, false);
        Rectangle2D r = new Rectangle2D.Double();
        r.setFrameFromDiagonal(p[0], p[1]);
        g.append(r, false);
        Point2D center = new Point2D.Double(centerX, centerY);
        g.append(new Line2D.Double(center, p[2]), false);
        g.append(new Line2D.Double(center, p[3]), false);
        return g;
    }
}

/**
 * Tworzy wielokąt o sześciu wierzchołkach.
 */
class PolygonMaker extends ShapeMaker
{
    public PolygonMaker()
    {
        super(6);
    }

    @Override
    public Shape makeShape(Point2D[] p)
    {
        GeneralPath s = new GeneralPath();
        s.moveTo((float) p[0].getX(), (float) p[0].getY());
        for (int i = 1; i < p.length; i++)
            s.lineTo((float) p[i].getX(), (float) p[i].getY());
        s.closePath();
        return s;
    }
}

/**
 * Tworzy krzywą drugiego stopnia zdefiniowaną przez jej końce
 * i pojedynczy punkt kontrolny.
 */
class QuadCurveMaker extends ShapeMaker
{
    public QuadCurveMaker()
    {
        super(3);
    }

    @Override
    public Shape makeShape(Point2D[] p)
    {
        return new QuadCurve2D.Double(p[0].getX(), p[0].getY(), p[1].getX(), p[1].getY(),
                                      p[2].getX(), p[2].getY());
    }
}

/**
 * Tworzy krzywą trzeciego stopnia zdefiniowaną przez jej końce
 * i dwa punkty kontrolne.
 */
class CubicCurveMaker extends ShapeMaker
{
    public CubicCurveMaker()
    {
        super(4);
    }

    @Override
    public Shape makeShape(Point2D[] p)
    {
        return new CubicCurve2D.Double(p[0].getX(), p[0].getY(), p[1].getX(), p[1].getY(),
                                       p[2].getX(), p[2].getY(), p[3].getX(), p[3].getY());
    }
}