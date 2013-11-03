/**
 * File: warlock.spell.Fireball.java
 *
 * A projectile spell that fires a self-steering projectile that seeks out the closest target.
 */
package warlock.spell;

import warlock.object.projectile.OrbProjectile;
import warlock.object.warlock.Warlock;
import warlock.phys.Vector;

/**
 *
 * @author Focusrite
 */
public class Seekerorb extends Spell {
   private static final double range[] = {600, 700, 700, 800, 800};
   private static final double knockback[] = {50, 50, 100, 100, 150};
   private static final double cooldown[] = {15, 15, 15, 15, 15};
   private static final double speed[] = {150, 150, 150, 150, 200};
   private static final double damage[] = {10, 12, 15, 17, 20};

   /**
    * Create a new Explosion spell
    * @param owner
    */
   public Seekerorb(Warlock owner) {
      super(owner, "Seeker Orb", SpellTarget.GROUND, SpellShortcut.Q, 5);
      setCooldown(cooldown[0]);
      setSpellIcon("spell-orb");
      setMaxLevel(5);
      setType(SpellType.PROJECTILE);

      setBaseDescription(
           "A glowing ball of \n"
         + "something seeking out\n"
         + "your foes. They'll never\n"
         + "suspect the orb is evil\n");
      setLevelDataNames(new String[]{"Damage", "Knockback", "Range", "Speed", "Cooldown"});
      setLevelData(new double[][]{damage, knockback, range, speed, cooldown});
      rebuildDescription();
   }

   /**
    * Cast the spell.
    * @param castVector
    */
   @Override
   public void cast(Vector castVector) {
      OrbProjectile projectile = new OrbProjectile(castVector.getAngle(),
         speed[getSpellLevel() - 1],
         range[getSpellLevel() - 1],
         (int)damage[getSpellLevel() - 1],
         knockback[getSpellLevel() - 1]);
      projectile.setPosition(getOwner().getX(), getOwner().getY());
      getOwner().getLevel().addObject(projectile, getOwner().getOwningPlayer());
   }

   /**
    * @return a "copy" of the spell
    */
   @Override
   public Seekerorb clone() {
      return new Seekerorb(null);
   }

}
