/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.spell;

import warlock.object.character.Warlock;
import warlock.object.projectile.FireballProjectile;
import warlock.phys.Vector;

/**
 *
 * @author Focusrite
 */
public class Fireball extends Spell {

   private static final double damage[] = {10, 15, 15, 20, 20};
   private static final double range[] = {250, 250, 300, 300, 350};
   private static final double knockback[] = {200, 200, 250, 250, 300};
   private static final double cooldown[] = {2, 2, 2, 2, 2};
   private static final double speed[] = {100, 100, 100, 100, 100};

   public Fireball(Warlock owner) {
      super(owner, "Fireball", SpellTarget.GROUND, SpellShortcut.MB, 2);
      setCooldown(cooldown[0]);
      setSpellIcon("spell-fireball");
      setMaxLevel(5);
      setType(SpellType.PROJECTILE);

      setBaseDescription("A basic fireball spell \n"
         + "every warlock must master\n"
         + "to even have a shot at \n"
         + "surviving the deadly \n"
         + "Fighting Plataue.");
      setLevelDataNames(new String[]{"Damage", "Knockback", "Range", "Speed", "Cooldown"});
      setLevelData(new double[][]{damage,knockback, range, speed, cooldown});
      rebuildDescription();
   }

   @Override
   public void cast(Vector castVector) {
      FireballProjectile projectile = new FireballProjectile(castVector.getAngle(),
         speed[getSpellLevel() - 1],
         range[getSpellLevel() - 1],
         (int) damage[getSpellLevel() - 1],
         knockback[getSpellLevel() - 1]);
      projectile.setPosition(getOwner().getX(), getOwner().getY());
      getOwner().getLevel().addObject(projectile, getOwner().getOwningPlayer());
   }

   @Override
   public Fireball clone() {
      Fireball spell = new Fireball(null);
      return spell;
   }
}
