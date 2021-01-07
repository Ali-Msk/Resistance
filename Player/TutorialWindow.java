/**
 * [TutorialWindow.java]
 * this is the window that pops up when the Tutorial button 
 * is selected in the main menu 
 * it tells the user what the goal in this game is 
 * and what keys are used to play the game
 * @author Ali Meshkat
 */

 

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TutorialWindow extends JFrame {

 private Image image;

 TutorialWindow() {
  this.setTitle("Resistance - Credits"); // sets title
  this.setVisible(true); // sets visible
  this.setSize(1000, 800); // sets size
  this.setResizable(false); // makes the window have a fixed size
  this.add(new AboutWindowPanel()); // adds the panel
  this.image = Toolkit.getDefaultToolkit().getImage("tutorialWindowImage.jpg"); // imports image
 }

 /**
  * [AboutWindowPanel] this panel draws the picture on the screen and is added to
  * tutorualWindow frame
  * 
  * @author Ali
  *
  */
 public class AboutWindowPanel extends JPanel {
  Image backgroundImage;

  AboutWindowPanel() {
   this.setVisible(true);// makes visible
  }

  /**
   * paint component draws the image and refreshes screen
   */
  public void paintComponent(Graphics g) {
   super.paintComponent(g);
   g.drawImage(image, 0, 0, 1000, 800, null); // draws iamge
   repaint();
  }
 }
}
