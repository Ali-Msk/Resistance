/**
 * [PlayerDisplayer.java]
 * this class gets all the user interaction 
 * from mouse and keyboard, sends the info to the server
 * where the calculations will be processed and based on 
 * the messages from the server displays the game to the user
 * @author Ali Meshkat
 */


import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class PlayerDisplayer extends JFrame {
 private Socket socket;

 private int playerNum;
 private char[] keysPressed;// two possible keys the user has pressed
 private GamePanel gamePanel;
 private PrintWriter output;
 private boolean mouseClicked;

 private PlayerClock clock;
 private long lastTimeSent;
 private String extraMsg;
 private Rectangle weaponUpgrade, armourUpgrade, moveUpgrade;
 
 PlayerDisplayer(Socket socket, int playerNum) {
  

  this.weaponUpgrade = new Rectangle(300, 50, 100, 50);
  this.armourUpgrade = new Rectangle(500, 50, 100, 50);
  this.moveUpgrade = new Rectangle(700, 50, 100, 50);

  this.mouseClicked = false;
  this.clock = new PlayerClock();
  this.clock.update();
  this.extraMsg = "";
  this.lastTimeSent = System.currentTimeMillis();
  this.socket = socket;
  try {
   output = new PrintWriter(this.socket.getOutputStream());
  } catch (Exception E) {
   System.out.println("Error creating output stream in player displayer constructor");
   E.printStackTrace();
  }

  // %%Graphics
  this.setTitle("Resistance");// sets title
  this.setSize(1000, 800);// sets size
  this.setResizable(false);
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allows it to be closed if x pressed
  this.setLayout(new BorderLayout()); // sets border layout
  this.addKeyListener(new keyListener()); // adds keylistener
  this.gamePanel = new GamePanel(this.socket, this.playerNum, this.clock);// inits gamePanel
  this.add(this.gamePanel, BorderLayout.CENTER); // sets gamePanel to centre
  this.addMouseListener(new GameMouseEvent());

  
  this.playerNum = playerNum;
  this.keysPressed = new char[2];
  this.keysPressed[0] = 'z';
  this.keysPressed[1] = 'z';// Z means no keys have been presssed

  this.setVisible(true);// sets visible
  boolean gameOn = true;
  while (gameOn) {
   makeMsg();
   
   //if both characters dead then end screen
   if(gamePanel.getCharacters()[0].getIsDead() && gamePanel.getCharacters()[1].getIsDead() ) {
    new EndWindow(gamePanel.getWaveNum());    
    this.dispose();
    gameOn = false;
   
   }
  }
 }

 /**
  * makeMsg creates the block of msg that 
  * will be sent to the server 
  * does it every 10 milliseconds
  * @param none
  * @return void
  */
 void makeMsg() {
  String msg = "";
  if (System.currentTimeMillis() - lastTimeSent > 10) {
   lastTimeSent = System.currentTimeMillis();

   
   //sends info to gamePanel
   this.gamePanel.setKeysPressed(this.keysPressed);
   this.gamePanel.setMouseX(((int) MouseInfo.getPointerInfo().getLocation().x - getX()));
   this.gamePanel.setMouseY(((int) MouseInfo.getPointerInfo().getLocation().y - getY()));

   
   //adds keypresses
   msg += "keyPressed:" + this.keysPressed[0] + this.keysPressed[1] + "&";
   
   //adds mmousepos
   msg += "mousePos:" + ((int) MouseInfo.getPointerInfo().getLocation().x - getX()) +
     ":" + ((int) MouseInfo.getPointerInfo().getLocation().y - getY()) + ":&";

   //adds click
   if (this.mouseClicked) {
    msg += "mouseClicked&";
   }
   this.mouseClicked = false;

   if(!extraMsg.equals("")) {
    msg += extraMsg;//adds messages that are not sent always ex. upgrades
   }
   extraMsg = "";
   
   
   msg += ")";// shows end of message
   
   sendMessage(msg);
   
   
   
  }
 }

 /**
  * [GameMouseEvent]
  * waits for mouse events and adds them to extra msg to be sent to the server
  */
 public class GameMouseEvent implements MouseListener {

  public void mouseClicked(MouseEvent e) {
   mouseClicked = true;

   //checks for clicks  on buttons
   if (e.getX() >= weaponUpgrade.getX() && e.getX() <= (weaponUpgrade.getX() + weaponUpgrade.getWidth()) &&
     e.getY() >= weaponUpgrade.getY() && e.getY() <= (weaponUpgrade.getY() + weaponUpgrade.getHeight())) {
    extraMsg += "upgradeWeapon&";
   }
   if (e.getX() >= armourUpgrade.getX() && e.getX() <= (armourUpgrade.getX() + armourUpgrade.getWidth()) &&
     e.getY() >= armourUpgrade.getY() && e.getY() <= (armourUpgrade.getY() + armourUpgrade.getHeight())) {
    extraMsg += "upgradeArmour&";
   }
   if (e.getX() >= moveUpgrade.getX() && e.getX() <= (moveUpgrade.getX() + moveUpgrade.getWidth()) &&
     e.getY() >= moveUpgrade.getY() && e.getY() <= (moveUpgrade.getY() + moveUpgrade.getHeight())) {
    extraMsg += "upgradeMovement&";
   }
   
  }

  public void mouseEntered(MouseEvent e) {
   
  }

  public void mouseExited(MouseEvent e) {

  }

  public void mousePressed(MouseEvent e) {
   mouseClicked = true;
  }

  public void mouseReleased(MouseEvent e) {

  }

 }

 /**
  * waits for key events and sets the keyspressed char array to the keys being
  * pressed so they can be sent to the
  */
 public class keyListener implements KeyListener {

  public keyListener() {
  }

  public void keyPressed(KeyEvent e) {
   if (e.getKeyChar() == 'w') {
    if (keysPressed[0] != 'w' && keysPressed[1] != 'w') {// if not already selected
     if (keysPressed[0] == 'z') {// if first is empty
      keysPressed[0] = 'w';// set to first
     } else if (keysPressed[1] == 'z') {// if second slot is empty
      keysPressed[1] = 'w';// set to second slot
     } else {
      keysPressed[1] = keysPressed[0];
      keysPressed[0] = 'w';
     }
    }
   }

   if (e.getKeyChar() == 's') {
    if (keysPressed[0] != 's' && keysPressed[1] != 's') {
     if (keysPressed[0] == 'z') {
      keysPressed[0] = 's';
     } else if (keysPressed[1] == 'z') {
      keysPressed[1] = 's';
     } else {
      keysPressed[1] = keysPressed[0];

      keysPressed[0] = 's';
     }
    }
   }

   if (e.getKeyChar() == 'a') {
    if (keysPressed[0] != 'a' && keysPressed[1] != 'a') {
     if (keysPressed[0] == 'z') {
      keysPressed[0] = 'a';
     } else if (keysPressed[1] == 'z') {
      keysPressed[1] = 'a';
     } else {
      keysPressed[1] = keysPressed[0];

      keysPressed[0] = 'a';
     }
    }
   }

   if (e.getKeyChar() == 'd') {
    if (keysPressed[0] != 'd' && keysPressed[1] != 'd') {
     if (keysPressed[0] == 'z') {
      keysPressed[0] = 'd';
     } else if (keysPressed[1] == 'z') {
      keysPressed[1] = 'd';
     } else {
      keysPressed[1] = keysPressed[0];

      keysPressed[0] = 'd';
     }
    }
   }
  }

  public void keyReleased(KeyEvent e) {
   if (e.getKeyChar() == 'w') {
    if (keysPressed[0] == 'w') {
     keysPressed[0] = 'z';
    } else if(keysPressed[1] == 'w'){
     keysPressed[1] = 'z';
    }
   }

   if (e.getKeyChar() == 's') {
    if (keysPressed[0] == 's') {
     keysPressed[0] = 'z';
    } else if(keysPressed[1] == 's'){
     keysPressed[1] = 'z';
    }
   }

   if (e.getKeyChar() == 'a') {
    if (keysPressed[0] == 'a') {
     keysPressed[0] = 'z';
    } else if(keysPressed[1] == 'a'){
     keysPressed[1] = 'z';
    }
   }

   if (e.getKeyChar() == 'd') {
    if (keysPressed[0] == 'd') {
     keysPressed[0] = 'z';
    } else if(keysPressed[1] == 'd') {
     keysPressed[1] = 'z';
    }
   }
  }

  public void keyTyped(KeyEvent e) {

  }
 }



 /**
  * this method accepts a string message and sends to the server using the class
  * socket
  * @return void
  * @param msg
  * @author Ali Meshkat
  */
 public void sendMessage(String msg) {
  try {

   output.println(msg);
   output.flush();

  } catch (Exception E) {
   System.out.println("sendMessage method errorrrrrr");
   E.printStackTrace();
  }
 }
}