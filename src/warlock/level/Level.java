/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.level;

import java.util.ArrayList;
import warlock.camera.Camera;
import warlock.constant.Direction;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.object.LevelObject;
import warlock.object.particle.ParticleHandler;
import warlock.phys.Vector;
import warlock.player.Player;

/**
 *
 * @author Focusrite
 */
public class Level {

   private ArrayList<LevelObject> objects = new ArrayList<>();
   private ArrayList<LevelObject> removingObjects = new ArrayList<>();
   private double scrollX = 0.0;
   private double scrollY = 0.0;
   private Camera camera;
   private Vector levelSize;
   private Vector groundSize;

   public Level(Camera camera) {
      this.camera = camera;
      this.levelSize = Vector.create(512, 512);
      this.groundSize = Vector.create(800, 800);
      ParticleHandler.clear();
   }

   public void newRound() {
   }

   public void addObjectRandomPosition(LevelObject o, Player owner) {
      o.setPosition(50, 50);
      addObject(o, owner);
      //Remake when proper level implemented to give random valid position on level
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
   }

   public void removeObject(LevelObject o) {
      removingObjects.add(o);
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
         for (int j = i+1; j < objects.size(); j++) {
            if(objects.get(i).collides(objects.get(j))) {
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
   }

   public void centerCameraOn(LevelObject obj) {
      camera.reset();
      camera.move((float) obj.getX() - (camera.getWidth() / 2), (float) obj.getY() - (camera.getHeight() / 2));
   }
}
