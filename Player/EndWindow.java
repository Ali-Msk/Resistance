/**
 * [EndWindow.java]
 * this is the window that pops up when the user loses the game 
 * it first asks for the player's name, and shows their scores 
 * if their score is high enough for the top five players(stored in a .txt)
 * they are told 
 * the updated leaderboard is shown
 * @author Ali Meshkat
 */

 

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class EndWindow extends JFrame {
 public static void main(String[] args) {
  new EndWindow(5);
 }

 //for file io
 private PrintWriter output;
 private Scanner input;
 
 //for storing leaderboard
 private int[] scores;
 private String[] names;
 
 private Image image;
 private int waveNum;
 private boolean topFive;//wether made it to to top five or not
 private String name;

 EndWindow(int waveNum) {
  this.setTitle("Resistance - Credits"); // sets title
  this.setVisible(true); // sets visible
  this.setSize(1000, 800); // sets size
  this.setResizable(false);//sets to not resizable
  this.add(new EndWindowPanel());//adds a panel
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//closes program on x press
  
  this.image = Toolkit.getDefaultToolkit().getImage("endWindowImage.jpg");//gets image
  
  this.topFive = false;//intital false
  
  this.waveNum = waveNum;//sets waveNum
  this.scores = new int[5];//initialize arrays
  this.names = new String[5];
  
  //asks for name using a dialog box and stores 
  this.name = JOptionPane.showInputDialog("Please enter your name: ");

  
  
  //reads  the leader board from file and stores 
  try {
   input = new Scanner(new File("highScores.txt"));

   for (int i = 0; i <= 4; i++) {
    String txt = input.nextLine();
    //<score>:<name>
    scores[i] = Integer.parseInt(txt.substring(0, txt.indexOf(":")));
    names[i] = txt.substring(txt.indexOf(":") + 1, txt.length());
   }
   
  } catch (Exception E) {
   System.out.println("error reading from highscores.txt");
   E.printStackTrace();
  }

  
  //checks the score 
  //finds the placement in top 5 if possible
  //updates leaderboard
  if (scores[0] < this.waveNum) {//if better than first place
   //push all back
   //and add score and name to first
   scores[4] = scores[3];
   scores[3] = scores[2];
   scores[2] = scores[1];
   scores[1] = scores[0];
   scores[0] = this.waveNum;

   names[4] = names[3];
   names[3] = names[2];
   names[2] = names[1];
   names[1] = names[0];
   names[0] = this.name;
   this.topFive = true;
  } else if (scores[1] < this.waveNum) {
   scores[4] = scores[3];
   scores[3] = scores[2];
   scores[2] = scores[1];
   scores[1] = this.waveNum;
   names[4] = names[3];
   names[3] = names[2];
   names[2] = names[1];
   names[1] = this.name;

   this.topFive = true;
  } else if (scores[2] < this.waveNum) {
   scores[4] = scores[3];
   scores[3] = scores[2];
   scores[2] = this.waveNum;
   this.topFive = true;

   names[4] = names[3];
   names[3] = names[2];
   names[2] = this.name;

  } else if (scores[3] < this.waveNum) {
   scores[4] = scores[3];
   scores[3] = this.waveNum;
   this.topFive = true;

   names[4] = names[3];
   names[3] = this.name;
  } else if (scores[4] < this.waveNum) {
   scores[4] = this.waveNum;
   this.topFive = true;
   names[4] = this.name;

  }
  input.close();//closes input

  //outputs the new leaderboard to the file
  try {
   output = new PrintWriter(new File("highScores.txt"));
   for (int i = 0; i <= 4; i++) {
    output.println(scores[i] + ":" + names[i]);
   }
   output.close();//closes output
  } catch (FileNotFoundException e) {
   e.printStackTrace();
  }
 }
 
 /**
  * [EndWindowPanel] 
  * this panel draws the picture on the screen with text on top 
  * showing the players score, and the new leaderboard(may or may not be changed depoending on score)
  * @author Ali Meshkat
  * @date Jan 18, 2018
  * @instructor Mr. Mangat
  */
 public class EndWindowPanel extends JPanel {
  Image backgroundImage;

  EndWindowPanel() {
   this.setVisible(true);
  }

  
  /**
   * paint component draws the image, and texts and refreshes screen
   */
  public void paintComponent(Graphics g) {
   super.paintComponent(g);
   
   g.drawImage(image, 0, 0, 1000, 800, null);//draws background

   //sets font and colour
   g.setColor(Color.RED);
   g.setFont(new Font("ShallowGrave BB", Font.PLAIN, 50));

   //draws leader board
   for (int i = 0; i <= 4; i++) {
    g.drawString((i + 1) + " - " + names[i] + ": " + scores[i], 350, 50 + 50 * i);
   }

   g.drawString("You made it to wave: " + waveNum, 350, 650);
   
   if (topFive) {//if in topfive
    g.drawString("You are in top 5 scores!!", 350, 700);
   }
   
   repaint();//refresh
  }
 }
}