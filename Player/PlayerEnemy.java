/**
 * [Enemy.java]
 * Enemy class that is spawned at the beginning of each wave and 
 * moves towards the closest character, and attacks them once close enough
 * @author Ali Meshkat
 */
 

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;




public class PlayerEnemy {
 private double x, y;
 private double anglePointingTo;
 private static Image image;
 private double speed;
 private static int width, length;
 private Rectangle rect;

 PlayerEnemy(double x, double y, double anglePointingTo) {
  this.x = x;
  this.y = y;
  this.anglePointingTo = anglePointingTo;
  image = Toolkit.getDefaultToolkit().getImage("enemy.png");
  this.speed = 0.033;
  width = 50;
  length = 50;
  this.rect = new Rectangle((int) this.x, (int) this.y, width, length);
 }

 /**a
  * draw 
  * draws the enemy at the current x and y
  * and at the direction it is pointing to 
  * @return void
  * @param graphics
  *            g
  */
 void draw(Graphics g) {
  
  //creates affineTransform object used to rotate images
  AffineTransform affineTransform = AffineTransform.getTranslateInstance((int)this.x, (int)this.y);
  affineTransform.rotate(this.anglePointingTo);//sets angle
  affineTransform.scale(0.5, 0.5);
  Graphics2D graphics2D = (Graphics2D)g;//creates graphics 2d object
  graphics2D.drawImage(image, affineTransform, null);//draws image 
  
  
 }
 

 void move(PlayerCharacter char1, PlayerCharacter char2, PlayerClock clock) {
  PlayerCharacter closerChar; // stores the closer Character

  if (!char1.getIsDead() && !char2.getIsDead()) {// if both alive

   double dTo1, dTo2;// disatnce from 1 and 2

   // finds the distances using pythagorean
   dTo1 = Math.sqrt(Math.abs(Math.pow((char1.getX() + 50) - this.x, 2) + Math.pow((char1.getY() + 50) - this.y, 2)));
   dTo2 = Math.sqrt(Math.abs(Math.pow((char2.getX() + 50) - this.x, 2) + Math.pow((char2.getY() + 50) - this.y, 2)));

   // sets the closer one
   if (dTo1 < dTo2) {
    closerChar = char1;
   } else {
    closerChar = char2;
   }
  } else if (char1.getIsDead()) {// if 1 is dead
   closerChar = char2;
  } else if (char2.getIsDead()) {// if 2 is dead
   closerChar = char1;
  } else {
   closerChar = null;
  }
  
  boolean readyToMove = true;//if true the enemy is safe to move 
  
  if(closerChar == null) {
   readyToMove = false;
  }else {
   if(!this.rect.intersects(closerChar.getRect())) {
    readyToMove = true;
   }
  }
  
  if (readyToMove) {// if safe to move

   if (this.x + 25 < (closerChar.getX() + 50) && (closerChar.getX() + 50) - (this.x + 25) > 10) {

    this.x += this.speed * clock.getElapsedTime() * 1000;// * elapsedTime*100;
   }

   if (this.y + 25 < (closerChar.getY() + 50) && (closerChar.getY() + 50) - (this.y + 25) > 10) {
    this.y += this.speed * clock.getElapsedTime() * 1000;// * elapsedTime*100;
   }

   if (this.x + 25 > (closerChar.getX() + 50) && (closerChar.getX() + 50) - (this.x + 25) < 10) {
    this.x -= this.speed * clock.getElapsedTime() * 1000;// * elapsedTime*100;
   }

   if (this.y + 25 > (closerChar.getY() + 50) && (closerChar.getY() + 50) - (this.y + 25) < 10) {
    this.y -= this.speed * clock.getElapsedTime() * 1000;// * elapsedTime*100;
   }

   this.setAnglePointingTo(closerChar); // runs the
   this.updateRect();
  }
 }
 
 
 /**
  * sets the angle the enemy is pointing at based on the character moving towards
  * 
  * @param character
  */
 void setAnglePointingTo(PlayerCharacter character) {

  // this calculate from the centre and calculate the right angel/
  double xDif = Math.abs((character.getX() + 50) - (this.x + 50));
  double yDif = Math.abs((character.getY() + 50) - (this.y + 50));
  double referenceAngle;

  if ((character.getX() + 50) <= this.x && (character.getY() + 50) <= this.y) { // if in quadrant 2
   referenceAngle = Math.atan(yDif / xDif);
   this.anglePointingTo = referenceAngle + Math.toRadians(180);
  } else if ((character.getX() + 50) >= this.x && (character.getY() + 50) >= this.y) {// if in quadrant 4
   referenceAngle = Math.atan(yDif / xDif);

   this.anglePointingTo = referenceAngle;
  } else if ((character.getX() + 50) <= this.x && (character.getY() + 50) >= this.y) {// quadrant 3
   referenceAngle = Math.atan(yDif / xDif);
   this.anglePointingTo = -referenceAngle + Math.toRadians(180);
  } else {// quadrant 1
   referenceAngle = Math.atan(yDif / xDif);
   this.anglePointingTo = -referenceAngle;
  }


 }
 
 
 
 public Rectangle getRect() {
  return rect;
 }
 void updateRect() {
  this.rect.setLocation((int) this.x, (int) this.y);
 }
}
