/**
 * [ServerCharacter.java]
 * this is the character objects the user will play as
 * it has properties such as damage, health, armour, etc...
 * @author Ali Meshkat
 */


import java.awt.Rectangle;

public class ServerCharacter {
 private int health, maxHealth;
 private int money;
 private double x, y, centerX, centerY;// coordinates
 private double anglePoiningTo;// angle to face cursor
 private int damage;
 private int moveSpeed;
 private Rectangle rect;
 private int armourLvl, damageLvl, moveSpeedLvl; // keeps track of levels

 private double attackSpeed;//how many milliseconds until attack again
 private double lastTimeShot;
 private double speed;
 private boolean isDead;

 private static int damageLvl1, damageLvl2, damageLvl3, damageLvl4;
 private static int armourLvl1, armourLvl2, armourLvl3, armourLvl4;
 private static int moveSpeedLvl1, moveSpeedLvl2, moveSpeedLvl3, moveSpeedLvl4;
 private static int lvl2Cost, lvl3Cost, lvl4Cost;
 private static int width, length;

 private static Clock clock;

 ServerCharacter(int x, int y, int money, Clock sclock) {

  //static variables 
  clock = sclock;
  width = 100;
  length = 100;
  lvl2Cost = 100;
  lvl3Cost = 100;
  lvl4Cost = 100;
  damageLvl1 = 10;
  damageLvl2 = 13;
  damageLvl3 = 16;
  damageLvl4 = 20;
  armourLvl1 = 100;
  armourLvl2 = 150;
  armourLvl3 = 200;
  armourLvl4 = 300;
  moveSpeedLvl1 = 5;
  moveSpeedLvl2 = 6;
  moveSpeedLvl3 = 8;
  moveSpeedLvl4 = 10;

  this.x = x;
  this.y = y;
  this.centerX = this.x + width / 2;
  this.centerY = this.y + length / 2;
  this.rect = new Rectangle((int) this.x, (int) this.y, width, length);
  this.maxHealth = 100;
  this.health = this.maxHealth;
  this.damage = damageLvl1;
  this.isDead = false;
  this.armourLvl = 1;
  this.damageLvl = 1;
  this.moveSpeedLvl = 1;
  this.money = 0;
  this.attackSpeed = 100;
  this.moveSpeed = moveSpeedLvl1;
  this.lastTimeShot = 10000; /// so it will attack right away after start
  this.money = money;
  this.speed = 0.1;

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
    if (keys[i] == 'd') {//if d
     if (this.x + this.speed * clock.getElapsedTime() * 1000 + 100 < 1000) {//if will not go out of bounds
      this.x += this.speed * clock.getElapsedTime() * 1000;//move
     }
    }
    if (keys[i] == 'a') {
     if (this.x - this.speed * clock.getElapsedTime() * 1000 > 0) {
      this.x -= this.speed * clock.getElapsedTime() * 1000;
     }
    }
    if (keys[i] == 's') {
     if (this.y +  this.speed * clock.getElapsedTime() * 1000 + 100 < 800) {

      this.y += this.speed * clock.getElapsedTime() * 1000;
     }
    }
    if (keys[i] == 'w') {
     if (this.y - this.speed * clock.getElapsedTime() * 1000 > 0) {

      this.y -= this.speed * clock.getElapsedTime() * 1000;
     }
    }
   }
   //updates centre x, y and rectangle
   this.centerX = this.x + width / 2;
   this.centerY = this.y + length / 2;
   this.updateRect();
  }
 }

 /**
  * shoot runs when the user clicks creates a bullet at the right direction
  * 
  * @return the bullet created or null if not time to attack
  * @param none
  */
 ServerBullet shoot() {
  if (System.currentTimeMillis() - this.lastTimeShot > this.attackSpeed) {//if not in cooldown 
   this.lastTimeShot = System.currentTimeMillis();//reset cooldown 
   return new ServerBullet(this.centerX, this.centerY, this.anglePoiningTo, this.damage, this);//create new bullet and return
  } else {
   return null;//return null if in cooldown 
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
  * sets the angle the character is pointing at based on the input mouse x and y
  * 
  * @param mouse
  *            x coordinate
  * @param mouse
  *            y coordinate
  */
 void setAnglePointingTo(int mouseX, int mouseY) {
  if (!this.isDead) {

   double xDif = Math.abs(mouseX - (this.x + 50));
   double yDif = Math.abs(mouseY - (this.y + 50));
   double referenceAngle;
   referenceAngle = Math.atan(yDif / xDif);

   if (mouseX <= this.centerX && mouseY <= this.centerY) { // if in quadrant 2
    this.anglePoiningTo = referenceAngle + Math.toRadians(180);
   } else if (mouseX >= this.centerX && mouseY >= this.centerY) {// if in quadrant 4
    this.anglePoiningTo = referenceAngle;
   } else if (mouseX <= this.centerX && mouseY >= this.centerY) {// quadrant 3
    this.anglePoiningTo = -referenceAngle + Math.toRadians(180);
   } else {// quadrant 1
    this.anglePoiningTo = -referenceAngle;
   }
  }
 }

 //////
 /**
  * upgradeWeapon, upgradeArmour, UpgradeMoveSpeed these methods are run when the
  * user hits the upgrade button they set the damage, health, and moveSpeed of
  * character to a higher number when ran =
  * 
  * @param none
  * @return void
  */
 void upgradeWeapon() {
  if (this.damageLvl != 4&& !isDead) {//if not already lvl 4
   int cost;
   
   //finds which cost to use
   if (this.damageLvl == 1) {
    cost = lvl2Cost;
   } else if (this.damageLvl == 2) {
    cost = lvl3Cost;
   } else {
    cost = lvl4Cost;
   }

   //if enough money 
   //processes the upgrade
   if (this.money > cost) {
    if (this.damage == damageLvl1) {
     this.damage = damageLvl2;
     this.damageLvl = 2;
    } else if (this.damage == damageLvl1) {
     this.damage = damageLvl3;
     this.damageLvl = 3;
    } else {
     this.damage = damageLvl4;
     this.damageLvl = 4;
    }
    this.money -= cost;

   }
  }
 }

 void upgradeArmour() {

  if (this.armourLvl != 4 && !isDead) {
   int cost;
   if (this.armourLvl == 1) {
    cost = lvl2Cost;
   } else if (this.armourLvl == 2) {
    cost = lvl3Cost;
   } else {
    cost = lvl4Cost;
   }

   if (this.money > cost) {

    if (this.maxHealth == armourLvl1) {
     this.maxHealth = armourLvl2;
     this.armourLvl = 2;
    } else if (this.maxHealth == armourLvl2) {
     this.maxHealth = armourLvl3;
     this.armourLvl = 3;
    } else {
     this.maxHealth = armourLvl4;
     this.armourLvl = 4;
    }
    this.health = this.maxHealth;// sets health to fulll
    this.money -= cost;

   }
  }

 }

 void upgradeMoveSpeed() {

  if (this.moveSpeedLvl != 4&& !isDead) {
   int cost;
   if (this.moveSpeedLvl == 1) {
    cost = lvl2Cost;
   } else if (this.moveSpeedLvl == 2) {
    cost = lvl3Cost;
   } else {
    cost = lvl4Cost;
   }

   if (this.money > cost) {

    if (this.moveSpeed == moveSpeedLvl1) {
     this.moveSpeed = moveSpeedLvl2;
     this.moveSpeedLvl = 2;
    } else if (this.moveSpeed == moveSpeedLvl1) {
     this.moveSpeed = moveSpeedLvl3;
     this.moveSpeedLvl = 3;
    } else {
     this.moveSpeed = moveSpeedLvl4;
     this.moveSpeedLvl = 4;
    }
    this.money -= cost;

   }
  }
 }
 //////

 /**
  * auto generated getters and setters(by eclipse)
  * 
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

 public int getHealth() {
  return health;
 }

 public void setHealth(int health) {
  this.health = health;
 }

 public int getMaxHealth() {
  return this.maxHealth;
 }

 public void setMaxHealth(int health) {
  this.maxHealth = health;
 }

 public int getMoney() {
  return money;
 }

 public void setMoney(int money) {
  this.money = money;
 }

 public double getX() {
  return x;
 }

 public void setX(double x) {
  this.x = x;
  this.updateRect();
  this.centerX = this.x + width / 2;
 }

 public double getY() {
  return y;
 }

 public void setY(double y) {
  this.y = y;
  this.updateRect();
  this.centerY = this.y + length / 2;
 }

 public double getAnglePoiningTo() {
  return anglePoiningTo;
 }

 public void setAnglePoiningTo(double anglePoiningTo) {
  this.anglePoiningTo = anglePoiningTo;
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

 public int getArmoutLvl() {
  return armourLvl;
 }

 public void setArmoutLvl(int armoutLvl) {
  this.armourLvl = armoutLvl;
 }

 public int getDamageLvl() {
  return damageLvl;
 }

 public void setDamageLvl(int damageLvl) {
  this.damageLvl = damageLvl;
 }

 public int getMoveSpeedLvl() {
  return moveSpeedLvl;
 }

 public void setMoveSpeedLvl(int moveSpeedLvl) {
  this.moveSpeedLvl = moveSpeedLvl;
 }

 public double getAttackSpeed() {
  return attackSpeed;
 }

 public void setAttackSpeed(double attackSpeed) {
  this.attackSpeed = attackSpeed;
 }

}
