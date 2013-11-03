/**
 * File: warlock.spell.Lightning.java
 *
 * A fast moving projectile spell with no damage but great knockback.
 */
package warlock.spell;

import warlock.object.projectile.LightningProjectile;
import warlock.object.warlock.Warlock;
import warlock.phys.Vector;

/**
 *
 * @author Focusrite
 */
public class Lightning extends Spell {
   private static final double range[] = {300, 300, 350, 350, 400};
   private static final double knockback[] = {400, 450, 500, 550, 600};
   private static final double cooldown[] = {10, 10, 10, 10, 10};
   private static final double speed[] = {300, 300, 300, 300, 300};

   /**
    * Create a new Lightning spell
    * @param owner
    */
   public Lightning(Warlock owner) {
      super(owner, "Lightning", SpellTarget.GROUND, SpellShortcut.Q, 5);
      setCooldown(cooldown[0]);
      setSpellIcon("spell-lightning");
      setMaxLevel(5);
      setType(SpellType.PROJECTILE);

      setBaseDescription(
           "The brilliant warlock \n"
         + "Mah'zul invented this\n"
         + "spell. Unfortunately\n"
         + "he was struck by\n"
         + "lightning a week later\n");
      setLevelDataNames(new String[]{"Knockback", "Range", "Speed", "Cooldown"});
      setLevelData(new double[][]{knockback, range, speed, cooldown});
      rebuildDescription();
   }

   /**
    * Cast the spell.
    * @param castVector
    */
   @Override
   public void cast(Vector castVector) {
      LightningProjectile projectile = new LightningProjectile(castVector.getAngle(),
         speed[getSpellLevel() - 1],
         range[getSpellLevel() - 1],
         0,
         knockback[getSpellLevel() - 1]);
      projectile.setPosition(getOwner().getX(), getOwner().getY());
      getOwner().getLevel().addObject(projectile, getOwner().getOwningPlayer());
   }

   /**
    * @return a "copy" of the spell
    */
   @Override
   public Lightning clone() {
      return new Lightning(null);
   }

}
