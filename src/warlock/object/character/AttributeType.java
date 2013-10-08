/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.object.character;

import java.util.Objects;

/**
 * Attributes to add, all in range
 * _Name___________Tag______Effect__________________________________________________________________
 * Hitpoints      hp       Dead when 0
 * Push           push     Knockback force magnitude
 * Damage         dmg      Hit damage scale
 * Acceleration   acc      Speed of reaching character MS
 * Movespeed      ms       Top speed
 * Turn rate      turn     Speed of turning to face angle
 */

/**
 *
 * @author Focusrite
 */
public class AttributeType {

   private String name;
   private String tooltip;
   private String tag;
   private String iconPath;
   private double defaultValue;

   public double getDefaultValue() {
      return defaultValue;
   }

   public AttributeType(String id, String name, String tooltip, double defaultValue) {
      this.tag = id;
      this.name = name;
      this.tooltip = tooltip;
      this.defaultValue = defaultValue;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getTooltip() {
      return tooltip;
   }

   public void setTooltip(String tooltip) {
      this.tooltip = tooltip;
   }

   public String getTag() {
      return this.tag;
   }

   @Override
   public int hashCode() {
      int hash = 3;
      hash = 43 * hash + Objects.hashCode(this.name);
      hash = 43 * hash + Objects.hashCode(this.tag);
      return hash;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final AttributeType other = (AttributeType) obj;
      if (!Objects.equals(this.name, other.name)) {
         return false;
      }
      if (!Objects.equals(this.tag, other.tag)) {
         return false;
      }
      return true;
   }
}
