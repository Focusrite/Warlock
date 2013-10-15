/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.level;

import java.util.ArrayList;
import warlock.ExtraMath;
import warlock.camera.Camera;
import warlock.constant.Direction;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.object.LevelObject;
import warlock.object.character.Warlock;
import warlock.object.particle.ParticleHandler;
import warlock.object.projectile.Projectile;
import warlock.phys.Vector;
import warlock.player.Player;

/**
 *
 * @author Focusrite
 */
public class Level {

   private ArrayList<LevelObject> objects = new ArrayList<>();
   private ArrayList<LevelObject> warlocks = new ArrayList<>();
   private ArrayList<LevelObject> removingObjects = new ArrayList<>();
   private double scrollX = 0.0;
   private double scrollY = 0.0;
   private Camera camera;
   private Vector groundSize;

   public Level(Camera camera) {
      this.camera = camera;
      this.groundSize = Vector.create(700, 700);
      ParticleHandler.clear();
   }

   public void newRound() {
   }

   public void addObjectRandomPosition(LevelObject o, Player owner) {
      o.setPosition((ExtraMath.getRandom().nextDouble() - 0.5) * 0.9 * groundSize.getX(),
         (ExtraMath.getRandom().nextDouble() - 0.5) * 0.9 * groundSize.getY());
      addObject(o, owner);
   }

   public GroundType getGroundTypeAt(double x, double y) {
      return (Math.abs(x) <= groundSize.getX() / 2 && Math.abs(y) <= groundSize.getY() / 2) ? GroundType.GROUND : GroundType.LAVA;
   }

   public void addObject(LevelObject o) {
      addObject(o, null);
   }

   public void addObject(LevelObject o, Player owner) {
      objects.add(o);
      o.setLevel(this);
      if (owner != null) {
         o.setOwningPlayer(owner);
      }
      if (o instanceof Warlock) {
         warlocks.add(o);
      }
   }

   public void removeObject(LevelObject o) {
      removingObjects.add(o);
      if (o instanceof Warlock) {
         warlocks.remove(o);
      }
   }

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

   public void render(Graphic g) {
      g.drawRectangle(0, 0, (int) groundSize.getX(), (int) groundSize.getY(), 0, new Color(100, 25, 25));
      for (LevelObject o : objects) {
         o.render(g);
      }
      ParticleHandler.render(g);
   }

   public Vector windowToLevelXY(int x, int y) {
      return Vector.create(x, y).add(cameraLookAt());
   }

   private Vector cameraLookAt() {
      return Vector.create(camera.getLookAtX(), camera.getLookAtY());
   }

   public boolean canScroll(Direction direction) {
      return true;
   }

   public void setScrollX(double scrollX) {
      this.scrollX = scrollX;
   }

   public void setScrollY(double scrollY) {
      this.scrollY = scrollY;
   }

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

   public void centerCameraOn(LevelObject obj) {
      camera.reset();
      camera.move((float) obj.getX() - (camera.getWidth() / 2), (float) obj.getY() - (camera.getHeight() / 2));
   }

   public Warlock getClosestWarlock(Warlock self) {
      Warlock target = null;
      double minDistance = 1000000;
      for (int i = 0; i < warlocks.size(); i++) {
         if (warlocks.get(i) instanceof Warlock && warlocks.get(i).getOwningPlayer() != self.getOwningPlayer()
            && self.getPosition().distance(warlocks.get(i).getPosition()) < minDistance) {
            target = (Warlock) warlocks.get(i);
            minDistance = warlocks.get(i).getPosition().distance(self.getPosition());
         }
      }
      return target;
   }

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

   public int getWarlocksLeft() {
      return warlocks.size();
   }
}
