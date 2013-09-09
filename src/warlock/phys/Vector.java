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

   public void setAngle(double angle) {
      this.angle = angle;
      this.updateCartesian();
   }

   public void rotate(double d)
   {
      setAngle(this.angle + d);
   }

   /**
    * Constructor for class Vector, takes cartesian polar coordinates. Use static method create if values
    * on cartesian form are desired instead.
    *
    * @param x
    * @param y
    */
   public Vector(double len, double ang) {
      this.length = len;
      this.angle = ang;
      this.updateCartesian();
   }

   /**
    * Empty constructor
    */
   public Vector(){ }

   /**
    * Functions simiar to the constructor but takes the values in polar form instead
    * @param angle in radians
    * @param length
    * @return resulting Vector
    */
   public static Vector create(double x, double y){
      Vector v = new Vector();
      v.x = x;
      v.y = y;
      v.updatePolar();
      return v;
   }

   /**
    * Private function updating polar values when changing in cartesian form
    */
   public void updatePolar() {
      length = Math.sqrt(x*x + y*y);
      angle = Math.atan2(x, y);
   }

   /**
    * Private function updating cartesian values when changing in polar form
    */
   public void updateCartesian() {
      x = Math.cos(angle) * length;
      y = Math.sin(angle) * length;
   }

   /**
    * Add a vector to this and return result
    * @param v Vector to add
    * $return Vector
    */
   public Vector add(Vector v){
      Vector t = new Vector(v.getLength(), v.getAngle());
      t.x = t.x + v.x;
      t.y = t.y + v.y;
      t.updatePolar();
      return t;
   }

}
