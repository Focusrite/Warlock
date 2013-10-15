/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.spell;

import warlock.object.character.AttributeHandler;
import warlock.object.character.StatusEffectType;
import warlock.object.character.Warlock;
import warlock.phys.Vector;

/**
 *
 * @author Focusrite
 */
public class Shield extends Spell {

   private static final double cooldown[] = {10, 9, 8, 7, 6};
   private static final double duration[] = {0.5, 0.7, 0.9, 1.1, 1.3};
   private static final double maxReflects[] = {1, 1, 2, 2, 3};

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

   @Override
   public void levelUp() {
      super.levelUp();
      setCooldown(cooldown[getSpellLevel() - 1]);
   }

   @Override
   public void cast(Vector castVector) {
      for (int i = 0; i < maxReflects[getSpellLevel() - 1]; i++) {
         getOwner().inflictStatusEffect(
            new StatusEffectType(-1, AttributeHandler.get("shield")), duration[getSpellLevel() - 1]);
      }
   }

   @Override
   public Shield clone() {
      Shield spell = new Shield(null);
      return spell;
   }
}
