/**
 * [ServerCharacter.java]
 * this is the character objects the user will play as
 * it has properties such as damage, health, armour, etc...
 * this is the player side character class which means it includes the picture and 
 * draw method
 */
 

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;

public class PlayerCharacter {
 private int health;//health
 private double x, y, centerX, centerY;//locations
 private double anglePointingTo;//angle towards mouse
 private int maxHealth;//maximum health 
 private double speed;
 private Rectangle rect;//rectangle class  to detect collision 
 private boolean isDead;//if dead or alive

 private static Image rifleImage;//image
 private static int width, length;//dimensions of image

 PlayerCharacter(int x, int y, int health) {
  
  //sets statics
  width = 50;
  length = 50;
  rifleImage = Toolkit.getDefaultToolkit().getImage("rifleImage.png");
  
  
  this.x = x;
  this.y = y;
  this.centerX = this.x + width / 2;
  this.centerY = this.y + length / 2;
  this.health = health;
  this.maxHealth = health;
  this.anglePointingTo = 0;
  this.speed = 0.1;
  this.isDead = false;
  this.rect = new Rectangle((int) this.x, (int) this.y, width, length);//initializes rectangle 
  
  
 }
 
 /**
  * draw 
  * draws the character at the current x and y
  * @return void
  * @param graphics g
  */
 void draw(Graphics g) {
  
  //creates affineTransform object used to rotate images
  AffineTransform affineTransform = AffineTransform.getTranslateInstance((int)this.x, (int)this.y);//creates affinetransform objet and gives location
  affineTransform.rotate(this.anglePointingTo, rifleImage.getWidth(null)/2, rifleImage.getHeight(null)/2);//sets angle
  affineTransform.scale(1, 1);//same size as picture
  Graphics2D graphics2D = (Graphics2D)g;//creates graphics 2d object
  graphics2D.drawImage(rifleImage, affineTransform, null);//draws image 
  
 }

 /**
  * move Accecpts an array of string with size two representing the keys pressed
  * and changes x x and y
  * 
  * @param keys
  */
 public void move(char[] keys, double elapsedTime) {
  if (!this.isDead) {


   // changes the cooridnates based on key pressed
   for (int i = 0; i <= 1; i++) {
    if (keys[i] == 'd') {
     if (this.x + 2 + 100 < 1000) {
      this.x+= this.speed * elapsedTime*1000;
     }
    }
    if (keys[i] == 'a') {
     if (this.x - 2 > 0) {
      this.x-= this.speed * elapsedTime*1000;
     }
    }
    if (keys[i] == 's') {
     if (this.y + 2 + 100 < 800) {
      this.y+= this.speed * elapsedTime*1000;
     }
    }
    if (keys[i] == 'w') {
     if (this.y - 2 > 0) {
      this.y-= this.speed * elapsedTime*1000;
     }
    }
   }
   this.centerX = this.x + width / 2;
   this.centerY = this.y + length / 2;
   this.updateRect();
  }
 }
 /**
  * sets the angle the character is pointing at based on the input mouse x and y
  * 
  * @param mouse
  *            x coordinate
  * @param mouse
  *            y coordinate
  */
 void setAnglePointingTo(int mouseX, int mouseY) {
  if (!this.isDead) {//if alive

   
   //find difference in x and y values
   double xDif = Math.abs(mouseX - (this.x + 50));
   double yDif = Math.abs(mouseY - (this.y + 50));
   double referenceAngle;
   referenceAngle = Math.atan(yDif / xDif);//use differences to ccalculate angle 

   
   //modify angles based on their quadrant
   if (mouseX <= this.centerX && mouseY <= this.centerY) { // if in quadrant 2
    this.anglePointingTo = referenceAngle + Math.toRadians(180);//add 180 degrees(PI rads)
   } else if (mouseX >= this.centerX && mouseY >= this.centerY) {// if in quadrant 4
    this.anglePointingTo = referenceAngle;
   } else if (mouseX <= this.centerX && mouseY >= this.centerY) {// quadrant 3
    this.anglePointingTo = -referenceAngle + Math.toRadians(180);
   } else {// quadrant 1

    this.anglePointingTo = -referenceAngle;
   }

  }
 }
 
 
 
 /**
  * this method is run when the coordinates of the character is changed. It
  * updates the coordinates of its bounding box
  */
 void updateRect() {
  this.rect.setLocation((int) this.x, (int) this.y);
 }

 
 /**
  * mostly auto generated getters and setters for all 
  * variables of playerCharacter
  */

 public double getCenterX() {
  return this.centerX;
 }

 public double getCenterY() {
  return this.centerY;
 }

 public boolean getIsDead() {
  return this.isDead;
 }

 public void setIsDead(boolean health) {
  this.isDead = health;
 }

 public Rectangle getRect() {
  return rect;
 }

 public void setRect(Rectangle rectangle) {
  this.rect = rectangle;
 }
 public int getMaxHealth() {
  return maxHealth;
 }

 public void setMaxHealth(int health) {
  this.maxHealth = health;
 }
 
 public int getHealth() {
  return health;
 }

 public void setHealth(int health) {
  this.health = health;
 }

 public double getX() {
  return x;
 }

 public void setX(double x) {
  this.x = x;
  this.centerX = this.x + width / 2;
 }

 public double getY() {
  return y;
 }

 public void setY(double y) {
  this.y = y;
  this.centerY = this.y + length / 2;
 }

 public double getAnglePoitingTo() {
  return anglePointingTo;
 }

 public void setAnglePoitingTo(double anglePoitingTo) {
  this.anglePointingTo = anglePoitingTo;
 }
}