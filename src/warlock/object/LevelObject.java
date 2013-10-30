/**
 * File: warlock.object.LevelObject.java
 *
 * An abstract class representing any object that can be on the level, excluding particles.
 */
package warlock.object;

import warlock.graphic.Graphic;
import warlock.input.InputHandler;
import warlock.level.GroundType;
import warlock.level.Level;
import warlock.phys.Vector;
import warlock.player.Player;

/**
 *
 * @author Focusrite
 */
public abstract class LevelObject {
   private Vector position = new Vector();
   private Level level;
   private double size = 10; //radius of collision check
   private Player owningPlayer;

   /**
    * @return the radius of the collisionsize
    */
   public double getSize() {
      return size;
   }

   /**
    * @param size new collision radius size
    */
   public void setSize(double size) {
      this.size = size;
   }

   /**
    * @return the owner of this object
    */
   public Player getOwningPlayer() {
      return owningPlayer;
   }

   /**
    * @param owningPlayer new owner
    */
   public void setOwningPlayer(Player owningPlayer) {
      this.owningPlayer = owningPlayer;
   }

   /**
    * @return the level the object is currently on
    */
   public Level getLevel() {
      return level;
   }

   /**
    * @param level new level this object is on
    */
   public void setLevel(Level level) {
      this.level = level;
   }

   /**
    * @return Position as a vector
    */
   public Vector getPosition() {
      return position;
   }

   /**
    * @return the x component of the position vector
    */
   public double getX() {
      return position.getX();
   }

   /**
    * @return the y component of the position vector
    */
   public double getY() {
      return position.getY();
   }

   /**
    * @param position new position vector
    */
   public void setPosition(Vector position) {
      this.position = position;
   }

   /**
    * Set position as (x,y)
    * @param x
    * @param y
    */
   public void setPosition(double x, double y) {
      setPosition(Vector.create(x, y));
   }

   /**
    * @param mod the value to add to the position vector
    */
   public void modifyPosition(Vector mod) {
      this.position = position.add(mod);
   }

   /**
    * Kill this object, removing it when update has finished running
    */
   public void kill() {
      getLevel().removeObject(this);
   }

   /**
    * @return the groundtype of the object center on the level
    */
   public GroundType getGroundType() {
      return getLevel().getGroundTypeAt(getPosition().getX(), getPosition().getY());
   }

   /**
    * Check whether this object collides with another object, o. Collision check is skipped for objects
    * with the same owner, and in that case always returns false
    * @param o object to check
    * @return true if it collides, else false
    */
   public boolean collides(LevelObject o) {
      if(o.getOwningPlayer() == getOwningPlayer()) {
         return false; //Can't collide with your own objects
      }
      return (getPosition().distance(o.getPosition()) < getSize() + o.getSize());
   }

   public void handleInput(double dt, InputHandler input) { }
   public abstract void update(double dt);
   public abstract void render(Graphic g);
   public abstract void handleCollision(LevelObject collidingObject);
}
