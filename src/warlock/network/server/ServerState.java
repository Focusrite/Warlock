/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.network.server;

/**
 *
 * @author Focusrite
 */
public abstract class ServerState {
   NetworkServer server;

   public ServerState(NetworkServer server) {
      this.server = server;
   }

   public abstract void run();
}
