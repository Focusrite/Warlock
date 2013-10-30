/**
 * File: warlock.hud.interactable.InteractableColorButton.java
 *
 * A colored button with some text displayed in the middle, no loger used, but still remains since
 * it's potentially useful in the future.
 */
package warlock.hud.interactable;

import warlock.constant.ZLayers;
import warlock.font.Font;
import warlock.graphic.Color;
import warlock.graphic.Graphic;

public class InteractableColorButton extends Interactable {
   private String text;
   private Color color;
   private Color highlight;
   private Color textColor;
   private int fontSize;

   /**
    * Create a single colored button.
    *
    * @param x
    * @param y
    * @param width
    * @param height
    * @param text
    * @param fontSize
    * @param color
    * @param highlight
    * @param textColor
    */
   public InteractableColorButton(int x, int y, int width, int height, String text, int fontSize,
      Color color, Color highlight, Color textColor) {
      super(x, y, width, height);
      this.text = text;
      this.color = color;
      this.textColor = textColor;
      this.highlight = highlight;
      this.fontSize = fontSize;
   }

   @Override
   public void update(double dt) { /* Need no updating */}

   /**
    * Render the interactable
    * @param g
    */
   @Override
   public void render(Graphic g) {
      g.drawText(Font.STYLE_NORMAL, getX() + (getWidth() / 2), getY() + getHeight(), ZLayers.GUI_FOREGROUND, text, fontSize, textColor, true);
      if (isMouseInside()) {
         g.drawRectangle(getX() + (getWidth() / 2), getY() + (getHeight() / 2), ZLayers.GUI, getWidth(), getHeight(), 0, highlight);
      }
      else {
         g.drawRectangle(getX() + (getWidth() / 2), getY() + (getHeight() / 2), ZLayers.GUI, getWidth(), getHeight(), 0, color);
      }
   }

}
