/**
 * File: warlock.attribute.Attribute.java
 *
 * An individual attribute that a warlock has. Essentially an "instance of" an attribute type.
 */
package warlock.attribute;

import java.util.Objects;

public class Attribute {
   private AttributeType attrType;
   private double baseValue;  //Base
   private double value;      //Current



   /**
    * Create a new individual instance of an attribute a warlock can have.
    *
    * @param attrType AttributeType
    * @param value the starting value the attribute begins with
    * @param value current valeu
    */
   public Attribute(AttributeType attrType, double baseValue, double value) {
      this.attrType = attrType;
      this.baseValue = baseValue;
      this.value = value;
   }

   public Attribute(AttributeType attrType, double value) {
      this(attrType, value, value);
   }

   /**
    * @return AttributeType of this attribute
    */
   public AttributeType getAttributeType() {
      return attrType;
   }

   /**
    * @return String, the id tag of attributeType
    */
   public String getAttributeTag() {
      return getAttributeType().getTag();
   }

   /**
    * @return double, base value (starting value)
    */
   public double getBaseValue() {
      return baseValue;
   }

   /**
    * @param baseValue new value
    */
   public void setBaseValue(int baseValue) {
      this.baseValue = baseValue;
   }

   /**
    * @return double, current attribute value
    */
   public double getValue() {
      return value;
   }

   /**
    * @param value new Value
    */
   public void setValue(double value) {
      this.value = value;
   }

   /**
    * @param dv value change. Added to current value.
    */
   public void modValue(double dv){
      this.value = this.value + dv;
   }

}
