/**
 * File: warlock.hud.interactable.InteractableTextField.java
 *
 * A simple text field displaying some text. It's not interactable, I'm a liar like that.
 */
package warlock.hud.interactable;

import warlock.constant.ZLayers;
import warlock.font.Font;
import warlock.graphic.Color;
import warlock.graphic.Graphic;

public class InteractableTextField extends Interactable {

   private String text;
   private Color color;
   private Color textColor;
   private int fontSize;
   private boolean centered;

   /**
    * Creates a text field interactable.
    *
    * @param x
    * @param y
    * @param width
    * @param height
    * @param text
    * @param fontSize
    * @param color
    * @param textColor
    * @param centered
    */
   public InteractableTextField(int x, int y, int width, int height, String text, int fontSize,
      Color color, Color textColor, boolean centered) {
      super(x, y, width, height);
      this.text = text;
      this.color = color;
      this.textColor = textColor;
      this.fontSize = fontSize;
      this.centered = centered;
   }

   /**
    * @return the text of this text field
    */
   public String getText() {
      return text;
   }

   /**
    * @param text the new text of this text field
    */
   public void setText(String text) {
      this.text = text;
   }

   @Override
   public void update(double dt) { /*Update not needed*/ }

   /**
    * Render the text field
    * @param g
    */
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

   /**
    * Clicked not needed for this kind of text field
    * @param x
    * @param y
    */
   @Override
   public void notifyClicked(int x, int y) { }
}
