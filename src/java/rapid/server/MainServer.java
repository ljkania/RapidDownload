package rapid.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import rapid.common.*;
import static rapid.common.Config.*;

public class MainServer implements Runnable {

    class URLInfo{
        boolean inProgress;
        int tries;
        Vector<String> ips;
        File f;
        
        URLInfo(){
            inProgress = false;
            tries = 0;
            ips = new Vector<>();
            f = null;
        }
        URLInfo(String ip){
            inProgress = false;
            tries = 0;
            ips = new Vector<>();
            addUser(ip);
            f = null;
        }
        boolean addUser(String ip){
            if(checkUser(ip))
                return false;
            ips.add(ip);
            return true;
        }
        boolean checkUser(String ip) {
            return ips.contains(ip);
        }
        void addFilePath(String path) {
            f = new File(path);
        }
        boolean isComplete(){
            return f!=null;
        }
        File getFile(){
            return f;
        }

        private boolean isInProgress() {
            return inProgress;
        }
        void setInProgress(boolean s){
            inProgress = s;
        }
        void addTry(){
            tries++;
        }

        private boolean toRemove() {
            return tries>=10 && !isComplete();
        }
         
    }
    
    Socket csocket;
    static ConcurrentHashMap users = new ConcurrentHashMap<>();
    static ConcurrentHashMap tasks = new ConcurrentHashMap<>();

    public MainServer(Socket csocket) {
        this.csocket = csocket;
    }

    public static void main(String args[]) throws Exception {
        ServerSocket ssock = new ServerSocket(Config.SERVER_PORT);
        System.out.println("Listening");

        while (true) {
            Socket sock = ssock.accept();
            System.out.println("Connected");
            new Thread(new MainServer(sock)).start();
        }
    }

    private void client(ObjectOutputStream out, ObjectInputStream in, Message m) throws Exception {

        String ip = csocket.getInetAddress().getHostAddress();
        Integer req = (Integer) users.get(ip);

        if(req == null) {
            users.put(ip, 0);
            req = (Integer) users.get(ip);
        }

        if(req >= Config.REQUEST_LIMIT) {
            out.writeObject(new Message(MessageType.Notify, null, new Object[]{Config.LIMIT}));
            return;
        }

        out.writeObject(new Message(MessageType.Notify, null, new Object[]{Config.OK}));

        URL url = ((URL) m.getArg(URL));
        boolean flag = true;
        
        if(!tasks.containsKey(url.toString())) {
            tasks.put(url.toString(), new URLInfo(ip));
            users.replace(ip, ((int) users.get(ip))+1);
        }
        else {
            URLInfo temp = (URLInfo) tasks.get(url.toString());
            
            if(temp.addUser(ip))
                users.replace(ip, ((int) users.get(ip))+1);
            
            tasks.replace(url.toString(), temp);
        }
        
        System.out.println("Byde miał coś dla Cie! " + csocket.getRemoteSocketAddress().toString());

        while(!((URLInfo) tasks.get(url.toString())).isComplete());
        
        users.replace(ip, ((int) users.get(ip))-1);
        
        File file =((URLInfo) tasks.get(url.toString())).getFile();
        
        if(!file.isFile())
            throw new Exception();
        
        m = new Message(MessageType.FileReadyToSend, null, new Object[]{OK, csocket, url, file.getPath()});
        m = Message.getResponse(m, in, out);

        if (m.getType() != MessageType.Notify || (int) m.getArg(STATUS) != OK || !FileExchange.SendFile(csocket, url.toString())) {
            throw new Exception();
        }
    }

    private void server(ObjectOutputStream out, ObjectInputStream in, Message m) {
        try{
            boolean flag = false;
            
            while(!flag) {

                if(m.getType() != MessageType.LimitInfo) {
                    throw new Exception();
                }
                
                flag = false;
                System.out.println("Heja!");
                Set<String> keys = tasks.keySet();

                for (String address : keys) {
                    URL url = new URL(address);
                    System.out.println(url);

                    System.out.println(m.getArg(1));
                    System.out.println(m.getArg(MODULE));
                    System.out.println(getModule(url));
                    
                    URLInfo task = (URLInfo) tasks.get(address);

                    if((Module) m.getArg(MODULE) != getModule(url) || task.isInProgress() || task.isComplete()) {
                        continue;
                    }

                    System.out.println("TUM");

                    try {
                        task.setInProgress(true);
                        tasks.replace(address, task);

                        m = new Message(MessageType.GetFromURL, null, new Object[]{OK, getModule(url), url});
                        m = Message.getResponse(m, in, out);
                        
                        System.out.println("TUŻEMJEST " + m.getType() + " " + m.getArg(STATUS));

                        if(m.getType() != MessageType.FileReadyToSend) {
                            throw new Exception();
                        }

                        String filePath = (String) m.getArg(FILEPATH); 

                        System.out.println("GÓWNO!");

                        out.writeObject(new Message(MessageType.Notify, null, new Object[]{OK}));

                        System.out.println("SRANIE");

                        if (!FileExchange.GetFile(csocket, filePath)) {
                            throw new Exception();
                        }
                        else
                        {
                            task.addFilePath(filePath);
                            tasks.replace(address,task);
                        }

                    } catch (Exception e) {
                        out.writeObject(new Message(MessageType.Notify, null, new Object[]{UNKNOWN}));
                    } finally {
                        System.out.println("Sprzątam!");
                        task.addTry();
                        if(task.toRemove())
                            tasks.remove(address);
                        else {
                            task.setInProgress(false);
                            tasks.replace(address, task);
                        }
                    }
                    flag = true;
                    break;
                }

                if (!flag) {
                    System.out.println("No i dupcia");
                    out.writeObject(new Message(MessageType.Notify, null, new Object[]{OK}));
                    m = (Message) in.readObject();
                }
            }
        } catch (Exception e) {}
    }

    @Override
    public void run() {
        ObjectInputStream in = null;
        ObjectOutputStream out = null;

        try {
            in = new ObjectInputStream(csocket.getInputStream());
            out = new ObjectOutputStream(csocket.getOutputStream());
            System.out.println("Waiting on: " + csocket.getRemoteSocketAddress().toString() + " " + csocket.getInetAddress().toString());

            Message m = (Message) in.readObject();
            System.out.println("Masz wiadomość " + csocket.getRemoteSocketAddress().toString());

            if (m.getType() == MessageType.GetFromURL) {
                client(out, in, m);
            } else if (m.getType() == MessageType.LimitInfo) {
                server(out, in, m);
            }

        } catch (Exception e) {
            try {
                out.writeObject(new Message(MessageType.Notify, null, new Object[]{UNKNOWN}));
            } catch (Exception ex) {
            }

        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
            try {
                out.close();
            } catch (Exception e) {
            }
            try {
                csocket.close();
            } catch (Exception e) {
            }
        }

        System.out.println("Po robocie " + csocket.getRemoteSocketAddress().toString());
    }
}
