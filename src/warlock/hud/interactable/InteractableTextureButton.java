/**
 * File: warlock.hud.interactable.InteractableTextureButton.java
 *
 * A button with a texture. Similar to color button but without text and texture instead, crazy right?
 */
package warlock.hud.interactable;

import warlock.constant.ZLayers;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.resource.ResourceHandler;

public class InteractableTextureButton extends Interactable {

   private String texture;
   private Color highlight;
   private Color normal;
   private Color disabledColor = Color.DARK_GREY;
   private boolean disabled = false;

   /**
    * Create a textured interactable button
    * @param x
    * @param y
    * @param width
    * @param height
    * @param texture
    * @param normal
    * @param highlight
    */
   public InteractableTextureButton(int x, int y, int width, int height, String texture, Color normal, Color highlight) {
      super(x, y, width, height);
      this.texture = texture;
      this.highlight = highlight;
      this.normal = normal;
   }

   public InteractableTextureButton(int x, int y, int width, int height, String texture) {
      this(x, y, width, height, texture, Color.LIGHT_GREY, Color.WHITE);
   }

   /**
    * @return whether button is disabled or not
    */
   public boolean isDisabled() {
      return disabled;
   }

   /**
    * Set if the rendering is shaded as disabled or not
    * @param disabled
    */
   public void setDisabled(boolean disabled) {
      this.disabled = disabled;
   }

   @Override
   public void update(double dt) { /*Not needed*/ }

   /**
    * Render this textured button with correct color.
    * @param g
    */
   @Override
   public void render(Graphic g) {
      Color c = (!isDisabled()) ? (isMouseInside()) ? highlight : normal : disabledColor;

      g.drawTexture(ResourceHandler.getTexture(texture), getX() + (getWidth() / 2),
            getY() + (getHeight() / 2), ZLayers.GUI, getWidth(), getHeight(), 0, c);
   }
}
