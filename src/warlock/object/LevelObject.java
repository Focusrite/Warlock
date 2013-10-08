/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

   public double getSize() {
      return size;
   }

   public void setSize(double size) {
      this.size = size;
   }

   public Player getOwningPlayer() {
      return owningPlayer;
   }

   public void setOwningPlayer(Player owningPlayer) {
      this.owningPlayer = owningPlayer;
   }

   public Level getLevel() {
      return level;
   }

   public void setLevel(Level level) {
      this.level = level;
   }

   public Vector getPosition() {
      return position;
   }

   public double getX() {
      return position.getX();
   }

   public double getY() {
      return position.getY();
   }

   public void setPosition(Vector position) {
      this.position = position;
   }

   public void setPosition(double x, double y) {
      setPosition(Vector.create(x, y));
   }

   public void modifyPosition(Vector mod) {
      this.position = position.add(mod);
   }
   public void kill() {
      getLevel().removeObject(this);
   }

   public GroundType getGroundType() {
      return getLevel().getGroundTypeAt(getPosition().getX(), getPosition().getY());
   }

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
