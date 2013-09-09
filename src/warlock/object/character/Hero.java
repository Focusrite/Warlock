/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.object.character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import warlock.graphic.Graphic;
import warlock.input.InputHandler;
import warlock.object.LevelObject;
import warlock.phys.Force;
import warlock.time.Misc;

/**
 *
 * @author Focusrite
 */
public class Hero extends LevelObject {

   private Map<String, Attribute> attributes = new HashMap<String, Attribute>();
   private ArrayList<StatusEffect> statusEffects = new ArrayList<StatusEffect>();
   private Force movement; //The dMovement done per sec
   private double facingAngle = 0;

   public Hero() {
      movement = new Force();
   }

   private void initAttr() {
      //do-loadfromfile at game-load, then fetch what attrs was loaded here
      for (int i = 0; i < 10; i++) {
         AttributeType attrtype = new AttributeType("Tag" + i, "Name" + i, "Description");
         this.attributes.put(attrtype.getTag(), new Attribute(attrtype, 5));
      }
   }

   public void init() {
      this.initAttr();
   }

   /**
    * I miss __call magic functions, so much neater :C
    *
    * @param name
    * @return Attribute if exist, or null if not available
    */
   public Attribute attr(String name) {
      return this.attributes.get(name);
   }

   public int attrVal(String name) {
      Attribute attr = attr(name);
      return (attr == null) ? null : attr.getValue();
   }

   public int attrBaseVal(String name) {
      Attribute attr = attr(name);
      return (attr == null) ? null : attr.getBaseValue();
   }

   public void setPosition(int x, int y) {
      super.getPosition().setX(x);
      super.getPosition().setY(y);
   }

   public Force getMovement() {
      return movement;
   }

   public void setMovement(Force movement) {
      this.movement = movement;
   }

   public void inflictStatusEffect(StatusEffectType type, int secondsLasting, Hero inflicter) {
      StatusEffect effect = StatusEffect.newInstance(this.attr(type.getAffectedTag()), type);
      effect.setInflicter(inflicter).setTemporary((secondsLasting > 0)).
         setExprStamp((secondsLasting > 0) ? Misc.relativetime(secondsLasting) : 0);

      effect.onset(); //"start" status effect
      statusEffects.add(effect);
   }

   public void inflictStatusEffect(StatusEffectType type, Hero inflicter) {
      inflictStatusEffect(type, 0, inflicter);
   }

   public void inflictStatusEffect(StatusEffectType type, int secondsLasting) {
      inflictStatusEffect(type, secondsLasting, null);
   }

   private void updateStatusEffects(double dt) {
      for (StatusEffect e : statusEffects) {
         if (e.isTemporary() && e.hasExpired()) {
            e.expire();
            statusEffects.remove(e);
         }
      }
   }

   public void updateMovement(double dt) {
      setPosition(getMovement().add(getPosition()).scale(dt));
      System.out.println("x: " + getPosition().getX() + ", y: " + getPosition().getY());
   }

   @Override
   public void update(double dt) {
      this.updateStatusEffects(dt);
      this.updateMovement(dt);
   }

   @Override
   public void handleInput(double dt, InputHandler input) {
      if (input.isMouseDown(0)) {
         steer(input.getMouseAngle(), dt);
      }
   }

   public void steer(double angle, double dt) {
      double accel = 5;//attrVal("acc");
      setMovement(getMovement().add(new Force(accel, angle).scale(dt)));
      System.out.println("x: " + getMovement().getX() + ", y: " + getMovement().getY());
   }

   @Override
   public void render(Graphic g) {
      //draw hero
      g.drawSquare((int) getPosition().getX() - 10, (int) getPosition().getY() - 10, 20, 20);
   }
}
