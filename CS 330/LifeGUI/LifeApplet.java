import javax.swing.JApplet;


/**
 *  An applet that launches the spreadsheet as a separate window.
 *
 */
public class LifeApplet extends JApplet
{

    Life window;

    public void init()
    {
        window = new Life(true);
        window.setTitle("CS330 Asst - Life GUI");
        window.pack();
    }

    public void start()
    {    
        window.setVisible(true);
    }

    public void stop()
    {
        window.setVisible(false);
    }

    public void destroy()
    {
    }
}
