package rapid.server;

import java.io.*;
import static java.lang.Thread.sleep;
import java.net.*;
import rapid.common.*;
import static rapid.common.Config.*;

public class UserServer implements Runnable {
    Socket csocket;
    Module module;
    
    public UserServer(Socket csocket, Module module) {
        this.csocket = csocket;
        this.module = module;
    }
   
    @Override
    public void run() {
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        Message m = null;

        try {
            out = new ObjectOutputStream(csocket.getOutputStream());
            in = new ObjectInputStream(csocket.getInputStream());
            
            
            while(true) {
                System.out.println("Sprawdze se, a co!");
                m = module.checkLimit();
                System.out.println(m.getType()+" "+m.getArg(TIME));

                if((int) m.getArg(TIME)==0)
                    m = Message.getResponse(m, in, out);
                else {
                    sleep((int) m.getArg(TIME)*1000);
                    continue;
                }

                if(m.getType()==MessageType.Notify){
                    System.out.println("Nic z tego :(");
                    sleep(10000);
                    continue;
                } else
                    break;
            }

            System.out.println("Hahaha! Będę pobierał!");

            System.out.println((URL) m.getArg(URL));
            String filePath = module.downloadFromURL((URL) m.getArg(URL));

            if(filePath!=null){
                System.out.println("No nieźle");
                m = new Message(MessageType.FileReadyToSend, null, new Object[] {OK, csocket, (URL) m.getArg(URL), filePath});
                System.out.println("Ledwo stoi");
                out.writeObject(m);
                m = (Message) in.readObject();

                System.out.println("Bailando! " + m.getType());

                if(m.getType()!=MessageType.Notify || (int) m.getArg(STATUS)!=OK || FileExchange.SendFile(csocket, filePath))
                    throw new Exception();

            }
            else
                throw new Exception();

        } catch(Exception e) {
            System.out.println(e.getMessage());System.out.println("I cały misterny plan w pizdu");

            try {
            out.writeObject(new Message(MessageType.Notify, null, new Object[] {UNKNOWN}));
            } catch(Exception ex){}

        } finally {
           try{
                in.close();
            } catch(Exception e){}
            try {
                out.close();
            } catch(Exception e) {}
            try {
                csocket.close();
            } catch(Exception e){}
        }
        
        try {
            
            Socket sock = new Socket(SERVER_IP, SERVER_PORT);
            new Thread(new UserServer(sock, module)).start();
        } catch(Exception e){System.out.println("CHUJ");}
        
        System.out.println("Zaraz zginę...");
    }
}   