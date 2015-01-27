package rapid.client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import rapid.common.*;
import static rapid.common.Config.*;

/**
 *
 * @author ljk
 */
class Download extends Observable implements Runnable {    
    private final URL url;
    private final Module site;
    private int downloaded;
    private int status;
    Thread thread;
    
    public Download(URL url) {
        this.url = url;
        site = getModule(url);
        
        if(site==null){
            setStatus(ERROR);
            return;
        }
        
        downloaded = 0;
        setStatus(DOWNLOADING);
        download();
    }
    
    public String getUrl() {
        return url.toString();
    }
    
    public int getStatus() {
        return status;
    }
    
    public void restart() {
        downloaded = 0;
        setStatus(DOWNLOADING);
        download();
    }    
        
    private void setStatus(int s) {
        status = s;
        stateChanged();
    }
    
    private void download() {
        thread = new Thread(this);
        thread.start();
    }
    
    private String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }
        
    @Override
    public void run() {
        Socket sock = null;
        
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        
        try {
            sock = new Socket(SERVER_IP, SERVER_PORT);
            
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());
            
            Message m =  new Message(MessageType.GetFromURL, null, new Object[] {OK, site, url});
            m = Message.getResponse(m, in, out);
            
            if(m.getType()==MessageType.Notify && (int) m.getArg(0)!=OK){
                setStatus((int) m.getArg(STATUS));
                
            } else {
            
                System.out.println("Czekam na plik");
                m = (Message) in.readObject();

                if(m.getType()!=MessageType.FileReadyToSend) {
                    throw new Exception();
                }
                
                out.writeObject(new Message(MessageType.Notify, null, new Object[] {OK}));
                
                if(!FileExchange.GetFile(sock, (String) m.getArg(FILEPATH)))
                    throw new Exception();

                setStatus(COMPLETE);
            }
        } catch (Exception e) {
            setStatus(ERROR);
        } finally {
            try {
                if(in != null)
                    in.close();
            } catch(Exception e) {}
            try {
                if(out != null)
                    out.close();
            } catch(Exception e) {}
            try {
                if(sock != null)
                    sock.close();
            } catch(Exception e) {}
        }
        
    }

    private void stateChanged() {
        setChanged();
        notifyObservers();
    }
}