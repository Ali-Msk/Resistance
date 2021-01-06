/**
 * [Enemy.java]
 * Enemy class that is spawned at the beginning of each wave and 
 * moves towards the closest character, and attacks them once close enough
 * @author Ali Meshkat
 */


import java.awt.Rectangle;

public class ServerEnemy {

 private int health;

 private int damage;
 private int moveSpeed;
 private Rectangle rect;
 private double x, y;
 private double anglePointingTo;
 private double attackSpeed;
 private double lastTimeAttacked;
 private double speed;

 private static int width, length;

 ServerEnemy(double x, double y) {
  width = 50;
  length = 50;

  this.speed = 0.033;
  this.x = x;
  this.y = y;
  this.anglePointingTo = 0;
  this.rect = new Rectangle((int) this.x, (int) this.y, width, length);
  this.health = 20;
  this.damage = 20;
  this.lastTimeAttacked = 10000;// so it will attack right away the first time
  this.attackSpeed = 1000;// time in milliseconds
 }

 /**
  * move takes in the two characters and the clock class, finds the closer
  * character and updates the enemies location towards that character
  * 
  * @param char1
  * @param char2
  * @param clock
  */
 void move(ServerCharacter char1, ServerCharacter char2, Clock clock) {
  ServerCharacter closerChar; // stores the closer Character

  if (!char1.getIsDead() && !char2.getIsDead()) {// if both alive

   double dTo1, dTo2;// disatnce from 1 and 2

   // finds the distances using pythagorean
   dTo1 = Math.sqrt(Math.abs(Math.pow((char1.getX() + length) - this.x, 2) + Math.pow((char1.getY() + width) - this.y, 2)));
   dTo2 = Math.sqrt(Math.abs(Math.pow((char2.getX() + length) - this.x, 2) + Math.pow((char2.getY() + width) - this.y, 2)));

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
 void setAnglePointingTo(ServerCharacter character) {

  // this calculate from the centre and calculate the right angel/
  double xDif = Math.abs((character.getX() + length) - (this.x + length));
  double yDif = Math.abs((character.getY() + width) - (this.y + width));
  double referenceAngle;

  if ((character.getX() + length) <= this.x && (character.getY() + width) <= this.y) { // if in quadrant 2
   referenceAngle = Math.atan(yDif / xDif);
   this.anglePointingTo = referenceAngle + Math.toRadians(180);
  } else if ((character.getX() + length) >= this.x && (character.getY() + width) >= this.y) {// if in quadrant 4
   referenceAngle = Math.atan(yDif / xDif);
   this.anglePointingTo = referenceAngle;
  } else if ((character.getX() + length) <= this.x && (character.getY() + width) >= this.y) {// quadrant 3
   referenceAngle = Math.atan(yDif / xDif);
   this.anglePointingTo = -referenceAngle + Math.toRadians(180);
  } else {// quadrant 1
   referenceAngle = Math.atan(yDif / xDif);
   this.anglePointingTo = -referenceAngle;
  }


 }

 /**
  * attack runs when an enemy and character collide decreases health of character
  * if enemy is ready to attack
  * 
  * @param character
  */
 void attack(ServerCharacter character) {
  if (System.currentTimeMillis() - this.lastTimeAttacked > this.attackSpeed) {
   this.lastTimeAttacked = System.currentTimeMillis();
   character.setHealth(character.getHealth() - this.damage);
  }

 }

 /**
  * this method is run when the coordinates of the enemy is changed. It updates
  * the coordinates of its bounding box
  */
 void updateRect() {
  this.rect.setLocation((int) this.x, (int) this.y);
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
  this.updateRect();
 }

 public double getY() {
  return y;
 }

 public void setY(double y) {
  this.y = y;
  this.updateRect();
 }

 public double getAnglePointingTo() {
  return anglePointingTo;
 }

 public void setAnglePointingTo(double anglePointingTo) {
  this.anglePointingTo = anglePointingTo;
 }

 public int getDamage() {
  return damage;
 }

 public void setDamage(int damage) {
  this.damage = damage;
 }

 public int getMoveSpeed() {
  return moveSpeed;
 }

 public void setMoveSpeed(int moveSpeed) {
  this.moveSpeed = moveSpeed;
 }

 public Rectangle getRect() {
  return rect;
 }

 public void setRect(Rectangle rectangle) {
  this.rect = rectangle;
 }

}
