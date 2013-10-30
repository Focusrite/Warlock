/**
 * File: warlock.hud.interactable.InteractableListener.java
 *
 * An interface that can be implemented to allow event reading when an interactable are being
 * manipulated. This is the fat version which also includes mouseEnter and mouseExited events.
 */
package warlock.hud.interactable;

/**
 *
 * @author Focusrite
 */
public interface InteractableListener extends InteractableListenerSlim{
   public void mouseEntered(InteractableInfo source);
   public void mouseExited(InteractableInfo source);
}
