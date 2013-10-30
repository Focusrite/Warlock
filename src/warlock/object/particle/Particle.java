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

   /**
    * Create a new individual single colored particle effect
    * @param position start position
    * @param angle angle it moves towards
    * @param speed speed of the particle
    * @param color color of the particle
    * @param size size of the particle
    * @param lifetime lifetime of the particle
    * @param slowdown how much the speed slowdown factor is per second (under 1 yields slow down,
    * over yields speed up)
    */
   public Particle(Vector position, double angle, double speed, Color color, int size, double lifetime, double slowdown) {
      this.position = position;
      this.movement = new Vector(speed, angle);
      this.color = color;
      this.size = size;
      this.lifetime = lifetime;
      this.slowdown = slowdown;
   }

   /**
    * Update the particle position, lifetime left and speed. Kill it if expired
    * @param dt
    */
   public void update(double dt) {
      position = position.add(movement.scale(dt));
      if (slowdown != 1) {
         movement = movement.scale(1 - ((1 - slowdown) * dt));
      }
      lifetime -= dt;
      if(lifetime < 0) {
         ParticleHandler.kill(this);
      }
   }

   /**
    * Render the particle
    * @param g
    */
   public void render(Graphic g) {
      g.drawRectangle((int) position.getX(), (int) position.getY(), ZLayers.OBJECT, size, size, 0, color);
   }
}
