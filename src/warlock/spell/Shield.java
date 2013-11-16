/**
 * File: warlock.spell.Shield.java
 *
 * A spell that provides a brief shield that reflects any projectile spell back the way it came as
 * well as claims the ownership of it.
 */

package warlock.spell;

import warlock.attribute.AttributeHandler;
import warlock.attribute.StatusEffectType;
import warlock.object.warlock.Warlock;
import warlock.phys.Vector;

/**
 *
 * @author Focusrite
 */
public class Shield extends Spell {

   private static final double cooldown[] = {10, 10, 10, 10, 8};
   private static final double duration[] = {1, 1.3, 1.6, 1.9, 2.2};
   private static final double maxReflects[] = {1, 1, 2, 2, 2};

   /**
    * Create a new Shield spell
    * @param owner
    */
   public Shield(Warlock owner) {
      super(owner, "Shield", SpellTarget.SELF, SpellShortcut.E, 5);
      setCooldown(cooldown[0]);
      setSpellIcon("spell-shield");
      setMaxLevel(5);
      setType(SpellType.DEFENSIVE);

      setBaseDescription(
         "Some Warlocks are \n"
         + "simply just cowards.\n"
         + "Reflects projectiles\n"
         + "back the opposite way. \n");
      setLevelDataNames(new String[]{"Duration", "Max reflects", "Cooldown"});
      setLevelData(new double[][]{duration, maxReflects, cooldown});
      rebuildDescription();
   }

   /**
    * Override the levelup to lower the cooldown
    */
   @Override
   public void levelUp() {
      super.levelUp();
      setCooldown(cooldown[getSpellLevel() - 1]);
   }

   /**
    * Cast the spell.
    * @param castVector
    */
   @Override
   public void cast(Vector castVector) {
      for (int i = 0; i < maxReflects[getSpellLevel() - 1]; i++) {
         getOwner().inflictStatusEffect(
            new StatusEffectType(-1, AttributeHandler.get("shield")), duration[getSpellLevel() - 1]);
      }
   }

   /**
    * @return a "copy" of the spell
    */
   @Override
   public Shield newInstance() {
      return new Shield(null);
   }
}
