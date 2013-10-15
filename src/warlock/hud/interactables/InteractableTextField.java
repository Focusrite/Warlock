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
 *
 * @author Focusrite
 */
public class InteractableTextField extends Interactable {

   private String text;
   private Color color;
   private Color textColor;
   private int fontSize;
   private boolean centered;

   public InteractableTextField(int x, int y, int width, int height, String text, int fontSize,
      Color color, Color textColor, boolean centered) {
      super(x, y, width, height);
      this.text = text;
      this.color = color;
      this.textColor = textColor;
      this.fontSize = fontSize;
      this.centered = centered;
   }

   public String getText() {
      return text;
   }

   public void setText(String text) {
      this.text = text;
   }

   @Override
   public void update(double dt) {
   }

   @Override
   public void render(Graphic g) {
      if (centered) {
         g.drawText(Font.STYLE_NORMAL, getX() + (getWidth() / 2), getY() + getHeight(),
            ZLayers.GUI_FOREGROUND, text, fontSize, textColor, centered);
      }
      else {
         g.drawText(Font.STYLE_NORMAL, getX(), getY() + getHeight(), ZLayers.GUI_FOREGROUND,
            text, fontSize, textColor, centered);
      }
      g.drawRectangle(getX() + (getWidth() / 2), getY() + (getHeight() / 2), ZLayers.GUI, getWidth(), getHeight(), 0, color);
   }

   @Override
   public void notifyClicked(int x, int y) {
   }
}
