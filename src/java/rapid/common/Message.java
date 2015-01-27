/*
 * 1, download from URL for me, {URL, status on site}
 * 2, 
 * 
 */
package rapid.common;

import java.io.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author ljk
 */
public class Message implements Serializable{

    MessageType type;
    String info;
    Object[] args;
    
    public Message() {}
    public Message(MessageType type, String info, Object[] args) {
        this.type = type;
        this.info = info;
        this.args = args;
    }
    
    @Override
    public String toString() {
        return info;
    }
    
    public MessageType getType() {
        return type;
    }
    
    public Object[] getArgs() {
        return args;
    }
    
    public Object getArg(int i) {
        return args[i];
    }
    
    public static Message getResponse(Message request, ObjectInputStream in, ObjectOutputStream out, int time) {
        System.out.println("Czy to w ogóle działa??? " + request.getType());
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Callable<Message> readResponse = () -> (Message) in.readObject();
        
        while(true) {
            System.out.println("Choć troche?");
            try {
                System.out.println(request.getType());
                out.writeObject(request);
            } catch (IOException ex) {
                Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("Próbuje");
            try {
                Future<Message> future = executor.submit(readResponse);
                return future.get(time, TimeUnit.MILLISECONDS);
            } catch(Exception e) {System.out.println("Dupeczka");}
        }
    }
    
    public static Message getResponse(Message request, ObjectInputStream in, ObjectOutputStream out) {
        return getResponse(request, in, out, 5000);
    }


}
