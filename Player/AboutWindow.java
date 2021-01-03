/**
 * [AboutWindow.java]
 * this is the window that pops up when the credits button 
 * is selected in the main menu, it has the author's name
 * @author Ali Meshkat
 */
 

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AboutWindow extends JFrame {
 
 
 private Image image;
 AboutWindow(){
  this.setTitle("Resistance - Credits"); // sets title
  this.setVisible(true); // sets visible
  this.setSize(1000, 800); // sets size
  this.setResizable(false);
  this.add(new AboutWindowPanel());
  this.image =Toolkit.getDefaultToolkit().getImage("aboutWindowImage.jpg"); //gets image 
 }

 
 
 /**
  * [AboutWindowPanel]
  * this panel draws the background image to the screen 
  * @author Ali Meshkat
  * @date Jan 18, 2018
  * @instructor Mr. Mangat
  */
 public class AboutWindowPanel extends JPanel {
  Image backgroundImage;

  AboutWindowPanel() {
   //sets visible
   this.setVisible(true);
  }

  public void paintComponent(Graphics g) {
   super.paintComponent(g);
   g.drawImage(image, 0, 0, 1000, 800, null);//draws image
   repaint();
  }
 }
}
