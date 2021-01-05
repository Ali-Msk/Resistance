/**
 * [PlayerBullet.java]
 * bullets are shot from characters when clicked 
 * they move towards the mouse location when it was clicked 
 * and if intersect an enemy, will deal damage
 * this is the player side bullet class which means it includes the picture and 
 * draw method
 * @author Ali Meshkat
 */
 

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;

public class PlayerBullet {
 private double x, y;
 private double direction;
 private static Image image;
 private double speed;
 private static int width, length;
 private Rectangle rect;

 PlayerBullet(double x, double y, double direction) {
  this.x = x;
  this.y = y;
  this.direction = direction;
  image = Toolkit.getDefaultToolkit().getImage("bullet.png");

  this.speed = 5;
  width = 6;
  length = 20;
  this.rect = new Rectangle((int) this.x, (int) this.y, length, width);

 }

 /**
  * draw draws the bullet at the current x and y and at the direction it is
  * pointing to
  * 
  * @return void
  * @param graphics
  *            g
  */
 void draw(Graphics g) {

  // creates affineTransform object used to rotate images
  AffineTransform affineTransform = AffineTransform.getTranslateInstance((int) this.x, (int) this.y);
  affineTransform.rotate(this.direction);// sets angle
  Graphics2D graphics2D = (Graphics2D) g;// creates graphics 2d object
  graphics2D.drawImage(image, affineTransform, null);// draws image

 }

 /**
  * move moves the bullet towards the direction uses slope of the line obtained
  * from the tan of angle to determine how much the bullet should move in x and y
  * axis
  * 
  * @param elapsedtimefromserver
  * @return void
  */
 void move(double elapsedTime) {

  double reference;
  if (Math.toDegrees(this.direction) >= 180) {

   reference = this.direction - Math.PI;

   if (Math.tan(reference) < 1) {
    this.y -= this.speed * Math.tan(reference) / (Math.tan(reference) + 1) * elapsedTime * 1000;
    this.x -= this.speed * 1 / (Math.tan(reference) + 1) * elapsedTime * 1000;
   } else {
    this.x -= this.speed * 1 / (Math.tan(reference) + 1) * elapsedTime * 1000;
    this.y -= this.speed * Math.tan(reference) / (Math.tan(reference) + 1) * elapsedTime * 1000;
   }

  } else if (Math.toDegrees(this.direction) >= 90) {

   reference = this.direction - Math.PI / 2;

   if (Math.tan(reference) < 1) {
    this.y += this.speed * 1 / (Math.tan(reference) + 1) * elapsedTime * 1000;
    this.x -= this.speed * Math.tan(reference) / (Math.tan(reference) + 1) * elapsedTime * 1000;
   } else {
    this.y += this.speed * 1 / (Math.tan(reference) + 1) * elapsedTime * 1000;
    this.x -= this.speed * Math.tan(reference) / (Math.tan(reference) + 1) * elapsedTime * 1000;
   }
  } else if (Math.toDegrees(this.direction) >= 0) {

   reference = this.direction;

   if (Math.tan(reference) < 1) {
    this.y += this.speed * Math.tan(reference) / (Math.tan(reference) + 1) * elapsedTime * 1000;
    this.x += this.speed * 1 / (Math.tan(reference) + 1) * elapsedTime * 1000;
   } else {
    this.x += this.speed * 1 / (Math.tan(reference) + 1) * elapsedTime * 1000;
    this.y += this.speed * Math.tan(reference) / (Math.tan(reference) + 1) * elapsedTime * 1000;
   }
  } else if (Math.toDegrees(this.direction) >= -90) {
   reference = Math.abs(this.direction);// finds reference angle

   if (Math.tan(reference) < 1) { // if slope smaller than 1
    this.y -= this.speed * Math.tan(reference) / (Math.tan(reference) + 1) * elapsedTime * 1000;// changes x accordingly(more)
    this.x += this.speed * 1 / (Math.tan(reference) + 1) * elapsedTime * 1000; /// changes y accordingly

   } else {
    this.x += this.speed * 1 / (Math.tan(reference) + 1) * elapsedTime * 1000;
    this.y -= this.speed * Math.tan(reference) / (Math.tan(reference) + 1) * elapsedTime * 1000;

   }
  }

  this.updateRect();
 }

 void updateRect() {
  this.rect.setLocation((int) this.x, (int) this.y);
 }

 public Rectangle getRect() {
  return rect;
 }

}
