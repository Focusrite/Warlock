/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.hud.interactables;

import warlock.constant.ZLayers;
import warlock.font.Font;
import warlock.graphic.Color;
import warlock.graphic.Graphic;

/**
 * Maybe make strategy pattern
 * @author Focusrite
 */
public class InteractableColorButton extends Interactable {
   private String text;
   private Color color;
   private Color highlight;
   private Color textColor;
   private int fontSize;

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
   public void update(double dt) {

   }

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
