/**
 * File: warlock.spell.Fireball.java
 *
 * The normal spell spammed throughout.
 */
package warlock.spell;

import warlock.object.projectile.FireballProjectile;
import warlock.object.warlock.Warlock;
import warlock.phys.Vector;

public class Fireball extends Spell {

   private static final double damage[] = {10, 15, 15, 20, 20};
   private static final double range[] = {250, 250, 300, 300, 350};
   private static final double knockback[] = {300, 300, 350, 350, 400};
   private static final double cooldown[] = {2, 2, 2, 2, 2};
   private static final double speed[] = {100, 100, 100, 100, 100};

   /**
    * Create a new Fireball spell
    * @param owner
    */
   public Fireball(Warlock owner) {
      super(owner, "Fireball", SpellTarget.GROUND, SpellShortcut.LMB, 2);
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

   /**
    * Cast the spell.
    * @param castVector
    */
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

   /**
    * @return a "copy" of the spell
    */
   @Override
   public Fireball newInstance() {
      return new Fireball(null);
   }
}
