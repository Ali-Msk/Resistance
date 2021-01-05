

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class TestMainGame {
 private Socket player1, player2; // user sockets
 private PrintWriter output1, output2;
 
 private boolean gameOn;

 private ArrayList<ServerEnemy> enemies;
 private ArrayList<ServerBullet> bullets;

 private ServerCharacter character1, character2;
 private char[] player1KeyPresses, player2KeyPresses;// stores the keys they
 
 private int screenWidth, screenHeight;
 private int killReward;
 private int waveReward;
 private int initialMoney;
 private int mouseX1, mouseY1, mouseX2, mouseY2;// 1 and 2 are playerNums


 private Clock clock;

 private String extraMsg1, extraMsg2;

 // waves
 private double timeSinceLastWave;
 private int waveNum;
 TestMainGame(Socket player1, Socket player2) {
  this.player1 = player1;
  this.player2 = player2;
  
  
  //initial values 
  this.killReward = 10;// money rewarded per kill
  this.waveReward = 20;// money rewarded per wave
  this.initialMoney = 100;
  this.extraMsg1 = "";
  this.extraMsg2 = "";
  this.screenHeight = 800;
  this.screenWidth = 1000;
  this.waveNum = 0;

  this.clock = new Clock();
  this.clock.update();
  
  
  this.bullets = new ArrayList<ServerBullet>();
  this.enemies = new ArrayList<ServerEnemy>();
  
  
  
  //initializes printWriters
  try {
   output1 = new PrintWriter(this.player1.getOutputStream());
   output2 = new PrintWriter(this.player2.getOutputStream());
  }catch(Exception e) {
   System.out.println("output streams cannot be created");
   e.printStackTrace();
  }
  
  
  startGame();
 
 }

 /**
  * startGame is first called at the beginning of class 
  * and involves a loop that  runs the game
  * @param none
  * @return void
  */
 void startGame() {
  this.player1KeyPresses = new char[2];
  this.player2KeyPresses = new char[2];

  this.gameOn = true;

  
  //creates the characters
  character1 = new ServerCharacter(200, 200, this.initialMoney, this.clock);
  character2 = new ServerCharacter(400, 400, this.initialMoney, this.clock);

  getMessage(); // initiates two threads to read messages from users


  while (this.gameOn) {
   
   try {
    Thread.sleep(10);//delays the server sending msg's to the player to every 10 ms
   }catch(Exception e) {
    e.printStackTrace();
    System.out.println("thread delay error");
   }

    makeMsg();

  }

 }

 /**
  * creates the message that will contain the game information being sent to the
  * server
  * 
  * @param none
  * @return void
  */
 synchronized void makeMsg() {
  String msg1 = "";
  String msg2 = "";

  
  //character locations
  msg1 += "character1:" + this.character1.getX() + ":" + this.character1.getY() + ":"
    + this.character1.getAnglePoiningTo() + ":" + this.character1.getHealth() + ":" + this.character1.getMoney() + ":&";
  msg1 += "character2:" + this.character2.getX() + ":" + this.character2.getY() + ":"
    + this.character2.getAnglePoiningTo() + ":" + this.character2.getHealth() + ":";
  msg2 += "character1:" + this.character2.getX() + ":" + this.character2.getY() + ":"
    + this.character2.getAnglePoiningTo() + ":" + this.character2.getHealth() + ":" + this.character2.getMoney() + ":&";
  msg2 += "character2:" + this.character1.getX() + ":" + this.character1.getY() + ":"
    + this.character1.getAnglePoiningTo() + ":" + this.character1.getHealth() + ":";

  //adds enemies to msg
  try {
   // sends enemy info to the user
   if (enemies.size() > 0 /*&& enemiesChanged*/) {
    msg1 += "&"; // & shows character type of element is done and theres more info
    msg2 += "&";
    for (int i = 0; i <= enemies.size() - 1; i++) {
     msg1 += "enemy:" + enemies.get(i).getX() + ":" + enemies.get(i).getY() + ":"
       + enemies.get(i).getAnglePointingTo() + ":+";
     msg2 += "enemy:" + enemies.get(i).getX() + ":" + enemies.get(i).getY() + ":"
       + enemies.get(i).getAnglePointingTo() + ":+";
    }
    msg1 = msg1.substring(0, msg1.length() - 1);// removes last plus sign
    msg2 = msg2.substring(0, msg2.length() - 1);// removes last plus sign
   }

  } catch (Exception e) {
      //the error is caused because of the two threads running at the same 
      //time and changing the same values which make these error happen 
      //for example as the server is trying to detect collisions between an enemy and bullet 
      //the bullet or the enemy could die right while the server is in the loop above and gets a 
      //_null pointer_ or _indeoutofbounds size 0_ exceptions
      //the errors do not affect the game play
   e.printStackTrace();
  }

  
  if (enemies.size() == 0) {
    extraMsg1 += "allDead&";
    extraMsg2 += "allDead&";
  }

  
  //sends all bullet coordinates
  try {
   if (bullets.size() > 0 ) {
    msg1 += "&"; // & shows enemy type of element is done
    msg2 += "&";
    for (int i = 0; i <= bullets.size() - 1; i++) {
     msg1 += "bullet:" + bullets.get(i).getX() + ":" + bullets.get(i).getY() + ":"
       + bullets.get(i).getDirection() + ":+";
     msg2 += "bullet:" + bullets.get(i).getX() + ":" + bullets.get(i).getY() + ":"
       + bullets.get(i).getDirection() + ":+";
    }
    msg1 = msg1.substring(0, msg1.length() - 1);// removes lat plus sign
    msg2 = msg2.substring(0, msg2.length() - 1);// removes last plus sign

   }

  } catch (Exception e) {
      //the error is caused because of the two threads running at the same 
      //time and changing the same values which make these error happen 
      //for example as the server is trying to detect collisions between an enemy and bullet 
      //the bullet or the enemy could die right while the server is in the loop above and gets a 
      //_null pointer_ or _indeoutofbounds size 0_ exceptions
      //the errors do not affect the game play
   e.printStackTrace();
  }

  msg1 += "&";
  msg2 += "&";

  // adds the extra conditions if they exist
  //these are conditions that dont always occur eg new wave
  if (!extraMsg1.equals("")) {

   msg1 += extraMsg1;
   msg2 += extraMsg2;

  }

  extraMsg1 = "";
  extraMsg2 = "";

  // shows the end of message
  msg1 += ")";
  msg2 += ")";

  // sends the messages
  sendMessage(0, msg1);
  sendMessage(1, msg2);
 }

 /**
  * updateInfo runs all calculations and method 
  * calls related to the game, it also checks for deaths 
  * collisions etc
  * @return void 
  * @param none
  */
 public synchronized void updateInfo() {
  this.clock.update();

  // updating character locations

  try {
   this.character1.move(this.player1KeyPresses, clock.getElapsedTime());
   // resests
   this.player1KeyPresses[0] = 'z';
   this.player1KeyPresses[1] = 'z';
  } catch (Exception e) {
   e.printStackTrace();
  }
  try {
   this.character2.move(this.player2KeyPresses, clock.getElapsedTime());
   this.player2KeyPresses[0] = 'z';
   this.player2KeyPresses[1] = 'z';
  } catch (Exception e) {
   e.printStackTrace();
  }

  // moves enemies
  if (enemies.size() > 0) {
   for (int i = 0; i <= enemies.size() - 1; i++) {
    enemies.get(i).move(character1, character2, clock);
   }
  }

  // updates the angle the characters are pointing to
  character1.setAnglePointingTo(mouseX1, mouseY1);
  character2.setAnglePointingTo(mouseX2, mouseY2);

  // moves the bullets
  if (bullets.size() > 0) {
   for (int i = 0; i <= bullets.size() - 1; i++) {
    bullets.get(i).move(this.clock.getElapsedTime());
   }
  }

  try {
   // checks for collision between bullets and enemies
   // and kills dead enemies
   if (bullets.size() > 0 && enemies.size() > 0) {
    for (int i = 0; i <= bullets.size() - 1; i++) {
     for (int j = 0; j <= enemies.size() - 1; j++) {
      if (bullets.get(i).getRect().intersects(enemies.get(j).getRect())) {
       bullets.get(i).hit(enemies.get(j));// runs the hit method for bullet

       // checks for kill
       if (enemies.get(j).getHealth() < 1) { // removes
        enemies.remove(j);
        bullets.get(i).getShooter().setMoney(bullets.get(i).getShooter().getMoney() + this.killReward);
       }

       bullets.remove(i);// remove bullet

      }
     }
    }
   }
  } catch (Exception e) {
      //the error is caused because of the two threads running at the same 
      //time and changing the same values which make these error happen 
      //for example as the server is trying to detect collisions between an enemy and bullet 
      //the bullet or the enemy could die right while the server is in the loop above and gets a 
      //_null pointer_ or _indeoutofbounds size 0_ exceptions
      //the errors do not affect the game play
   e.printStackTrace();

  }
  
  // checks for collision between enemies and characters
  if (enemies.size() > 0) {
   for (int i = 0; i <= enemies.size() - 1; i++) {
    if (enemies.get(i).getRect().intersects(character1.getRect())) {
     enemies.get(i).attack(character1);
     if (character1.getHealth() <= 0) {
      character1.setIsDead(true);
     }
    }
    if (enemies.get(i).getRect().intersects(character2.getRect())) {
     enemies.get(i).attack(character2);
     if (character2.getHealth() <= 0) {
      character2.setIsDead(true);
     }
    }

   }
  }

  try {
   // removes bullets if they are out of bounds
   if (bullets.size() > 0) {
    for (int i = 0; i <= bullets.size() - 1; i++) {
     if (bullets.get(i).getX() > screenWidth || bullets.get(i).getX() < 0 || bullets.get(i).getY() > screenHeight || bullets.get(i).getY() < 0) {
      bullets.remove(i);// removes bullet from list to reduce info being sent => smoother gamePlay
     }
    }
   }
  }catch(Exception e) {
      //the error is caused because of the two threads running at the same 
      //time and changing the same values which make these error happen 
      //for example as the server is trying to detect collisions between an enemy and bullet 
      //the bullet or the enemy could die right while the server is in the loop above and gets a 
      //_null pointer_ or _indeoutofbounds size 0_ exceptions
      //the errors do not affect the game play
   e.printStackTrace();
  }

  // makes waves if time
  double waveWaitTime = 20000;//every 20 seconds
  if (System.currentTimeMillis() - this.timeSinceLastWave > waveWaitTime) {
   this.timeSinceLastWave = System.currentTimeMillis();
   this.waveNum++;
   
   //gives money to characters
   character1.setMoney(character1.getMoney() + waveReward);
   character2.setMoney(character2.getMoney() + waveReward);

   //adds to extra msg's to tell the client
   this.extraMsg1 += "newWave&";
   this.extraMsg2 += "newWave&";

   Random random = new Random();
   for (int i = 0; i <= (1 + this.waveNum * 3); i++) {
    int side = random.nextInt(4);

    // rendomly adds enemies to different locaitons just outside the screen
    // where not in view of player
    if (side == 0) {
     enemies.add(new ServerEnemy(random.nextInt(1000) + 1, -50));

    } else if (side == 1) {
     enemies.add(new ServerEnemy(random.nextInt(1000) + 1, 800));

    } else if (side == 2) {
     enemies.add(new ServerEnemy(-50, random.nextInt(800) + 1));

    } else {
     enemies.add(new ServerEnemy(1000, random.nextInt(800) + 1));

    }
    
   }

  }

 }

 
 /**
  * decode gets the message recieved and the playerNum and decodes the message, and sets
  * the required values
  * 
  * @param playerNum
  *            0 or 1
  * @param msg
  *            directly from player
  */
 void decode(int playerNum, String msg) {
  boolean hasNext = true;
  if (playerNum == 0) {
   while (hasNext) {

    if (msg.startsWith("keyPressed:")) {
     // keyPressed:<keys>
     msg = msg.substring(11, msg.length());// removes// header  
     player1KeyPresses[0] = msg.charAt(0);
     player1KeyPresses[1] = msg.charAt(1);
     msg = msg.substring(msg.indexOf("&") + 1, msg.length());

    } else if (msg.startsWith("mousePos:")) {
     // mousePos:<int x>:<int y>
     msg = msg.substring(9, msg.length());// removes header
     mouseX1 = Integer.parseInt(msg.substring(0, msg.indexOf(":")));// gets x
     msg = msg.substring(msg.indexOf(":") + 1, msg.length());// remvoes x
     mouseY1 = Integer.parseInt(msg.substring(0, msg.indexOf(":")));// gets y
     msg = msg.substring(msg.indexOf("&"), msg.length());// removes mouse coord

     if (msg.length() > 2) {// if not end of msg
      msg = msg.substring(1, msg.length());// removes &
     } else {// if end of msg
      hasNext = false;
     }

    } else if (msg.startsWith("mouseClicked")) {
     if (!character1.getIsDead()) {

      ServerBullet bullet = character1.shoot();

      if (!(bullet == null)) {// null is returned if waiting for attack speed
       bullets.add(bullet);
      }

     }

     msg = msg.substring(msg.indexOf("&"), msg.length());// removes all of mousecliked
     if (msg.length() > 2) {// if not end of msg
      msg = msg.substring(1, msg.length());// removes &
     } else {// if end of message
      hasNext = false;
     }
     
    } else if (msg.startsWith("upgradeWeapon")) {

     character1.upgradeWeapon();

     msg = msg.substring(msg.indexOf("&"), msg.length());
     if (msg.length() > 2) {// if not end of msg
      msg = msg.substring(1, msg.length());// removes &
     } else {// if end of message
      hasNext = false;
     }
     
    } else if (msg.startsWith("upgradeArmour")) {

     character1.upgradeArmour();
     System.out.println("armour upgraded");

     // tells clients about the change in health
     extraMsg1 += "maxHealth:1:" + character1.getMaxHealth() + ":&";
     extraMsg2 += "maxHealth:2:" + character1.getMaxHealth() + ":&";

     msg = msg.substring(msg.indexOf("&"), msg.length());
     if (msg.length() > 2) {// if not end of msg
      msg = msg.substring(1, msg.length());// removes &
     } else {// if end of message
      hasNext = false;
     }
     
    } else if (msg.startsWith("upgradeMovement")) {

     character1.upgradeMoveSpeed();

     msg = msg.substring(msg.indexOf("&"), msg.length());
     if (msg.length() > 2) {// if not end of msg
      msg = msg.substring(1, msg.length());// removes &
     } else {// if end of message
      hasNext = false;
     }
    }
   }
  } else {
   while (hasNext) {

    if (msg.startsWith("keyPressed:")) {
     // keyPressed:<keys>
     msg = msg.substring(11, msg.length());// removes// header  
     player2KeyPresses[0] = msg.charAt(0);
     player2KeyPresses[1] = msg.charAt(1);
     msg = msg.substring(msg.indexOf("&") + 1, msg.length());

    } else if (msg.startsWith("mousePos:")) {
     // mousePos:<int x>:<int y>
     msg = msg.substring(9, msg.length());// removes header
     mouseX2 = Integer.parseInt(msg.substring(0, msg.indexOf(":")));// gets x
     msg = msg.substring(msg.indexOf(":") + 1, msg.length());// remvoes x
     mouseY2 = Integer.parseInt(msg.substring(0, msg.indexOf(":")));// gets y
     msg = msg.substring(msg.indexOf("&"), msg.length());// removes mouse coord

     if (msg.length() > 2) {// if not end of msg
      msg = msg.substring(1, msg.length());// removes &
     } else {// if end of msg
      hasNext = false;
     }

    } else if (msg.startsWith("mouseClicked")) {
     if (!character2.getIsDead()) {

      ServerBullet bullet = character2.shoot();

      if (!(bullet == null)) {
       bullets.add(bullet);
      }

     }
     msg = msg.substring(msg.indexOf("&"), msg.length());// removes all of mousecliked
     if (msg.length() > 2) {// if not end of msg
      msg = msg.substring(1, msg.length());// removes &
     } else {// if end of message
      hasNext = false;
     }

    } else if (msg.startsWith("upgradeWeapon")) {

     character2.upgradeWeapon();
     msg = msg.substring(msg.indexOf("&"), msg.length());
     if (msg.length() > 2) {// if not end of msg
      msg = msg.substring(1, msg.length());// removes &
     } else {// if end of message
      hasNext = false;
     }
    } else if (msg.startsWith("upgradeArmour")) {

     character2.upgradeArmour();
     System.out.println("armour upgraded");

     // tells clients about the change in health
     extraMsg1 += "maxHealth:2:" + character2.getMaxHealth() + ":&";
     extraMsg2 += "maxHealth:1:" + character2.getMaxHealth() + ":&";

     msg = msg.substring(msg.indexOf("&"), msg.length());
     if (msg.length() > 2) {// if not end of msg
      msg = msg.substring(1, msg.length());// removes &
     } else {// if end of message
      hasNext = false;
     }
    } else if (msg.startsWith("upgradeMovement")) {

     character2.upgradeMoveSpeed();

     msg = msg.substring(msg.indexOf("&"), msg.length());
     if (msg.length() > 2) {// if not end of msg
      msg = msg.substring(1, msg.length());// removes &
     } else {// if end of message
      hasNext = false;
     }
    }

   }
  }
  updateInfo();

 }

 
 
 /**
  * [ReadFromServer] reads the messages sent from the users and runs the
  * approporiate methods or changes values
  * 
  * @author Ali Meshkat
  * 
  */
 class ReadFromUser1 implements Runnable {
  private BufferedReader input; // Stream for network input
  private boolean running;

  ReadFromUser1() {
   try {
    InputStreamReader stream = new InputStreamReader(player1.getInputStream()); // input steam to be used in
                       // buffered reader
    this.input = new BufferedReader(stream); // buffered reader for
               // input
   } catch (Exception e) {
    e.printStackTrace();
   }
   this.running = true; // sets running to true (used to determine for
         // how long the it will try to read)

  }

  public void run() {
   String msg = "";
   while (this.running) { // loop unit a message is received
    try {
     if (this.input.ready()) { // check for an incoming messge
      msg = this.input.readLine();
      decode(0, msg);
     }
    } catch (IOException e) {
     System.out.println("Failed to receive msg from the client");
     e.printStackTrace();
    }
   }
  }
 }

 
 class ReadFromUser2 implements Runnable {
  private BufferedReader input; // Stream for network input
  private boolean running;

  ReadFromUser2() {
   try {
    InputStreamReader stream = new InputStreamReader(player2.getInputStream()); // input steam to be used in
                       // buffered reader
    this.input = new BufferedReader(stream); // buffered reader for
               // input
   } catch (Exception e) {
    e.printStackTrace();
   }
   this.running = true; // sets running to true (used to determine for
         // how long the it will try to read)

  }

  public void run() {
   String msg = "";
   while (this.running) { // loop unit a message is received
    try {
     if (this.input.ready()) { // check for an incoming messge
      msg = this.input.readLine();
      decode(1, msg);
     }
    } catch (IOException e) {
     System.out.println("Failed to receive msg from the client");
     e.printStackTrace();
    }
    msg = "";

   }
  }
 }

 /**
  * getMessage creates the threads to read from user
  * 
  * @param none
  * @return a string representing the message
  * @author: Ali Meshkat
  */
 public void getMessage() {
  System.out.println("running readFromServer");
  Thread t = new Thread(new ReadFromUser2()); // create a thread for
             // player2
  Thread d = new Thread(new ReadFromUser1()); // create a thread for
             // player1

  t.start(); // starts the new threads
  d.start();
 }

 /**
  * this method accepts a string message and sends to the selected user 0 means
  * first player and 1 is second
  * 
  * @return void
  * @param msg
  * @author Ali Meshkat
  */
 public void sendMessage(int userNum, String msg) {
  try {

   if (userNum == 0) {
    this.output1.println(msg);
    this.output1.flush();
   } else {
    this.output2.println(msg);
    this.output2.flush();
   }

  } catch (Exception e) {
   e.printStackTrace();

   System.out.println("sendMessage method errorrrrrr");

  }
 }
}
