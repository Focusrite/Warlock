/**
 * File: warlock.spell.Spell.java
 *
 * The abstract class which all other spells extends. The cast method is the main difference between
 * them apart from the obvious things that differs such as name, description, icons and similar.
 */
package warlock.spell;

import java.util.Locale;
import warlock.graphic.Color;
import warlock.object.warlock.Warlock;
import warlock.phys.Vector;
import warlock.state.Updateable;

/**
 *
 * @author Focusrite
 */
public abstract class Spell implements Updateable {

   private SpellTarget target;
   private SpellShortcut shortcut;
   private Warlock owner;
   private double cooldown;
   private double currentCooldown = 0;
   private String spellIcon;
   private String description;
   private String spellName;
   private SpellType type; //used for ai
   private int spellLevel = 1;
   private int maxLevel;
   //Description building
   private String baseDescription;
   private double[][] levelData;
   private String[] levelDataNames;

   /**
    * @return the level of the spell
    */
   public int getSpellLevel() {
      return spellLevel;
   }

   /**
    * Level up the skill if possible, and update the description to reflect new level
    */
   public void levelUp() {
      if (spellLevel < maxLevel) {
         spellLevel++;
      }
      rebuildDescription();
   }

   /**
    * @return owner of the spell
    */
   public Warlock getOwner() {
      return owner;
   }

   /**
    * @return spell type (mainly used for AI)
    */
   public SpellType getType() {
      return type;
   }

   /**
    * @param type new spell type
    */
   public void setType(SpellType type) {
      this.type = type;
   }

   /**
    * @param owner new owner of this spell
    */
   public void setOwner(Warlock owner) {
      this.owner = owner;
   }

   /**
    * Create a new spell
    * @param owner
    * @param spellName
    * @param target
    * @param shortcut
    * @param maxLevel
    */
   public Spell(Warlock owner, String spellName, SpellTarget target, SpellShortcut shortcut, int maxLevel) {
      this.target = target;
      this.shortcut = shortcut;
      this.owner = owner;
      this.maxLevel = maxLevel;
      this.spellName = spellName;
   }

   /**
    * Set base description, the part that doesn't change upon level up
    * @param baseDescription
    */
   public void setBaseDescription(String baseDescription) {
      this.baseDescription = baseDescription;
   }

   /**
    * Register the level data. The 2d array is in form [data type][level]. Data type corresponds
    * to the "level data names" and the "level" reflects what the data is each level.
    * @param levelData
    */
   public void setLevelData(double[][] levelData) {
      this.levelData = levelData;
   }

   /**
    * The names of the datas registered with setLevelData. Should equal length as levelData[], ie
    * excluding the level part of the 2d array.
    * @param levelDataNames
    */
   public void setLevelDataNames(String[] levelDataNames) {
      this.levelDataNames = levelDataNames;
   }

   /**
    * @return the cooldown of the spell when used
    */
   public double getCooldown() {
      return cooldown;
   }

   /**
    * @param cooldown new cooldown when used
    */
   public void setCooldown(double cooldown) {
      this.cooldown = cooldown;
   }

   /**
    * @return current time left of cooldown
    */
   public double getCurrentCooldown() {
      return currentCooldown;
   }

   /**
    * @param currentCooldown new current time left of cooldown
    */
   public void setCurrentCooldown(double currentCooldown) {
      this.currentCooldown = currentCooldown;
   }

   /**
    * @param dCurrentCooldown value to add to current cooldown
    */
   public void modifyCurrentCooldown(double dCurrentCooldown) {
      this.currentCooldown += dCurrentCooldown;
   }

   /**
    * @return this spells shortcut enumeration
    */
   public SpellShortcut getShortcut() {
      return shortcut;
   }

   /**
    * @param shortcut new shortcut
    */
   public void setShortcut(SpellShortcut shortcut) {
      this.shortcut = shortcut;
   }

   /**
    * @return this spells target type
    */
   public SpellTarget getTarget() {
      return target;
   }

   /**
    * @param target new spell target type
    */
   public void setTarget(SpellTarget target) {
      this.target = target;
   }

   /**
    * @return spell icon resource name
    */
   public String getSpellIcon() {
      return spellIcon;
   }

   /**
    * @param spellIcon new spell icon resource name
    */
   public void setSpellIcon(String spellIcon) {
      this.spellIcon = spellIcon;
   }

   public abstract void cast(Vector castVector);

   /**
    * @return whether this spell can be cast or is on cooldown
    */
   public boolean canCast() {
      return getCurrentCooldown() <= 0;
   }

   /**
    * Spell is cast, begin cooldown countdown
    */
   public void preCast() {
      setCurrentCooldown(getCooldown());
   }

   /**
    * Decrease current cooldown if it's on it
    * @param dt
    */
   @Override
   public void update(double dt) {
      if (getCurrentCooldown() > 0) {
         modifyCurrentCooldown(-dt);
      }
   }

   /**
    * @return the shortcut as string
    */
   public String getShortcutAsString() {
      return (shortcut.toString());
   }

   /**
    * @return the description text
    */
   public String getDescription() {
      return description;
   }

   /**
    * @param description new description
    */
   public void setDescription(String description) {
      this.description = description;
   }

   /**
    * Rebuild a description with base description as pretext and the spell data beneath with the
    * current level highlighted.
    */
   public void rebuildDescription() {
      StringBuilder builder = new StringBuilder(getSpellName()).append(" lvl ").append(getSpellLevel());
      builder.append('\n').append(baseDescription).append('\n').append('\n');
      Color currentLevel = Color.GOLD;
      Color otherLevels = Color.DARK_GREY;
      for (int i = 0; i < levelDataNames.length; i++) {
         builder.append(levelDataNames[i]).append(": \n");
         for (int j = 0; j < levelData[i].length; j++) {
            if (j == getSpellLevel() - 1) {
               builder.append(currentLevel.toString());
            }
            else {
               builder.append(otherLevels.toString());
            }
            if (levelData[i][j] < 100) { //Sub 100, show one decimal
               builder.append(String.format(Locale.US, "%1$.1f", levelData[i][j]));
            }
            else { //No decimals
               builder.append(String.format(Locale.US, "%1$.0f", levelData[i][j]));
            }
            builder.append("|");
            if (j + 1 < levelData[i].length) {
               builder.append("/");
            }
         }
         builder.append('\n');
      }
      setDescription(builder.toString());
   }

   /**
    * @return max level of this spell
    */
   public int getMaxLevel() {
      return maxLevel;
   }

   /**
    * @param maxLevel the new max level
    */
   public void setMaxLevel(int maxLevel) {
      this.maxLevel = maxLevel;
   }

   @Override
   public abstract Spell clone();

   /**
    * @return return the spell name colored
    */
   public String getSpellName() {
      return Color.GOLD + spellName + "|";
   }

   /**
    * @param spellName new spell name
    */
   public void setSpellName(String spellName) {
      this.spellName = spellName;
   }

   /**
    * Get the data of name dataName of the current level
    * @param dataName
    * @return value if found else 0
    */
   public double getLevelData(String dataName) {
      for (int i = 0; i < levelDataNames.length; i++) {
         if (levelDataNames[i].equals(dataName)) {
            return levelData[i][getSpellLevel() - 1];
         }
      }
      return 0;
   }
}
