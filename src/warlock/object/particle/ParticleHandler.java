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

   private static void spawnParticle(Vector position, int size, Color color, double slowdown,
      double angle, double speed, double lifetime) {
      Particle p = new Particle(position, angle, speed, color, size, lifetime, slowdown);
      particles.add(p);
   }

   public static void kill(Particle p) {
      removingParticles.add(p);
   }

   public static void update(double dt) {
      for (Particle p : particles) {
         p.update(dt);
      }
      while (removingParticles.size() > 0) {
         particles.remove(removingParticles.get(0));
         removingParticles.remove(0);
      }
   }

   public static void render(Graphic g) {
      for (Particle p : particles) {
         p.render(g);
      }
   }

   public static void clear() {
      particles.clear();
      removingParticles.clear();
   }
}
