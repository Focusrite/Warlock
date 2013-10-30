/**
 * File: warlock.level.Level.java
 *
 * A level, aka Deadly Death Plateau, where the warlocks fight. Handles keeping track of all the
 * objects on the level (and runs their update/render), level settings, camera position in the level,
 * as well as other goody methods for getting data from the level.
 */
package warlock.level;

import java.util.ArrayList;
import warlock.ExtraMath;
import warlock.camera.Camera;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.object.LevelObject;
import warlock.object.particle.ParticleHandler;
import warlock.object.projectile.Projectile;
import warlock.object.warlock.Warlock;
import warlock.phys.Vector;
import warlock.player.Player;

/**
 *
 * @author Focusrite
 */
public class Level {
   private static final double RANDOM_POSITION_SCALING = 0.9;
   private ArrayList<LevelObject> objects = new ArrayList<>();
   private ArrayList<Warlock> warlocks = new ArrayList<>();
   private ArrayList<LevelObject> removingObjects = new ArrayList<>();
   private double scrollX = 0.0;
   private double scrollY = 0.0;
   private Camera camera;
   private Vector groundSize;

   /**
    * Create a new rectangular level
    * @param camera
    * @param size
    */
   public Level(Camera camera, int size) {
      this.camera = camera;
      this.groundSize = Vector.create(size, size);
      ParticleHandler.clear();
   }

   public void newRound() { }

   /**
    * Add an object at a random position on the level inside solid ground area.
    * @param o object
    * @param owner
    */
   public void addObjectRandomPosition(LevelObject o, Player owner) {
      o.setPosition((ExtraMath.getRandom().nextDouble() - 0.5) * RANDOM_POSITION_SCALING * groundSize.getX(),
         (ExtraMath.getRandom().nextDouble() - 0.5) * RANDOM_POSITION_SCALING * groundSize.getY());
      addObject(o, owner);
   }

   /**
    * Return the ground type enumeration at position (x, y)
    * @param x
    * @param y
    * @return the ground type
    */
   public GroundType getGroundTypeAt(double x, double y) {
      return (Math.abs(x) <= groundSize.getX() / 2 && Math.abs(y) <= groundSize.getY() / 2) ? GroundType.GROUND : GroundType.LAVA;
   }

   /**
    * Add an object to the level
    * @param o
    */
   public void addObject(LevelObject o) {
      addObject(o, null);
   }

   /**
    * Add an object to the level, and set owner of the object to "owner"
    * @param o
    * @param owner
    */
   public void addObject(LevelObject o, Player owner) {
      objects.add(o);
      o.setLevel(this);
      if (owner != null) {
         o.setOwningPlayer(owner);
      }
      if (o instanceof Warlock) {
         warlocks.add((Warlock)o);
      }
   }

   /**
    * Remove an object from the level, notice that the actual removal of objects from the level happens
    * next update.
    * @param o the object to remove
    */
   public void removeObject(LevelObject o) {
      removingObjects.add(o);
      if (o instanceof Warlock) {
         warlocks.remove((Warlock)o);
      }
   }

   /**
    * Update scrolling and all objects on the level. Also does removing of objects marked for delete.
    * @param dt
    */
   public void update(double dt) {
      scroll(dt);

      for (LevelObject o : objects) {
         o.update(dt);
      }
      while (removingObjects.size() > 0) {
         objects.remove(removingObjects.get(0));
         removingObjects.remove(0);
      }
      collisionCheck();
      ParticleHandler.update(dt);
   }

   /**
    * Iterate all objects and checks if they collide with any other, if they do run both objects
    * handleCollision method.
    */
   private void collisionCheck() {
      for (int i = 0; i < objects.size(); i++) {
         for (int j = i + 1; j < objects.size(); j++) {
            if (objects.get(i).collides(objects.get(j))) {
               objects.get(i).handleCollision(objects.get(j));
               objects.get(j).handleCollision(objects.get(i));
            }
         }
      }
   }

   /**
    * Render the level, particles and all objects on the level
    * @param g
    */
   public void render(Graphic g) {
      g.drawRectangle(0, 0, (int) groundSize.getX(), (int) groundSize.getY(), 0, new Color(100, 25, 25));
      for (LevelObject o : objects) {
         o.render(g);
      }
      ParticleHandler.render(g);
   }

   /**
    * Add the mouse (x,y) to the current camera lookAt (x,y) to get what point was clicked in world
    * coordinates.
    * @param x
    * @param y
    * @return Vector in world coordinates
    */
   public Vector windowToLevelXY(int x, int y) {
      return Vector.create(x, y).add(cameraLookAt());
   }

   /**
    * @return (x,y) vector of camera coordinates
    */
   private Vector cameraLookAt() {
      return Vector.create(camera.getLookAtX(), camera.getLookAtY());
   }

   /**
    * Set the amount of scroll in x should be done per second in each scroll call.
    * @param scrollX
    */
   public void setScrollX(double scrollX) {
      this.scrollX = scrollX;
   }

   /**
    * Set the amount of scroll in y should be done per second in each scroll call.
    * @param scrollY
    */
   public void setScrollY(double scrollY) {
      this.scrollY = scrollY;
   }

   /**
    * Scroll the camera in the direction it's supposed to, but constrain camera to world bounds.
    * @param dt
    */
   private void scroll(double dt) {
      double dx = scrollX;
      double dy = scrollY;

      camera.move((float) (dx * dt), (float) (dy * dt));
      //Constrain to level
      if (camera.getLookAtX() > groundSize.getX() / 2 - camera.getWidth() / 2) {
         camera.setX((float) groundSize.getX() / 2 - camera.getWidth() / 2);
      }
      else if (camera.getLookAtX() < -groundSize.getX() / 2 - camera.getWidth() / 2) {
         camera.setX((float) -groundSize.getX() / 2 - camera.getWidth() / 2);
      }

      if (camera.getLookAtY() > groundSize.getY() / 2 - camera.getHeight() / 2) {
         camera.setY((float) groundSize.getY() / 2 - camera.getHeight() / 2);
      }
      else if (camera.getLookAtY() < -groundSize.getY() / 2 - camera.getHeight() / 2) {
         camera.setY((float) -groundSize.getY() / 2 - camera.getHeight() / 2);
      }
   }

   /**
    * Center the camera on an object in the world
    * @param obj to center around
    */
   public void centerCameraOn(LevelObject obj) {
      camera.reset();
      camera.move((float) obj.getX() - (camera.getWidth() / 2), (float) obj.getY() - (camera.getHeight() / 2));
   }

   /**
    * Return the closest warlock to another.
    * @param self
    * @return closest warlock, or null if no other warlocks on the level
    */
   public Warlock getClosestWarlock(Warlock self) {
      Warlock target = null;
      double minDistance = 1000000;
      for (int i = 0; i < warlocks.size(); i++) {
         if (warlocks.get(i).getOwningPlayer() != self.getOwningPlayer()
            && self.getPosition().distance(warlocks.get(i).getPosition()) < minDistance) {
            target = (Warlock) warlocks.get(i);
            minDistance = warlocks.get(i).getPosition().distance(self.getPosition());
         }
      }
      return target;
   }

   /**
    * Returns the closest projectile to a warlock.
    * @param self
    * @return closest projectile, or null if no projectile close
    */
   public Projectile getClosestProjectile(Warlock self) {
      Projectile target = null;
      double minDistance = 1000000;
      for (int i = 0; i < objects.size(); i++) {
         if (objects.get(i) instanceof Projectile && objects.get(i).getOwningPlayer() != self.getOwningPlayer()
            && self.getPosition().distance(objects.get(i).getPosition()) < minDistance) {
            target = (Projectile) objects.get(i);
            minDistance = objects.get(i).getPosition().distance(self.getPosition());
         }
      }
      return target;
   }

   /**
    * @return Amount of warlocks left on the level
    */
   public int getWarlocksLeft() {
      return warlocks.size();
   }

   /**
    * Returns a ArrayList of warlocks in a circular distance of a point (vector), bar from self.
    * @param position
    * @param self
    * @param radius
    * @return ArrayList<Warlock> warlocks in radius
    */
   public ArrayList<Warlock> getWarlocksInDistance(Vector position, Warlock self, double radius) {
      ArrayList<Warlock> inDistance = new ArrayList<>();
      for (int i = 0; i < warlocks.size(); i++) {
         if(warlocks.get(i).getOwningPlayer() != self.getOwningPlayer() &&
            position.distance(warlocks.get(i).getPosition()) <= radius) {
            inDistance.add(warlocks.get(i));
         }
      }
      return inDistance;
   }


}
