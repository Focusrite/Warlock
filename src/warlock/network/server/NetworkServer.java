/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package warlock.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkServer implements Runnable{

   private static final int PORT = 9090;
   private ServerSocket listener;
   private ArrayList<ServerClient> clients = new ArrayList<>();
   private ServerState serverState;
   private boolean playersCanJoin = true;

   public NetworkServer() {
      try {
         listener = new ServerSocket(PORT);
      }
      catch (Exception e) {
         System.out.println("Can't open a connection on port " + PORT);
      }
   }

   public void close() {
      try {
         listener.close();
      }
      catch (IOException ex) {
         Logger.getLogger(NetworkServer.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @Override
   public void run() {
      try {
         while(playersCanJoin) { //continously listen to new clients wanting to connect

            serverState.run();
         }
         while(true) {
            serverState.run();
         }
      }
      catch (Exception e) {
      }
      finally {
         close();
      }
   }

   public void disablePlayersJoining() {
      playersCanJoin = false;
   }

}
