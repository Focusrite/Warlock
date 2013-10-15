/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.hud.interactables;

import warlock.constant.ZLayers;
import warlock.graphic.Color;
import warlock.graphic.Graphic;
import warlock.resource.ResourceManager;

/**
 * Maybe make strategy pattern
 *
 * @author Focusrite
 */
public class InteractableTextureButton extends Interactable {

   private String texture;
   private Color highlight;
   private Color normal;
   private Color disabledColor = Color.DARK_GREY;
   private boolean disabled = false;

   public InteractableTextureButton(int x, int y, int width, int height, String texture, Color normal, Color highlight) {
      super(x, y, width, height);
      this.texture = texture;
      this.highlight = highlight;
      this.normal = normal;
   }

   public boolean isDisabled() {
      return disabled;
   }

   public void setDisabled(boolean disabled) {
      this.disabled = disabled;
   }

   @Override
   public void update(double dt) {
   }

   @Override
   public void render(Graphic g) {
      Color c = (!isDisabled()) ? (isMouseInside()) ? highlight : normal : disabledColor;

      g.drawTexture(ResourceManager.getTexture(texture), getX() + (getWidth() / 2),
            getY() + (getHeight() / 2), ZLayers.GUI, getWidth(), getHeight(), 0, c);
   }
}
