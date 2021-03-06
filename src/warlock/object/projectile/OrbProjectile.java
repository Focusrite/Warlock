/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.object.projectile;

import warlock.constant.ZLayers;
import warlock.graphic.Graphic;
import warlock.object.LevelObject;
import warlock.attribute.AttributeHandler;
import warlock.attribute.StatusEffectType;
import warlock.object.warlock.Warlock;
import warlock.object.particle.ParticleHandler;
import warlock.phys.Vector;

/**
 *
 * @author Focusrite
 */
public class OrbProjectile extends Projectile {

   private static final double RADIANS_PER_SECONDS = Math.PI / 4;
   private static final double timerDefault = 0.1;
   private double particleTimer = 0.0f;
   private boolean dead = false;

   /**
    * Creates a new orb projectile, cast by using the seekerorb spell
    * @param angle
    * @param speed
    * @param range
    * @param damage
    * @param knockback
    */
   public OrbProjectile(double angle, double speed, double range, int damage, double knockback) {
      super(angle, speed, range, damage, knockback);
   }

   /**
    * Update position and create particle effect after the projectile
    * @param dt
    */
   @Override
   public void update(double dt) {
      checkRemoval(dt);
      particleTimer -= dt;
      steer(dt);
      if (particleTimer < 0) {
         ParticleHandler.spawn(10, getPosition(), ParticleHandler.SIZE_NORMAL, //Positional and size
            getOwningPlayer().getPrimaryColor(), getOwningPlayer().getSecondaryColor(), //Color
            1, //No slowdown
            0, Math.PI * 2, //Angle
            40, 5, //Particle speed
            0.3, 0.1); //Lifetime
         particleTimer = timerDefault;
      }
      setPosition(getPosition().add(getHeading().scale(dt)));
   }

   /**
    * Guide the orb towards the closest enemy warlock
    * @param dt
    */
   private void steer(double dt) {
      if(dead) {
         return;
      }
      Warlock closest = getLevel().getClosestWarlock(getOwningPlayer().getWarlock());
      if(closest == null) {
         return;
      }
      Vector v = closest.getPosition().subtract(getPosition());
      double dAngle = v.angleBetween(getHeading());
      dAngle = (Math.abs(dAngle) > RADIANS_PER_SECONDS) ? (dAngle >= 0)
         ? RADIANS_PER_SECONDS * dt : -RADIANS_PER_SECONDS * dt : dAngle;
      getHeading().rotate(dAngle);
   }

   /**
    * Render the projectile
    * @param g
    */
   @Override
   public void render(Graphic g) {
      g.drawCircle((int) getPosition().getX(), (int) getPosition().getY(), ZLayers.OBJECT,
         6, getOwningPlayer().getPrimaryColor());
   }

   /**
    * The particle effect played when this projectile expires
    */
   @Override
   public void deathFX() {
      dead = true;
      ParticleHandler.spawn(20, getPosition(), ParticleHandler.SIZE_SMALL, //Positional and size
         getOwningPlayer().getPrimaryColor(), getOwningPlayer().getSecondaryColor(), //Color
         1, //No slowdown
         getHeading().getAngle(), Math.PI / 8, //Angle
         100, 10, //Particle speed
         0.5, 0.3); //Lifetime
   }

   /**
    * The particle effect that plays when this projectile hits a target
    */
   @Override
   public void hitFX() {
      ParticleHandler.spawn(50, getPosition(), ParticleHandler.SIZE_LARGE, //Positional and size
         getOwningPlayer().getPrimaryColor(), getOwningPlayer().getSecondaryColor(), //Color
         1, //No slowdown
         0, Math.PI * 2, //Angle
         80, 10, //Particle speed
         0.9, 0.3); //Lifetime
   }
}
