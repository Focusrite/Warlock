/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.object.particle;

import java.util.ArrayList;
import warlock.ExtraMath;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.phys.Vector;

/**
 *
 * @author Focusrite
 */
public class ParticleHandler {
   public static final int SIZE_SMALL = 2;
   public static final int SIZE_NORMAL = 4;
   public static final int SIZE_LARGE = 6;

   private static ArrayList<Particle> particles = new ArrayList<>();
   private static ArrayList<Particle> removingParticles = new ArrayList<>();

   /**
    * Creates multiple particles at a position
    * @param amount of particles to create
    * @param position start position
    * @param size size of the particles
    * @param from first color, a color is randomized between this..
    * @param to second color, .. and this color
    * @param slowdown how much the speed slowdown factor is per second (under 1 yields slow down,
    * over yields speed up)
    * @param angle middle angle the particles can move towards, in radians
    * @param dAngle how much the angle can vary in radians
    * @param speed the speed of which particles move
    * @param dSpeed the amount of which the speed can vary
    * @param lifetime the lifetime in seconds of this particle
    * @param dLifetime how much the lifetime can vary
    */
   public static void spawn(int amount, Vector position, int size, Color from, Color to, double slowdown,
      double angle, double dAngle, double speed, double dSpeed, double lifetime, double dLifetime) {
      for (int i = 0; i < amount; i++) {
         double ang = angle + ((0.5 - ExtraMath.getRandom().nextDouble()) * 2 * dAngle);
         double spd = speed + ((0.5 - ExtraMath.getRandom().nextDouble()) * 2 * dSpeed);
         double life = lifetime + ((0.5 - ExtraMath.getRandom().nextDouble()) * 2 * dLifetime);
         Color c = from.randomColorBetween(to);
         spawnParticle(position, size, c, slowdown, ang, spd, life);
      }
   }

   /**
    * Add an individual particle to this particle handler
    * @param position
    * @param size
    * @param color
    * @param slowdown
    * @param angle
    * @param speed
    * @param lifetime
    */
   private static void spawnParticle(Vector position, int size, Color color, double slowdown,
      double angle, double speed, double lifetime) {
      Particle p = new Particle(position, angle, speed, color, size, lifetime, slowdown);
      particles.add(p);
   }

   /**
    * Kill an individual particle
    * @param p
    */
   public static void kill(Particle p) {
      removingParticles.add(p);
   }

   /**
    * Update all particles and remove those that has expired
    * @param dt
    */
   public static void update(double dt) {
      for (Particle p : particles) {
         p.update(dt);
      }
      while (removingParticles.size() > 0) {
         particles.remove(removingParticles.get(0));
         removingParticles.remove(0);
      }
   }

   /**
    * Render all particles
    * @param g
    */
   public static void render(Graphic g) {
      for (Particle p : particles) {
         p.render(g);
      }
   }

   /**
    * Clear all particles
    */
   public static void clear() {
      particles.clear();
      removingParticles.clear();
   }
}
