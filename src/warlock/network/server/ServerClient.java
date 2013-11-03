/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.network.server;

import java.net.InetAddress;
import warlock.lobby.Lobby;
import warlock.network.FlowController;
import warlock.player.Player;

public class ServerClient {
   private Player player;
   private FlowController controller;

   public ServerClient(InetAddress address, Lobby lobby) {
      try {
         controller = new FlowController(address);
         player = lobby.addPlayerToLobby();
      }
      catch (Exception e) {
         System.out.println("Could not establish connection to " + address.getHostAddress());
      }
   }

   public void sendMessage() {
   }
}
