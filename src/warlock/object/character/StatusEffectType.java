/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.object.character;

/**
 *
 * @author Focusrite
 */
class StatusEffectType {
   private int magnitude;
   private AttributeType affectedType;
   private String iconPath;
   private boolean showIcon = false;

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

   public int getMagnitude() {
      return magnitude;
   }

   public void setMagnitude(int magnitude) {
      this.magnitude = magnitude;
   }

   public String getIconPath() {
      return iconPath;
   }

   public void setIconPath(String iconPath) {
      this.iconPath = iconPath;
   }


}
