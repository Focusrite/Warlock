/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.phys;

/**
 *
 * @author Focusrite
 */
public class Force extends Vector {

   private double inertia = 3.0; //Force scaling per radian

   public double getInertia() {
      return inertia;
   }

   public void setInertia(double inertia) {
      this.inertia = inertia;
   }

   public Force(double len, double ang) {
      super(len, ang);
   }

   public Force(double len, double ang, double inertia) {
      super(len, ang);
      this.inertia = inertia;
   }

   public Force(Force f) {
      this(f.getLength(), f.getAngle(), f.getInertia());
   }
   public Force(Vector v) {
      this(v.getLength(), v.getAngle(), 1);
   }
   public Force() {}


   public Force add(Force f) {
//      double aDif = Math.abs((this.getAngle() - f.getAngle()) % Math.PI);
//      double iScale = 1 / (aDif * getInertia());

      Vector t = new Vector(f.getLength(), f.getAngle());
      return this.add(t);
   }

   @Override
   public Force scale(double c) {
      Force t = new Force(this.getLength(), this.getAngle(), this.getInertia());
      t.setLength(t.getLength() * c);
      return t;
   }

   @Override
   public Force add(Vector v){
      Force t = new Force(v);
      t.setX(getX() + v.getX());
      t.setY(getY() + v.getY());
      t.updatePolar();
      return t;
   }
}
