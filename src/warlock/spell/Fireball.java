/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.spell;

import warlock.object.character.Warlock;
import warlock.object.projectile.FireballProjectile;

/**
 *
 * @author Focusrite
 */
public class Fireball extends Spell {

   public Fireball(Warlock owner) {
      super(owner, SpellTarget.GROUND, SpellShortcut.MB, 2);
   }

   @Override
   public void cast(double angle) {
      FireballProjectile projectile = new FireballProjectile(angle, 10);
      projectile.setPosition(getOwner().getX(), getOwner().getY());
      getOwner().getLevel().addObject(projectile, getOwner().getOwningPlayer());
   }

}
