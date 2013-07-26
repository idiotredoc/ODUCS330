import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Life extends JFrame
{

  private LifeView viewer;
  private Choice initialPic;
  private boolean inApplet;
  
  public Life(boolean inAnApplet)
  {
      inApplet = inAnApplet;

      initialPic = new Choice ();
      initialPic.add("R-Pentamino");
      initialPic.add("Box");
      initialPic.add("X Box");
      initialPic.add("Random");
      initialPic.addItemListener (new ItemListener() {
	      public void itemStateChanged (ItemEvent e)
	      {
		  if (e.getStateChange() == ItemEvent.SELECTED) {
		      String value = initialPic.getSelectedItem();
		      viewer.reset (value);
		  }
	      }});
      
      
      JButton reset = new JButton("reset");
      reset.addActionListener (new ActionListener() {
	      public void actionPerformed (ActionEvent e) {
		  String value = initialPic.getSelectedItem();
		  viewer.setMaxGenerations(0);
		  viewer.reset (value);
	      }
	  } );
      
      JButton nextGen = new JButton("+");
      nextGen.addActionListener (new ActionListener() {
	      public void actionPerformed (ActionEvent e) {
		  viewer.setMaxGenerations(viewer.getMaxGenerations() + 1);
	      }
	  } );
      
      JButton nextGen10 = new JButton("+10");
      nextGen10.addActionListener (new ActionListener() {
	      public void actionPerformed (ActionEvent e) {
		  viewer.setMaxGenerations(viewer.getMaxGenerations() + 10);
	      }
	  } );
      
      JButton nextGen100 = new JButton("+100");
      nextGen100.addActionListener (new ActionListener() {
	      public void actionPerformed (ActionEvent e) {
		  viewer.setMaxGenerations(viewer.getMaxGenerations() + 100);
	      }
	  } );

      JButton nextGen1000 = new JButton("+1000");
      nextGen1000.addActionListener (new ActionListener() {
	      public void actionPerformed (ActionEvent e) {
		  viewer.setMaxGenerations(viewer.getMaxGenerations() + 1000);
	      }
	  } );
      
      JButton zoomIn = new JButton("in");
      zoomIn.addActionListener (new ActionListener() {
	      public void actionPerformed (ActionEvent e) {
		  int size = viewer.getModelSize();
		  if (size > 1)
		      size = size / 2;
		  viewer.setModelSize(size);
		  String value = initialPic.getSelectedItem();
		  viewer.reset (value);
	      }
	  } );
      
      JButton zoomOut = new JButton("out");
      zoomOut.addActionListener (new ActionListener() {
	      public void actionPerformed (ActionEvent e) {
		  int size = viewer.getModelSize();
		  viewer.setModelSize(2*size);
		  String value = initialPic.getSelectedItem();
		  viewer.reset (value);
	      }
	  } );

      
      
      viewer = new LifeView();
      getContentPane().add (viewer, BorderLayout.CENTER);
      
      addWindowListener
	  (new WindowAdapter() {
		  public void windowClosing(WindowEvent e) {
		      if (inApplet)
			  hide();
		      else
			  System.exit(0);
		  }
	      });
  
  
      JPanel upperButtons = new JPanel();
      
  upperButtons.add(initialPic);
  upperButtons.add(reset);
      
   getContentPane().add (upperButtons,BorderLayout.NORTH);
      
      JPanel lowerButtons = new JPanel();
    
  lowerButtons.add(nextGen);
  lowerButtons.add(nextGen10);
  lowerButtons.add(nextGen100);
  lowerButtons.add(nextGen1000);
  
 JLabel lowerButtons2 = new JLabel (" Zoom: ");
 
  lowerButtons.add(lowerButtons2);
  lowerButtons.add(zoomIn);
  lowerButtons.add(zoomOut);
   
  getContentPane().add (lowerButtons,BorderLayout.SOUTH);
  
  }
 
    public static void main(String args[]) {
	Life window = new Life(false);
	window.setTitle("CS330 Asst - Life GUI");
	window.pack();
        window.setVisible(true);
    }

}
