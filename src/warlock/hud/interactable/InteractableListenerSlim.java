/**
 * File: warlock.hud.interactable.InteractableListener.java
 *
 * An interface that can be implemented to allow event reading when an interactable are being
 * manipulated. This is the slim version only listening to clicked events.
 */
package warlock.hud.interactable;

/**
 *
 * @author Focusrite
 */
public interface InteractableListenerSlim {
   public void clicked(InteractableInfo source);
}
