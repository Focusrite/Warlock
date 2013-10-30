/**
 * File: warlock.hud.interactable.InteractableIntCounter.java
 *
 * A interactable object that counts, arrows are used to increase/decrease the value of this
 * interactable.
 */
package warlock.hud.interactable;

import warlock.font.Font;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.input.InputHandler;

public class InteractableIntCounter extends Interactable {

   private InteractableTextureButton increase;
   private InteractableTextureButton decrease;
   private InteractableTextField number;
   private int value, max, min, step;

   /**
    * Create an int counter
    *
    * @param defaultValue
    * @param minVal
    * @param maxVal
    * @param stepValue
    * @param x
    * @param y
    * @param width
    * @param height
    */
   public InteractableIntCounter(int defaultValue, int minVal, int maxVal, int stepValue, int x, int y, int width, int height) {
      super(x, y, width, height);
      value = defaultValue;
      this.max = maxVal;
      this.min = minVal;
      this.step = stepValue;
      decrease = new InteractableTextureButton(x, y, height, height, "ui-decrease", Color.LIGHT_GREY, Color.WHITE);
      decrease.addListener(new InteractableListener() {
         @Override
         public void clicked(InteractableInfo source) {
            if (value > min) {
               value -= step;
            }
            number.setText(value + "");
         }

         @Override
         public void mouseEntered(InteractableInfo source) {
         }

         @Override
         public void mouseExited(InteractableInfo source) {
         }
      });
      increase = new InteractableTextureButton(x + width - height, y, height, height, "ui-increase", Color.LIGHT_GREY, Color.WHITE);
      increase.addListener(new InteractableListener() {
         @Override
         public void clicked(InteractableInfo source) {
            if (value < max) {
               value += step;
            }
            number.setText(value + "");
         }

         @Override
         public void mouseEntered(InteractableInfo source) {
         }

         @Override
         public void mouseExited(InteractableInfo source) {
         }
      });
      number = new InteractableTextField(x, y, width, height, value + "", Font.SIZE_NORMAL, Color.DARK_GREY, Color.WHITE, true);
   }

   /**
    * @return the current value
    */
   public int getValue() {
      return value;
   }

   @Override
   public void update(double dt) { /*Update not needed*/ }

   /**
    * Render this interactable
    * @param g
    */
   @Override
   public void render(Graphic g) {
      increase.render(g);
      decrease.render(g);
      number.render(g);
   }

   /**
    * Handle the input of this interactable
    * @param input
    */
   @Override
   public void handleInput(InputHandler input) {
      increase.handleInput(input);
      decrease.handleInput(input);
      super.handleInput(input);
   }
}
