package rapid.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import rapid.client.WindowClient;
import rapid.common.Message;

/**
 *
 * @author ljk
 */
public class MainClient {
    public static void main(String[] args) throws MalformedURLException {
        int serverPort = 1235;
        String ip = "127.0.0.1";
        
        try {
            Socket socket = new Socket(ip, serverPort);
            
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            
            
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Type in something and press enter. Will send it to the server and tell ya what it thinks.");
            String line = null;

            System.out.println();
            
            while (true) {

                line = keyboard.readLine();
                out.writeObject(new Message(0, line, null));
                System.out.println("Wrinting Something on the server");
                Message response = (Message) in.readObject();
                System.out.println("Line Sent back by the server---" + response);
            }
        } catch (Exception e) {
             Logger.getLogger(MainClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }    
}
