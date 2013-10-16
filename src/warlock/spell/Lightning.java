/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.spell;

import warlock.object.character.Warlock;
import warlock.object.projectile.LightningProjectile;
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

   @Override
   public Lightning clone() {
      Lightning spell = new Lightning(null);
      return spell;
   }

}
