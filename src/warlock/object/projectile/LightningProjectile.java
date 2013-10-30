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

/**
 *
 * @author Focusrite
 */
public class LightningProjectile extends Projectile {

   private static final double timerDefault = 0.1;
   private double particleTimer = 0.0f;

   /**
    * Creates a new lightning projectile, cast by using the lightning spell
    * @param angle
    * @param speed
    * @param range
    * @param damage
    * @param knockback
    */
   public LightningProjectile(double angle, double speed, double range, int damage, double knockback) {
      super(angle, speed, range, damage, knockback);
   }

   /**
    * Update position and create particle effect after the projectile
    * @param dt
    */
   @Override
   public void update(double dt) {
      particleTimer -= dt;
      if (particleTimer < 0) {
         ParticleHandler.spawn(5, getPosition(), ParticleHandler.SIZE_NORMAL, //Positional and size
            getOwningPlayer().getPrimaryColor(), getOwningPlayer().getSecondaryColor(), //Color
            1, //No slowdown
            getHeading().getAngle() - Math.PI, Math.PI, //Angle
            10, 5, //Particle speed
            0.5, 0.1); //Lifetime
         ParticleHandler.spawn(10, getPosition(), ParticleHandler.SIZE_SMALL, //Positional and size
            getOwningPlayer().getPrimaryColor(), getOwningPlayer().getSecondaryColor(), //Color
            1, //No slowdown
            0, 2 * Math.PI, //Angle
            80, 5, //Particle speed
            0.2, 0.1); //Lifetime
         particleTimer = timerDefault;
      }
      setPosition(getPosition().add(getHeading().scale(dt)));
      checkRemoval(dt);
   }

   /**
    * Render the projectile
    * @param g
    */
   @Override
   public void render(Graphic g) {
      g.drawRectangle((int) getPosition().getX(), (int) getPosition().getY(), ZLayers.OBJECT,
         6, 2, getHeading().getAngle(), getOwningPlayer().getPrimaryColor());
   }

   /**
    * The particle effect played when this projectile expires
    */
   @Override
   public void deathFX() {
      ParticleHandler.spawn(20, getPosition(), ParticleHandler.SIZE_SMALL, //Positional and size
         getOwningPlayer().getPrimaryColor(), getOwningPlayer().getSecondaryColor(), //Color
         1, //No slowdown
         getHeading().getAngle(), Math.PI / 8, //Angle
         200, 10, //Particle speed
         0.5, 0.3); //Lifetime
   }

   /**
    * The particle effect that plays when this projectile hits a target
    */
   @Override
   public void hitFX() {
      ParticleHandler.spawn(50, getPosition(), ParticleHandler.SIZE_NORMAL, //Positional and size
         getOwningPlayer().getPrimaryColor(), getOwningPlayer().getSecondaryColor(), //Color
         1, //No slowdown
         getHeading().getAngle() - Math.PI, Math.PI / 2, //Angle
         50, 10, //Particle speed
         0.7, 0.3); //Lifetime
   }
}
