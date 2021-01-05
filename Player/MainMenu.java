/**
 * [MainMenu.java]
 * this is the first screen the user will after opening the game 
 * it will have options to start game, see the tutorial or credits page
 * @author Ali Meshkat
 */
 

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainMenu extends JFrame {

 public static void main(String[] args) {
  new MainMenu();
 }

 Rectangle playButton;// rectangles for each button
 Rectangle tutorialButton;
 Rectangle aboutButton;

 MainMenuPanel gamePanel;
 MainMenu thisFrame;

 private String headerTitle;

 MainMenu() {
  headerTitle = "Resistance";// header

  this.setTitle(headerTitle); // sets title
  this.setVisible(true); // sets visible
  this.setSize(1000, 800); // sets size
  this.setResizable(false);// not resizable
  gamePanel = new MainMenuPanel();
  this.add(gamePanel);// adds the game panel
  thisFrame = this;
  gamePanel.setVisible(true); // sets panel to visible
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allows it to be closed if x pressed(end program)
  this.addMouseListener(new ButtonMouseListener()); // adds click listener

  // sets cooeidnates for the rectangles
  playButton = new Rectangle(440, 440, 105, 65);
  tutorialButton = new Rectangle(472, 536, 80, 28);
  aboutButton = new Rectangle(14, 749, 80, 28);
 }

 /**
  * [MainMenuPanel] this panel draws the pictures and text on the screen and has
  * a mouse listener to operate when buttons are pressed
  * 
  * @author Ali Meshkat
  * @date Jan 18, 2018
  * @instructor Mr. Mangat
  */
 public class MainMenuPanel extends JPanel {

  Image backgroundImage;

  MainMenuPanel() {
   try {
    backgroundImage = Toolkit.getDefaultToolkit().getImage("mainMenuBackground.jpg");// imports image

   } catch (Exception E) {
    System.out.println("cannot find images");
    E.printStackTrace();
   }
  }

  public void paintComponent(Graphics g) {
   super.paintComponent(g);

   //draws image
   g.drawImage(backgroundImage, 0, 0, 1000, 800, this);

   
   //draws the fonts in different locations with different sizes
   g.setFont(new Font("ShallowGrave BB", Font.PLAIN, 150));
   g.drawString("Resistance", 50, 350);

   g.setFont(new Font("ShallowGrave BB", Font.PLAIN, 80));
   g.drawString("Play", 450, 500);

   g.setFont(new Font("ShallowGrave BB", Font.PLAIN, 35));
   g.drawString("Tutorial", 470, 535);

   g.setFont(new Font("ShallowGrave BB", Font.PLAIN, 35));
   g.drawString("Credits", 15, 750);

   repaint();
  }
 }

 
 /**
  * [ButtonMouseListener]
  * this class implements mouse listener and waits for click events 
  * on buttons to perform their actions
  * @author Ali Meshkat
  * @date Jan 18, 2018
  * @instructor Mr. Mangat
  */
 private class ButtonMouseListener implements MouseListener {
  
  public void mouseClicked(MouseEvent e) {
   System.out.println(e.getX());
   System.out.println(e.getY());

   //if mouse is within boudary of rectangle
   if (e.getX() >= playButton.getX() && e.getX() <= (playButton.getX() + playButton.getWidth()) && e.getY() >= playButton.getY() && e.getY() <= (playButton.getY() + playButton.getHeight())) { // if clicked on button
    new PlayerConnector();
   } else if (e.getX() >= tutorialButton.getX() && e.getX() <= (tutorialButton.getX() + tutorialButton.getWidth()) && e.getY() >= tutorialButton.getY() && e.getY() <= (tutorialButton.getY() + tutorialButton.getHeight())) {
    new TutorialWindow();
   } else if (e.getX() > aboutButton.getX() && e.getX() < aboutButton.getX() + aboutButton.getWidth() && e.getY() > aboutButton.getY() && e.getY() < aboutButton.getY() + aboutButton.getHeight()) {
    new AboutWindow();

   }
  }

  public void mousePressed(MouseEvent e) {
  }

  public void mouseReleased(MouseEvent e) {
  }

  public void mouseEntered(MouseEvent e) {
  }

  public void mouseExited(MouseEvent e) {
  }
 }
}
