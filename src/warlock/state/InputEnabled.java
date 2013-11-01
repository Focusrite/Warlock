/**
 * File: warlock.state.InputEnabled.java
 *
 * An interface with the handleInput(..) method utilized all over the
 * project.
 */
package warlock.state;

import warlock.input.InputHandler;

public interface InputEnabled {
   public void handleInput(InputHandler input);
}
