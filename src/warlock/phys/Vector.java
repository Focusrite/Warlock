/**
 * File: warlock.phys.Vector.java
 *
 * Simply a vector
 */
package warlock.phys;

public class Vector {

   private double x = 0;
   private double y = 0;
   private double angle = 0;
   private double length = 0;

   /**
    * @return the x component of the vector
    */
   public double getX() {
      return x;
   }

   /**
    * @param x new x component value
    */
   public void setX(double x) {
      this.x = x;
      this.updatePolar();
   }

   /**
    * @return the y component of the vector
    */
   public double getY() {
      return y;
   }

   /**
    * @param y new y component value
    */
   public void setY(double y) {
      this.y = y;
      this.updatePolar();
   }

   /**
    * Set the coordinates in cartesian form
    * @param x
    * @param y
    */
   public void setCoordinates(double x, double y) {
      this.x = x;
      this.y = y;
      this.updatePolar();
   }

   /**
    * @return the angle of the vector
    */
   public double getAngle() {
      return angle;
   }

   /**
    * @return length of the vector
    */
   public double getLength() {
      return length;
   }

   /**
    * @param length new length
    */
   public void setLength(double length) {
      this.length = length;
      this.updateCartesian();
   }

   /**
    * Creates a copy of the vector and scales it by c, returning the result
    * @param c
    * @return resulting vector
    */
   public Vector scale(double c) {
      Vector t = new Vector(this.getLength(), this.getAngle());
      t.setLength(t.getLength() * c);
      return t;
   }

   /**
    * @param angle new angle in radians
    */
   public final void setAngle(double angle) {
      angle = (angle) % (2 * Math.PI);
      angle = (angle >= 0) ? angle : (2 * Math.PI) + angle;
      this.angle = angle;
      this.updateCartesian();
   }

   /**
    * @param d angle in radian to rotate with
    */
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

   /**
    * Copy constructor
    * @param v
    */
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

   /**
    * Get the difference between two vector
    * @param v
    * @return the difference vector
    */
   public Vector subtract(Vector v) {
      return create(x - v.x, y - v.y);
   }

   /**
    * @param v to compare with
    * @return the distance between this vector and v
    */
   public double distance(Vector v) {
      return subtract(v).getLength();
   }

   /**
    * The dot product of this vector and v
    * @param v
    * @return the dot product
    */
   public double dotProduct(Vector v) {
      return (x * v.x) + (y * v.y);
   }

   /**
    * @param v
    * @return the angle between this vector and v
    */
   public double angleBetween(Vector v) {
      return (getAngle() - v.getAngle()) % Math.PI;
   }
}
