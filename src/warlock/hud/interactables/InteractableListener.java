/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.hud.interactables;

/**
 *
 * @author Focusrite
 */
public interface InteractableListener extends InteractableListenerSlim{
   public void mouseEntered(InteractableInfo source);
   public void mouseExited(InteractableInfo source);
}
