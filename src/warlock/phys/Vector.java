/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.phys;

/**
 *
 * @author Focusrite
 */
public class Vector {

   /*
    * Members
    */
   private double x = 0;
   private double y = 0;
   private double angle = 0;
   private double length = 0;

   @Override
   public int hashCode() {
      int hash = 7;
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
      final Vector other = (Vector) obj;
      if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
         return false;
      }
      if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
         return false;
      }
      return true;
   }

   /*
    * Getters and setters
    */
   public double getX() {
      return x;
   }

   public void setX(double x) {
      this.x = x;
      this.updatePolar();
   }

   public double getY() {
      return y;
   }

   public void setY(double y) {
      this.y = y;
      this.updatePolar();
   }

   public void setCoordinates(double x, double y) {
      this.x = x;
      this.y = y;
      this.updatePolar();
   }

   public double getAngle() {
      return angle;
   }

   public double getLength() {
      return length;
   }

   public void setLength(double length) {
      this.length = length;
      this.updateCartesian();
   }

   public Vector scale(double c) {
      Vector t = new Vector(this.getLength(), this.getAngle());
      t.setLength(t.getLength() * c);
      return t;
   }

   public final void setAngle(double angle) {
      angle = (angle) % (2 * Math.PI);
      angle = (angle >= 0) ? angle : (2 * Math.PI) + angle;
      this.angle = angle;
      this.updateCartesian();
   }

   public void rotate(double d) {
      setAngle(this.angle + d);
   }

   /**
    * Constructor for class Vector, takes polar coordinates. Use static method create if values on
    * cartesian form are desired instead.
    *
    * @param length length
    * @param angle angle in radians
    * @return resulting Vector
    */
   public Vector(double length, double angle) {
      this.length = length;
      setAngle(angle);
      this.updateCartesian();
   }

   /**
    * Empty constructor
    */
   public Vector() {
   }

   public Vector(Vector v) {
      this(v.length, v.angle);
   }

   /**
    * Functions simiar to the constructor but takes the values in cartesian form instead
    *
    * @param x
    * @param y
    * @return resulting Vector
    */
   public static Vector create(double x, double y) {
      Vector v = new Vector();
      v.setCoordinates(x, y);
      return v;
   }

   /**
    * Private function updating polar values when changing in cartesian form
    */
   public final void updatePolar() {
      length = Math.sqrt((x * x) + (y * y));
      angle = Math.atan2(y, x);
      if (angle < 0) {
         angle = (2 * Math.PI) + angle;
      }
   }

   /**
    * Private function updating cartesian values when changing in polar form
    */
   public final void updateCartesian() {
      x = Math.cos(angle) * length;
      y = Math.sin(angle) * length;
   }

   /**
    * Add a vector to this and return result
    *
    * @param v Vector to add $return Vector
    */
   public Vector add(Vector v) {
      return create(x + v.x, y + v.y);
   }

   public Vector subtract(Vector v) {
      return create(x - v.x, y - v.y);
   }

   public double distance(Vector v) {
      return subtract(v).getLength();
   }

   public double dotProduct(Vector v) {
      return (x * v.x) + (y * v.y);
   }

   public double angleBetween(Vector v) {
      return (getAngle() - v.getAngle()) % Math.PI;
   }
}
