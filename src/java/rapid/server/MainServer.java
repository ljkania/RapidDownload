package rapid.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.BindException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import rapid.common.Message;

public class MainServer implements Runnable {
   Socket csocket;
   
   MainServer(Socket csocket) {
      this.csocket = csocket;
   }

   public static void main(String args[]) throws Exception {
      ServerSocket ssock = new ServerSocket(1235);
      System.out.println("Listening");
      while (true) {
         Socket sock = ssock.accept();
         System.out.println("Connected");
         new Thread(new MainServer(sock)).start();
      }
   }
   
    public void run() {
        try {
           ObjectInputStream in = new ObjectInputStream(csocket.getInputStream());
           ObjectOutputStream out = new ObjectOutputStream(csocket.getOutputStream());
           
           System.out.println("Waiting on: " + csocket.getPort());
            
           while (true) {
               Message message = (Message) in.readObject();
               System.out.println("Recievd the line----" + message);
               out.writeObject(new Message(message.getId(), message.toString() + " Comming back from server", message.getArgs()));
               System.out.println("waiting for the next line....");
           }
        } catch (Exception e) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, e);
       }
    }
}   