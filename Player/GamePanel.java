/**
 * [GamePanel.java]
 * this is the main game panel where the game will take place
 * displays enemies and characters at the appropriate locations
 * @author Ali Meshkat
 */


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

 private Socket socket;
 private PlayerCharacter[] characters;
 
 private int wealth;
 private char[] keysPressed;// two possible keys the user has pressed
 private int playerNum;
 private double mouseX, mouseY;
 private int waveNum;

 private ArrayList<PlayerEnemy> enemies;
 private ArrayList<PlayerBullet> bullets;
 private PlayerClock clock;
 private Rectangle weaponUpgrade, armourUpgrade, moveSpeedUpgrade;
 private Image weaponUpgradeImage, armourUpgradeImage, moveSpeedUpgradeImage;
 private Image backgroundImage, woodBackgroundImage;
 private PlayerClock myClock;
 private FrameRate myFrameRate;
 private Image characterOneMarker, characterTwoMarker;
 GamePanel(Socket socket, int playerNum, PlayerClock clock) {
  
  this.weaponUpgrade = new Rectangle(300, 0, 120, 70);
  this.armourUpgrade = new Rectangle(500, 0, 100, 70);
  this.moveSpeedUpgrade = new Rectangle(700, 0, 100, 70);
  
  this.weaponUpgradeImage = Toolkit.getDefaultToolkit().getImage("weaponUpgradeImage.png");
  this.armourUpgradeImage = Toolkit.getDefaultToolkit().getImage("armourUpgradeImage.png");
  this.moveSpeedUpgradeImage = Toolkit.getDefaultToolkit().getImage("moveSpeedUpgradeImage.png");
  this.backgroundImage = Toolkit.getDefaultToolkit().getImage("mainBackground.jpg");
  this.characterOneMarker= Toolkit.getDefaultToolkit().getImage("characterOneMarker.png");
  this.characterTwoMarker= Toolkit.getDefaultToolkit().getImage("characterTwoMarker.png");
  this.woodBackgroundImage = Toolkit.getDefaultToolkit().getImage("woodBackground.jpg");
  
  
  this.myClock = new PlayerClock();//
  this.myFrameRate = new FrameRate();//
  this.clock = new PlayerClock();
  this.enemies = new ArrayList<PlayerEnemy>();
  this.bullets = new ArrayList<PlayerBullet>();
  this.clock.update();
  this.socket = socket;
  this.playerNum = playerNum;
  this.waveNum = 0;
  
  
  // characterNum 1 starts game at 200,200
  // while 2 starts at 400 400
  // however is characters[] index 0 is always this class
  characters = new PlayerCharacter[2];
  if (this.playerNum == 1) {//
   characters[0] = new PlayerCharacter(200, 200, 100);
   characters[1] = new PlayerCharacter(400, 400, 100);
  } else {
   characters[1] = new PlayerCharacter(200, 200, 100);
   characters[0] = new PlayerCharacter(400, 400, 100);
  }

  getMessage();
  this.setVisible(true);
 }

 /**
  * gameLoop happens in here draws the game based on server messages
  */
 public void paintComponent(Graphics g) {
  super.paintComponent(g);
  g.drawImage(backgroundImage, 0, 0, null);

  
  clock.update();

  // updates info
  characters[0].move(keysPressed, this.clock.getElapsedTime());
  characters[0].setAnglePointingTo((int)mouseX, (int)mouseY);
  
  if(this.characters[0].getHealth() <= 0) {
   this.characters[0].setIsDead(true);
  }
  if(this.characters[1].getHealth() <= 0) {
   this.characters[1].setIsDead(true);
  }
  
  //moves enemies
  try {
   if (enemies.size() > 0) {
    for (int i = 0; i <= enemies.size() - 1; i++) {
     enemies.get(i).move(characters[0], characters[1], this.clock);
    }
   }
  } catch (Exception E) {
   //the stacktrace printing is commented out due the error happening too 
   //often and cause the program to slow down to print to console
   //the error is caused because of the two threads running at the same 
   //time and changing the same values which make these error happen 
   //the errors do not affect the game play
   //E.printStackTrace();
  }
  
  try {// moves the bullets
   if (bullets.size() > 0) {
    for (int i = 0; i <= bullets.size() - 1; i++) {
     bullets.get(i).move(this.clock.getElapsedTime());
    }
   }
  } catch (Exception E) {
   //the stacktrace printing is commented out due the error happening too 
   //often and cause the program to slow down to print to console
   //the error is caused because of the two threads running at the same 
   //time and changing the same values which make these error happen 
   //the errors do not affect the game play
   //E.printStackTrace();
  }

  try {
   // checks for collision between bullets and enemies
   // and kills dead enemies
   if (bullets.size() > 0 && enemies.size() > 0) {
    for (int i = 0; i <= bullets.size() - 1; i++) {
     for (int j = 0; j <= enemies.size() - 1; j++) {
      if (bullets.get(i).getRect().intersects(enemies.get(j).getRect())) {

       bullets.remove(i);// remove bullet

      }
     }
    }
   }
  } catch (Exception E) {
   //the stacktrace printing is commented out due the error happening too 
   //often and cause the program to slow down to print to console
   //the error is caused because of the two threads running at the same 
   //time and changing the same values which make these error happen 
   //the errors do not affect the game play
  // E.printStackTrace();
  }

  // tells characters to draw themselves
  if (characters[0].getHealth() > 0) {
   characters[0].draw(g);
   g.drawImage(characterOneMarker, (int)characters[0].getCenterX()-10, (int)characters[0].getCenterY()-30, 35, 35, null);
  }
  if (characters[1].getHealth() > 0) {
   characters[1].draw(g);
   g.drawImage(characterTwoMarker,(int) characters[1].getCenterX()-10, (int)characters[1].getCenterY()-30, 35, 35, null);
  }

  try {
   // draws enemies
   if (this.enemies.size() > 0) {
    for (int i = 0; i <= enemies.size() - 1; i++) {
     enemies.get(i).draw(g);
    }
   }
  } catch (Exception E) {

  }

  try {

   // draws enemies
   if (this.bullets.size() > 0) {
    for (int i = 0; i <= bullets.size() - 1; i++) {
     bullets.get(i).draw(g);
    }
   }
  } catch (Exception E) {

  }
  
  g.drawImage(woodBackgroundImage, 0, 0, null);//draws background

  // draws upgrade buttons and text below them   
  g.setFont(new Font("ShallowGrave BB", Font.PLAIN, 20));
  g.drawImage(weaponUpgradeImage, (int) weaponUpgrade.getX(), (int) weaponUpgrade.getY(), (int) weaponUpgrade.getWidth(), (int) weaponUpgrade.getHeight(), null);
  g.drawString("Upgrade Weapon - $100", (int) weaponUpgrade.getX() , (int) (weaponUpgrade.getY() + (int) weaponUpgrade.getHeight()) + 15);
  
  g.drawImage(armourUpgradeImage, (int) armourUpgrade.getX(), (int) armourUpgrade.getY(), (int) armourUpgrade.getWidth(), (int) armourUpgrade.getHeight(), null);
  g.drawString("Upgrade Armour - $100", (int) armourUpgrade.getX() , (int) (armourUpgrade.getY() + (int) armourUpgrade.getHeight())+ 15);
 
  g.drawImage(moveSpeedUpgradeImage, (int) moveSpeedUpgrade.getX(), (int) moveSpeedUpgrade.getY(), (int) moveSpeedUpgrade.getWidth(), (int) moveSpeedUpgrade.getHeight(), null);
  g.drawString("Upgrade Movement - $100", (int) moveSpeedUpgrade.getX() , (int) (moveSpeedUpgrade.getY() + (int) moveSpeedUpgrade.getHeight())+ 15);

  g.drawLine(0, 90, 1000, 90);// menu line
  g.drawLine(200, 0, 200, 90);// separator line

  g.setFont(new Font("ShallowGrave BB", Font.PLAIN, 30));

  // draws health bars in the bottom of the screen
  // this character
  g.setColor(Color.RED);
  g.drawRect(350, 700, 250, 25);
  g.fillRect(350, 700, 250 * characters[0].getHealth() / characters[0].getMaxHealth(), 25);
  g.drawImage(characterOneMarker,300, 685, 40, 40, null);
  // teammate
  g.drawRect(25, 725, 100, 5);
  g.fillRect(25, 725, 100 * characters[1].getHealth() / characters[1].getMaxHealth(), 5);
  g.drawImage(characterTwoMarker,5, 710, 20, 20, null);

  // drawing stats
  g.setColor(Color.BLACK);
  g.drawString("Wealth: " + this.wealth, 5, 30);
  g.drawString("Wave: " + this.waveNum, 5, 65);

  myClock.update(); // updates clock
  myFrameRate.update(); // updates frame rate
  myFrameRate.draw(g, 940, 30);// draws fps
  
  
  try {
   Thread.sleep(20);
  }catch(Exception e) {
   e.printStackTrace();
  }
  
  repaint();
  
 }

 /**
  * decodes the messages from server and runs the appropriate methods
  * @param msg recieved from server
  * @return void
  */
 public void decode(String msg) {
  boolean hasNext = true;

  while (hasNext) {

   if (msg.startsWith("character1")) {

    msg = msg.substring(msg.indexOf(":") + 1, msg.length());/// removes header
    double newX1 = Double.parseDouble(msg.substring(0, msg.indexOf(":")));// gets x
    msg = msg.substring(msg.indexOf(":") + 1, msg.length());// removes x
    double newY1 = Double.parseDouble(msg.substring(0, msg.indexOf(":")));// gets Y
    msg = msg.substring(msg.indexOf(":") + 1, msg.length());// removes y
    double newAngleFacing = Double.parseDouble(msg.substring(0, msg.indexOf(":")));// gets angle(rad)
    msg = msg.substring(msg.indexOf(":") + 1, msg.length()); // removes angle h
    int newHealth = Integer.parseInt(msg.substring(0, msg.indexOf(":")));
    msg = msg.substring(msg.indexOf(":") + 1, msg.length()); // removes health
    int newMoney = Integer.parseInt(msg.substring(0, msg.indexOf(":")));// gets money
    msg = msg.substring(msg.indexOf(":") + 1, msg.length()); // removes money

    characters[0].setX(newX1);
    characters[0].setY(newY1);
    characters[0].setAnglePoitingTo(newAngleFacing);
    characters[0].setHealth(newHealth);
    this.wealth = newMoney;
 

    msg = msg.substring(msg.indexOf("&") + 1, msg.length()); // removes all of character1

   } else if (msg.startsWith("character2")) {
    msg = msg.substring(msg.indexOf(":") + 1, msg.length());/// removes header
    double newX2 = Double.parseDouble(msg.substring(0, msg.indexOf(":")));// gets x
    msg = msg.substring(msg.indexOf(":") + 1, msg.length());// removes x
    double newY2 = Double.parseDouble(msg.substring(0, msg.indexOf(":")));// gets y
    msg = msg.substring(msg.indexOf(":") + 1, msg.length());// removes y

    double newAngleFacing = Double.parseDouble(msg.substring(0, msg.indexOf(":")));// gets angle(rad)
    msg = msg.substring(msg.indexOf(":") + 1, msg.length()); // removes angle
    int newHealth = Integer.parseInt(msg.substring(0, msg.indexOf(":")));
    msg = msg.substring(msg.indexOf(":") + 1, msg.length()); // removes health
 
     characters[1].setX(newX2);
     characters[1].setY(newY2);
     characters[1].setAnglePoitingTo(newAngleFacing);
     characters[1].setHealth(newHealth);

    if (msg.startsWith("&)")) {// if end of message
     hasNext = false;
    } else {
     msg = msg.substring(msg.indexOf("&") + 1, msg.length()); // removes all of character2
    }
   } else if (msg.startsWith("enemy:")) {
    enemies.clear();
    boolean enemyHasNext = true;

    while (enemyHasNext) {
     msg = msg.substring(msg.indexOf(":") + 1, msg.length());// removes header
     double x = Double.parseDouble(msg.substring(0, msg.indexOf(":")));
     msg = msg.substring(msg.indexOf(":") + 1, msg.length());// removes x
     double y = Double.parseDouble(msg.substring(0, msg.indexOf(":")));
     msg = msg.substring(msg.indexOf(":") + 1, msg.length());// removes y
     double anglePointingTo = Double.parseDouble(msg.substring(0, msg.indexOf(":")));
     msg = msg.substring(msg.indexOf(":") + 1, msg.length());// removes angle
     enemies.add(new PlayerEnemy(x, y, anglePointingTo));// adds tthe enemy

     if (msg.substring(0, 1).equals("+")) {
      enemyHasNext = true;
      msg = msg.substring(1, msg.length());// removes +

     } else {
      enemyHasNext = false;
     }

    }

    if (msg.startsWith("&)")) {// if end of message
     hasNext = false;
    } else {
     msg = msg.substring(1, msg.length()); // removes all of character2(&)
    }

   } else if (msg.startsWith("bullet:")) {
    bullets.clear();
    try {
     boolean bulletHasNext = true;

     while (bulletHasNext) {
      msg = msg.substring(msg.indexOf(":") + 1, msg.length());// removes header
      double x = Double.parseDouble(msg.substring(0, msg.indexOf(":")));
      msg = msg.substring(msg.indexOf(":") + 1, msg.length());// removes x
      double y = Double.parseDouble(msg.substring(0, msg.indexOf(":")));
      msg = msg.substring(msg.indexOf(":") + 1, msg.length());// removes y
      double anglePointingTo = Double.parseDouble(msg.substring(0, msg.indexOf(":")));
      msg = msg.substring(msg.indexOf(":") + 1, msg.length());// removes angle
      bullets.add(new PlayerBullet(x, y, anglePointingTo));// adds the bullet

      if (msg.substring(0, 1).equals("+")) {
       bulletHasNext = true;
       msg = msg.substring(1, msg.length());// removes +

      } else {
       bulletHasNext = false;
      }

     }

     if (msg.startsWith("&)")) {// if end of message
      hasNext = false;
     } else {
      msg = msg.substring(1, msg.length()); // removes all of character2
     }
    } catch (Exception E) {
     E.printStackTrace();
     msg = msg.substring(msg.indexOf("&") + 1, msg.length());
     if (msg.startsWith(")")) {// if end of message
      hasNext = false;
     }
    }
   } else if (msg.startsWith("maxHealth")) {
    /// maxHealth:<playerNum>:<maxHealth>:
  
    msg = msg.substring(msg.indexOf(":") + 1, msg.length());// removes header
    int playerNum = Integer.parseInt(msg.substring(0, 1));// gets playerNum
    msg = msg.substring(2, msg.length());// remoevs playerNum and :
    int newHealth = Integer.parseInt(msg.substring(0, msg.indexOf(":")));// gets health
    msg = msg.substring(msg.indexOf(":") + 1, msg.length());// remoevs health

    // assigns health
    if (playerNum == 1) {
     characters[0].setMaxHealth(newHealth);
    } else {
     characters[1].setMaxHealth(newHealth);
    }

    if (msg.startsWith("&)")) {// if end of message
     hasNext = false;
    } else {
     msg = msg.substring(1, msg.length()); // removes &
    }
   } else if (msg.startsWith("newWave")) {

    this.waveNum++;// adds to waveNum
    msg = msg.substring(msg.indexOf("&"), msg.length());// removes from msg

    if (msg.startsWith("&)")) {// if end of message
     hasNext = false;
    } else {
     msg = msg.substring(1, msg.length()); // removes &
    }
   }else if(msg.startsWith("allDead")) {
    enemies.clear();
    
    msg = msg.substring(msg.indexOf("&") + 1, msg.length());
    
    if(msg.startsWith(")")) {
     hasNext = false;
    }
   }
  }
 }

 /**
  * [ReadFromServer] this class is used to read the 
  * msg's from the server
  * 
  * @author Ali Meshkat
  *
  */
 class ReadFromServer implements Runnable {
  private BufferedReader input; // Stream for network input
  private boolean running;

  ReadFromServer() {
   try {
    InputStreamReader stream = new InputStreamReader(socket.getInputStream()); // input steam to be used in
    // buffered reader
    this.input = new BufferedReader(stream); // buffered reader for input
   } catch (Exception E) {
    E.printStackTrace();
   }
   running = true; // sets running to true (used to determine for how long the it will try to read)

  }

  public void run() {
   String msg = "";
   while (running) { // loop unit a message is received
    try {
     if (input.ready()) { // check for an incoming messge
      msg = input.readLine();

      decode(msg);// ruuuuuuuuuuuuuuuuuuuuns decode

     } 

    } catch (IOException e) {
     System.out.println("Failed to receive msg from the client");
     e.printStackTrace();
    }
   }
  }

 }

 /**
  * this method runs the readFromServer thread server and returns the value
  * 
  * @param none
  * @return a string representing the message
  * @author: Ali Meshkat
  */
 public void getMessage() {
  System.out.println("running readFromServer");
  Thread t = new Thread(new ReadFromServer()); // create a thread for

  t.start(); // start the new thread

 }

 // setters used by PLayerDisplayer
 void setKeysPressed(char[] keys) {
  this.keysPressed = keys;
 }
 void setMouseX(double x) {
  this.mouseX = x;
 }
 void setMouseY (double y) {
  this.mouseY = y;
 }

 PlayerCharacter[] getCharacters() {
  return this.characters;
 }
 int getWaveNum() {
  return this.waveNum;
 }
}
