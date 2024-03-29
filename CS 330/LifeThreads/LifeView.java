
import java.awt.*;
import javax.swing.*;

/** This class provides the GUI for Conway's game of life
as implemented in class LifeModel
 */
public class LifeView extends JPanel implements Runnable {

    private LifeModel theModel;
    private int generationCount;
    private int generationMax;
    private int modelSize;
    private Thread threads;
    private JLabel generationDisplay;
    private JPanel canvas;
    private JLabel sizeDisplay;
    private boolean stop, changes;
    private Color[] colorMap;

    /** Constuct the GUI **/
    public LifeView() {
        modelSize = 128;
        generationMax = 100;

        theModel = new LifeModel(modelSize);

        setLayout(new BorderLayout());
        JPanel displayPanel = new JPanel();
        generationDisplay = new JLabel(" ");
        sizeDisplay = new JLabel("" + modelSize);
        displayPanel.add(new JLabel("generation: "));
        displayPanel.add(generationDisplay);
        displayPanel.add(new JLabel(" size: "));
        displayPanel.add(sizeDisplay);
        setGenerationCount(0);

        canvas = new JPanel() {

            public void paint(Graphics g) {
                Image buffer = createImage(canvas.getSize().width,
                        canvas.getSize().height);
                Graphics gbuffer = buffer.getGraphics();
                paintCanvas(gbuffer);
                g.drawImage(buffer, 0, 0, this);
            }

            public void update(Graphics g) {
                paint(g);
            }
        };

        canvas.setBackground(Color.black);

        add(displayPanel, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);

        colorMap = new Color[10];
        for (int i = 0; i < 10; ++i) {
            float hue = ((float) i) / (float) 10.0;
            colorMap[i] = Color.getHSBColor(hue, (float) 0.9, (float) 0.75);
        }
        start();
    }

    public void start() {
        threads = new Thread(this);
        threads.start();
    }

    /** How many cells for the width/height of the model? */
    public int getModelSize() {
        return modelSize;
    }

    /** Set the number of cells in the width & height of the model. */
    public void setModelSize(int newSize) {
        modelSize = newSize;
        theModel.setSize(modelSize);
        sizeDisplay.setText("" + modelSize);
        setGenerationCount(0);
        canvas.repaint();
    }

    /** Get the number of generations the model will run */
    public int getMaxGenerations() {
        return generationMax;
    }

    /** Set the number of generations the model will run */
    public void setMaxGenerations(int newMax) {
        changes = false;
        generationMax = newMax;
        if (!(generationCount < generationMax)) {
            changes = true;
        }
        setGenerationCount(generationCount);

        if (changes) {
            synchronized (canvas) {
                changes = false;
                canvas.repaint();
                canvas.notifyAll();
            }
        }

    }

    /** Clear the current cells and replace with the indicated
    starting pattern. */
    public void reset(String patternName) {
        stop = true;
        int temp = generationMax;
        setGenerationCount(0);
        setMaxGenerations(0);
        if (patternName.equals("Random")) {
            theModel.reset(0.5);
        } else if (patternName.equals("R-Pentamino")) {
            theModel.reset(1);
        } else if (patternName.equals("Box")) {
            theModel.reset(2);
        } else if (patternName.equals("X Box")) {
            theModel.reset(3);
        }

        canvas.repaint();
//runGenerations(); //* Replace this by appropriate use of a thread


        synchronized (canvas) {
            canvas.notifyAll();
            stop = false;
            canvas.repaint();


        }
        setMaxGenerations(temp);
        setGenerationCount(0);
    }

    /** Repeatedly compute the next Life generation, requesting a
    redisplay each time. */
    public void runGenerations() {
        while (generationCount < generationMax) {
            nextGeneration();
        }
    }

    /** Compute the next generation. */
    public void nextGeneration() {
// System.err.println("Computing generation " + generationCount);
        theModel.nextGeneration();
        setGenerationCount(generationCount + 1);
        canvas.repaint();
    }

    /** Set the current generation number and display it. */
    private void setGenerationCount(int c) {
        generationCount = c;
        generationDisplay.setText(generationCount
                + " / " + generationMax);
    }

    /**
    The main function for displaying the Life game area.
     */
    private void paintCanvas(Graphics g) {
        Dimension canvasSize = canvas.getSize();
        int xdelta = canvasSize.width / modelSize;
        if (xdelta == 0) {
            xdelta = 1;
        }
        int ydelta = canvasSize.height / modelSize;
        if (ydelta == 0) {
            ydelta = 1;
        }

        for (int x = 0; x < modelSize; ++x) {
            for (int y = 0; y < modelSize; ++y) {
                int age = theModel.getCell(x, y);
// System.out.print("("+x+","+y+"): " + age);
                if (generationCount >= 10 && age > 0) {
                    age = 1 + 10 * age / generationCount;
                }
                if (age >= 10) {
                    age = 9;
                }
                Color c = (age > 0) ? colorMap[age - 1] : Color.black;
// System.out.println(" " + age + " color: " + c);
                g.setColor(c);
                g.fillRect(x * xdelta, y * ydelta, xdelta, ydelta);
            }
        }
        Thread.yield();
    }

    public Dimension getPreferredSize() {
        return new Dimension(550, 550);
    }

    public void run() {
        boolean done = false;

        while (!done) {
            synchronized (canvas) {
                while (generationCount < generationMax) {
                    if (stop) {
                        try {
                            canvas.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                    nextGeneration();
                    canvas.repaint();
                    canvas.notifyAll();
                }
            }
        }
    }
}