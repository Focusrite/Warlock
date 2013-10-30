/**
 * File: warlock.attribute.Attribute.java
 *
 * A type of status effect, used to setup what attribute is being targeted, and should later be used
 * to display buff indicator and is currently somewhat redundant.
 */
package warlock.attribute;

public class StatusEffectType {
   private double magnitude;
   private AttributeType affectedType;

   /**
    * Create a new status effect type
    *
    * @param magnitude
    * @param affectedType
    */
   public StatusEffectType(double magnitude, AttributeType affectedType) {
      this.magnitude = magnitude;
      this.affectedType = affectedType;
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

}
