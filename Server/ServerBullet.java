/**
 * [ServerBullet.java]
 * bullets are shot from characters when clicked 
 * they move towards the mouse location when it was clicked 
 * and if intersect an enemy, will deal damage
 * @author Ali Meshkat
 */


import java.awt.Rectangle;

public class ServerBullet {

 // coordinates
 private double startX, startY;
 private double x, y;

 private double direction;// angle at which it is moving
 private Rectangle rect;// rectangle

 private int damage;
 private double speed;
 private ServerCharacter shooter;
 private static int width, length;

 ServerBullet(double x, double y, double direction, int damage, ServerCharacter shooter) {
  this.startX = x;
  this.startY = y;
  this.x = x;
  this.y = y;
  this.direction = direction;
  // System.out.println(Math.toDegrees(this.direction));
  this.damage = damage;
  this.shooter = shooter;
  width = 6;
  length = 20;
  this.rect = new Rectangle((int) this.startX, (int) this.startY, length, width);
  this.speed = 10;
 }

 /**
  * move moves the bullet towards the direction
  * uses slope of the line obtained from the tan of angle 
  * to determine how much the bullet should move in x and y axis
  * @param elapsedtimefromserver
  * @return void
  */
 void move(double elapsedTime) {

  double reference;

  if (Math.toDegrees(this.direction) >= 180) {//if in quadrant 2

   reference = this.direction - Math.PI;//gets reference angle

   
   //uses angle to find the ratio of how much the bullet should travel in each direction 
   if (Math.tan(reference) < 1) {//y should change more 
    this.y -= this.speed * Math.tan(reference) / (Math.tan(reference) + 1) * elapsedTime * 1000;
    this.x -= this.speed * 1 / (Math.tan(reference) + 1) * elapsedTime * 1000;
   } else {//x should change more
    this.x -= this.speed * 1 / (Math.tan(reference) + 1) * elapsedTime * 1000;
    this.y -= this.speed * Math.tan(reference) / (Math.tan(reference) + 1) * elapsedTime * 1000;
   }

  } else if (Math.toDegrees(this.direction) >= 90) {//quadrant3

   reference = this.direction - Math.PI / 2;

   if (Math.tan(reference) < 1) {

    this.y += this.speed * 1 / (Math.tan(reference) + 1) * elapsedTime * 1000;
    this.x -= this.speed * Math.tan(reference) / (Math.tan(reference) + 1) * elapsedTime * 1000;
   } else {
    this.y += this.speed * 1 / (Math.tan(reference) + 1) * elapsedTime * 1000;
    this.x -= this.speed * Math.tan(reference) / (Math.tan(reference) + 1) * elapsedTime * 1000;
   }
  } else if (Math.toDegrees(this.direction) >= 0) {//quadrant4

   reference = this.direction;

   if (Math.tan(reference) < 1) {
    this.y += this.speed * Math.tan(reference) / (Math.tan(reference) + 1) * elapsedTime * 1000;
    this.x += this.speed * 1 / (Math.tan(reference) + 1) * elapsedTime * 1000;
   } else {
    this.x += this.speed * 1 / (Math.tan(reference) + 1) * elapsedTime * 1000;
    this.y += this.speed * Math.tan(reference) / (Math.tan(reference) + 1) * elapsedTime * 1000;
   }
  } else if (Math.toDegrees(this.direction) >= -90) {//quadrant 1(is in negative angles from -1 to -90)
   reference = Math.abs(this.direction); // - 270;//finds reference angle

   if (Math.tan(reference) < 1) { // if slope smaller than 1
    this.y -= this.speed * Math.tan(reference) / (Math.tan(reference) + 1) * elapsedTime * 1000;// changes x accordingly(more)
    this.x += this.speed * 1 / (Math.tan(reference) + 1) * elapsedTime * 1000; /// changes y accordingly

   } else {

    this.x += this.speed * 1 / (Math.tan(reference) + 1) * elapsedTime * 1000;
    this.y -= this.speed * Math.tan(reference) / (Math.tan(reference) + 1) * elapsedTime * 1000;

   }
  }

  this.updateRect();//updates the rect's coordinates as well
 }

 /**
  * hit is run whenever a collision is detected with an enemy 
  * decreases the heath of the enemy
  * @param enemy
  */
 void hit(ServerEnemy enemy) {
  enemy.setHealth(enemy.getHealth() - this.damage);
 }

 /**
  * this method is run when the coordinates of the bullet is changed. It updates
  * the coordinates of its bounding box
  */
 void updateRect() {
  this.rect.setLocation((int) this.x, (int) this.y);
 }

 /**
  * auto generated getters and setters for variables of bullet class
  */
 public ServerCharacter getShooter() {
  return this.shooter;
 }
 public double getStartX() {
  return startX;
 }

 public void setStartX(double startX) {
  this.startX = startX;
 }

 public double getStartY() {
  return startY;
 }

 public void setStartY(double startY) {
  this.startY = startY;
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

 public double getDirection() {
  return direction;
 }

 public void setDirection(double direction) {
  this.direction = direction;
 }

 public Rectangle getRect() {
  return rect;
 }

 public void setRect(Rectangle rect) {
  this.rect = rect;
 }

 public int getDamage() {
  return damage;
 }

 public void setDamage(int damage) {
  this.damage = damage;
 }

 public double getSpeed() {
  return speed;
 }

 public void setSpeed(double speed) {
  this.speed = speed;
 }

}
