/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.spell;

import java.util.Locale;
import warlock.graphic.Color;
import warlock.object.character.Warlock;
import warlock.phys.Vector;

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

   public int getSpellLevel() {
      return spellLevel;
   }

   public void levelUp() {
      if (spellLevel < maxLevel) {
         spellLevel++;
      }
      rebuildDescription();
   }

   public Warlock getOwner() {
      return owner;
   }

   public SpellType getType() {
      return type;
   }

   public void setType(SpellType type) {
      this.type = type;
   }

   public void setOwner(Warlock owner) {
      this.owner = owner;
   }

   public Spell(Warlock owner, String spellName, SpellTarget target, SpellShortcut shortcut, int maxLevel) {
      this.target = target;
      this.shortcut = shortcut;
      this.owner = owner;
      this.maxLevel = maxLevel;
      this.spellName = spellName;
   }

   public void setBaseDescription(String baseDescription) {
      this.baseDescription = baseDescription;
   }

   public void setLevelData(double[][] levelData) {
      this.levelData = levelData;
   }

   public void setLevelDataNames(String[] levelDataNames) {
      this.levelDataNames = levelDataNames;
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

   public String getSpellIcon() {
      return spellIcon;
   }

   public void setSpellIcon(String spellIcon) {
      this.spellIcon = spellIcon;
   }

   public abstract void cast(Vector castVector);

   public boolean canCast() {
      if (getCurrentCooldown() > 0) {
         return false; //later send notify message to HUD too, hence if statement
      }
      return true;
   }

   public void preCast() {
      setCurrentCooldown(getCooldown());
   }

   public void update(double dt) {
      if (getCurrentCooldown() > 0) {
         modifyCurrentCooldown(-dt);
      }
   }

   public String getShortcutAsString() {
      return (shortcut.toString());
   }

   public String getDescription() {
      return description;
   }

   public String unpreppedDecription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void rebuildDescription() {
      StringBuilder builder = new StringBuilder(getSpellName());
      builder.append('\n').append(baseDescription).append('\n').append('\n');
      Color currentLevel = Color.GOLD;
      Color otherLevels = Color.DARK_GREY;
      for (int i = 0; i < levelDataNames.length; i++) {
         builder.append(levelDataNames[i] + ": \n");
         for (int j = 0; j < levelData[i].length; j++) {
            if (j == getSpellLevel() - 1) {
               builder.append(currentLevel.toString());
            }
            else {
               builder.append(otherLevels.toString());
            }
            if (levelData[i][j] < 100) {
               builder.append(String.format(Locale.US, "%1$.1f", levelData[i][j]));
            }
            else {
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

   public int getMaxLevel() {
      return maxLevel;
   }

   public void setMaxLevel(int maxLevel) {
      this.maxLevel = maxLevel;
   }

   public abstract Spell clone();

   public String getSpellName() {
      return Color.GOLD + spellName + "|";
   }

   public void setSpellName(String spellName) {
      this.spellName = spellName;
   }

   public double getLevelData(String dataName) {
      for (int i = 0; i < levelDataNames.length; i++) {
         if (levelDataNames[i] == dataName) {
            return levelData[i][getSpellLevel() - 1];
         }
      }
      return 0;
   }
}
