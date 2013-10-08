/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.object.character;

/**
 *
 * @author Focusrite
 */
public class StatusEffectType {
   private double magnitude;
   private AttributeType affectedType;
   private String iconPath;
   private boolean showIcon = false;

   public StatusEffectType(double magnitude, AttributeType affectedType) {
      this.magnitude = magnitude;
      this.affectedType = affectedType;
   }

   public boolean isShowIcon() {
      return showIcon;
   }

   public void setShowIcon(boolean showIcon) {
      this.showIcon = showIcon;
   }

   public AttributeType getAffectedType() {
      return affectedType;
   }

   public String getAffectedTag() {
      return getAffectedType().getTag();
   }

   public void setAffectedType(AttributeType affectedType) {
      this.affectedType = affectedType;
   }

   public double getMagnitude() {
      return magnitude;
   }

   public void setMagnitude(double magnitude) {
      this.magnitude = magnitude;
   }

   public String getIconPath() {
      return iconPath;
   }

   public void setIconPath(String iconPath) {
      this.iconPath = iconPath;
   }


}
