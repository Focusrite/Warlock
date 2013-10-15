/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.object.projectile;

import warlock.graphic.Graphic;
import warlock.object.LevelObject;
import warlock.object.character.AttributeHandler;
import warlock.object.character.StatusEffectType;
import warlock.object.character.Warlock;
import warlock.phys.Vector;

/**
 *
 * @author Focusrite
 */
public abstract class Projectile extends LevelObject {

   private Vector heading;
   private double lifetime;
   private int damage;
   private double knockback;

   public double getKnockback() {
      return knockback;
   }

   public void setKnockback(double knockback) {
      this.knockback = knockback;
   }

   public Vector getHeading() {
      return heading;
   }

   public void setHeading(Vector heading) {
      this.heading = heading;
   }

   public double getLifetime() {
      return lifetime;
   }

   public void setLifetime(double lifetime) {
      this.lifetime = lifetime;
   }

   public void modifyLifetime(double dLifetime) {
      this.lifetime += dLifetime;
   }

   public int getDamage() {
      return damage;
   }

   public void setDamage(int damage) {
      this.damage = damage;
   }

   public Projectile(double angle, double speed, double range, int damage, double knockback) {
      this.heading = new Vector(speed, angle);
      this.lifetime = range / speed;
      this.damage = damage;
      this.knockback = knockback;
      setSize(5);
   }

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

   @Override
   public void handleCollision(LevelObject collidingObject) {
      if (collidingObject instanceof Warlock) {
         Warlock w = (Warlock) collidingObject;
         if(w.attrVal("shield") > 0) {
            setOwningPlayer(w.getOwningPlayer());
            w.getLastStatusEffect("shield").forceExpiration();
            getHeading().rotate(Math.PI);
            return;
         }
         w.inflictStatusEffect(new StatusEffectType(getDamage(), AttributeHandler.get("hp")), getOwningPlayer());
         getHeading().setLength(getKnockback());
         w.applyForce(getHeading());
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
