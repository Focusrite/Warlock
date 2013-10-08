/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.object.projectile;

import warlock.graphic.Graphic;
import warlock.object.LevelObject;
import warlock.phys.Vector;

/**
 *
 * @author Focusrite
 */
public abstract class Projectile extends LevelObject{
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
      this.lifetime+= dLifetime;
   }

   public int getDamage() {
      return damage;
   }

   public void setDamage(int damage) {
      this.damage = damage;
   }

   public Projectile(double speed, double angle, double lifetime, int damage, double knockback) {
      this.heading = new Vector(speed, angle);
      this.lifetime = lifetime;
      this.damage = damage;
      this.knockback = knockback;
   }

   public void checkRemoval(double dt) {
      modifyLifetime(-dt);
      if(this.lifetime < 0) {
         deathFX();
         kill();
      }
   }

   public void deathFX() {};
   public void hitFX() {};

   @Override
   public abstract void update(double dt);

   @Override
   public abstract void render(Graphic g);

}
