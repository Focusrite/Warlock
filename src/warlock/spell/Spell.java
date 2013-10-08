/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.spell;

import warlock.object.character.Warlock;

/**
 *
 * @author Focusrite
 */
public abstract class Spell {

   private SpellTarget target;
   private SpellShortcut shortcut;
   private Warlock owner;
   private double cooldown;
   private double currentCooldown = 0;

   public Warlock getOwner() {
      return owner;
   }

   public void setOwner(Warlock owner) {
      this.owner = owner;
   }

   public Spell(Warlock owner, SpellTarget target, SpellShortcut shortcut, double cooldown) {
      this.target = target;
      this.shortcut = shortcut;
      this.owner = owner;
      this.cooldown = cooldown;
   }

   public double getCooldown() {
      return cooldown;
   }

   public void setCooldown(double cooldown) {
      this.cooldown = cooldown;
   }

   public double getCurrentCooldown() {
      return currentCooldown;
   }

   public void setCurrentCooldown(double currentCooldown) {
      this.currentCooldown = currentCooldown;
   }

   public void modifyCurrentCooldown(double dCurrentCooldown) {
      this.currentCooldown += dCurrentCooldown;
   }

   public SpellShortcut getShortcut() {
      return shortcut;
   }

   public void setShortcut(SpellShortcut shortcut) {
      this.shortcut = shortcut;
   }

   public SpellTarget getTarget() {
      return target;
   }

   public void setTarget(SpellTarget target) {
      this.target = target;
   }

   public abstract void cast(double angle);

   public boolean canCast() {
      if(getCurrentCooldown() > 0) {
         return false; //later send notify message to HUD too, hence if statement
      }
      return true;
   }

   public void preCast() {
      setCurrentCooldown(getCooldown());
   }

   public void update(double dt) {
      if(getCurrentCooldown() > 0) {
         modifyCurrentCooldown(-dt);
      }
   }
}
