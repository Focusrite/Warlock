/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.object.projectile;

import warlock.constant.ZLayers;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.object.LevelObject;
import warlock.object.character.AttributeHandler;
import warlock.object.character.StatusEffectType;
import warlock.object.character.Warlock;
import warlock.object.particle.ParticleHandler;

/**
 *
 * @author Focusrite
 */
public class FireballProjectile extends Projectile {

   private static final double timerDefault = 0.1;
   private double particleTimer = 0.0f;

   public FireballProjectile(double angle, double speed, double range, int damage, double knockback) {
      super(angle, speed, range, damage, knockback);
   }

   @Override
   public void update(double dt) {
      particleTimer -= dt;
      if (particleTimer < 0) {
         ParticleHandler.spawn(10, getPosition(), ParticleHandler.SIZE_NORMAL, //Positional and size
            getOwningPlayer().getPrimaryColor(), getOwningPlayer().getSecondaryColor(), //Color
            1, //No slowdown
            getHeading().getAngle() - Math.PI, Math.PI, //Angle
            20, 5, //Particle speed
            0.2, 0.1); //Lifetime
         particleTimer = timerDefault;
      }
      setPosition(getPosition().add(getHeading().scale(dt)));
      checkRemoval(dt);
   }

   @Override
   public void render(Graphic g) {
      g.drawCircle((int) getPosition().getX(), (int) getPosition().getY(), ZLayers.OBJECT,
         4, getOwningPlayer().getPrimaryColor());
   }

   @Override
   public void deathFX() {
      ParticleHandler.spawn(20, getPosition(), ParticleHandler.SIZE_SMALL, //Positional and size
         getOwningPlayer().getPrimaryColor(), getOwningPlayer().getSecondaryColor(), //Color
         1, //No slowdown
         getHeading().getAngle(), Math.PI / 8, //Angle
         100, 10, //Particle speed
         0.5, 0.3); //Lifetime
   }

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
