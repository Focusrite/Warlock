/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.object.particle;

import warlock.constant.ZLayers;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.phys.Vector;

/**
 *
 * @author Focusrite
 */
public class Particle {

   private Vector position;
   private Vector movement;
   private Color color;
   private int size;
   private double slowdown;
   private double lifetime;

   public Particle(Vector position, double angle, double speed, Color color, int size, double lifetime, double slowdown) {
      this.position = position;
      this.movement = new Vector(speed, angle);
      this.color = color;
      this.size = size;
      this.lifetime = lifetime;
      this.slowdown = slowdown;
   }

   public void update(double dt) {
      position = position.add(movement.scale(dt));
      if (slowdown != 1) {
         movement = movement.scale(slowdown * dt);
      }
      lifetime -= dt;
      if(lifetime < 0) {
         ParticleHandler.kill(this);
      }
   }

   public void render(Graphic g) {
      g.drawRectangle((int) position.getX(), (int) position.getY(), ZLayers.OBJECT, size, size, 0, color);
   }
}
