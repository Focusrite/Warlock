/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.object.projectile;

import warlock.graphic.Graphic;
import warlock.object.LevelObject;
import warlock.attribute.AttributeHandler;
import warlock.attribute.StatusEffectType;
import warlock.object.warlock.Warlock;
import warlock.phys.Vector;

/**
 *
 * @author Focusrite
 */
public abstract class Projectile extends LevelObject {

   private static final double MIN_KNOCKBACK = 0.7;
   private static final double KNOCKBACK_HEALTH_INFLUENCE = 0.3;
   private Vector heading;
   private double lifetime;
   private int damage;
   private double knockback;

   /**
    * @return the knockback value set for the projectile when it hits a warlock
    */
   public double getKnockback() {
      return knockback;
   }

   /**
    * @param knockback new knockback balue
    */
   public void setKnockback(double knockback) {
      this.knockback = knockback;
   }

   /**
    * @return the heading vector, with both speed and angle of movement
    */
   public Vector getHeading() {
      return heading;
   }

   /**
    * @param heading new heading vector, both speed and angle of movement
    */
   public void setHeading(Vector heading) {
      this.heading = heading;
   }

   /**
    * @return the current lifetime left of this projectile
    */
   public double getLifetime() {
      return lifetime;
   }

   /**
    * @param lifetime new lifetime
    */
   public void setLifetime(double lifetime) {
      this.lifetime = lifetime;
   }

   /**
    * @param dLifetime value to add to current lifetime
    */
   public void modifyLifetime(double dLifetime) {
      this.lifetime += dLifetime;
   }

   /**
    * @return the damage this projectile inflicts when it hits a warlock
    */
   public int getDamage() {
      return damage;
   }

   /**
    * @param damage new damage value
    */
   public void setDamage(int damage) {
      this.damage = damage;
   }

   /**
    * @param angle
    * @param speed
    * @param range
    * @param damage
    * @param knockback
    */
   public Projectile(double angle, double speed, double range, int damage, double knockback) {
      this.heading = new Vector(speed, angle);
      this.lifetime = range / speed;
      this.damage = damage;
      this.knockback = knockback;
      setSize(5);
   }

   /**
    * Check whether this projectile has expired, and if it has, kill it and play deathFX method
    *
    * @param dt
    */
   public void checkRemoval(double dt) {
      modifyLifetime(-dt);
      if (this.lifetime < 0) {
         deathFX();
         kill();
      }
   }

   public void deathFX() {
   }

   public void hitFX() {
   }

   @Override
   public abstract void update(double dt);

   @Override
   public abstract void render(Graphic g);

   /**
    * Handle collision with other objects
    *
    * @param collidingObject
    */
   @Override
   public void handleCollision(LevelObject collidingObject) {
      if (collidingObject instanceof Warlock) {
         Warlock w = (Warlock) collidingObject;
         if (w.attrVal("shield") > 0) { //If player has shield activated, projectile becomes a traitor!
            setOwningPlayer(w.getOwningPlayer());
            w.getLastStatusEffect("shield").forceExpiration();
            getHeading().rotate(Math.PI);
            return;
         }
         //Do damage
         w.inflictStatusEffect(new StatusEffectType(getDamage(), AttributeHandler.get("hp")), getOwningPlayer());
         //Scale based on damage taken
         double knockbackScaling = MIN_KNOCKBACK + KNOCKBACK_HEALTH_INFLUENCE
            * (1 - (w.attrVal("hp") / w.attrBaseVal("hp")));
         //Apply knockback force
         getHeading().setLength(getKnockback());
         w.applyForce(getHeading().scale(knockbackScaling));
         //Play fx
         w.hitFX(getHeading().getAngle());
         hitFX();
         kill();
      }
      else if (collidingObject instanceof Projectile) {
         Projectile p = (Projectile) collidingObject;
         p.hitFX();
         hitFX();
         p.kill();
         kill();
      }
   }
}
