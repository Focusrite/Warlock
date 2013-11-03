/**
 * File: warlock.attribute.AttributeType.java
 *
 * The types of attributes that are available to a warlock.
 */
package warlock.attribute;

import java.util.Objects;

public class AttributeType {

   private String name;
   private String tooltip;
   private String tag;
   private double defaultValue;

   /**
    * @return double, the default value the attribute type is set to have
    */
   public double getDefaultValue() {
      return defaultValue;
   }

   /**
    * Create a new AttributeType.
    *
    * @param id the tag the attribute is fetched with.
    * @param name name of the attribute
    * @param tooltip tooltip of the attribute with description what it does
    * @param defaultValue default value of the attribute
    */
   public AttributeType(String id, String name, String tooltip, double defaultValue) {
      this.tag = id;
      this.name = name;
      this.tooltip = tooltip;
      this.defaultValue = defaultValue;
   }

   /**
    * @return String, the name
    */
   public String getName() {
      return name;
   }

   /**
    * @param name the new name
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * @return String, the tooltip
    */
   public String getTooltip() {
      return tooltip;
   }

   /**
    * @param tooltip, the new tooltip text
    */
   public void setTooltip(String tooltip) {
      this.tooltip = tooltip;
   }

   /**
    * @return String, the tag of this AttributeType
    */
   public String getTag() {
      return this.tag;
   }

   /*
    * For use with the AttributeHandler's hashmap
    */
   
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
      return Objects.equals(this.tag, other.tag);
   }
}
