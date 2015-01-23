package rapid.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.BindException;

/**
 *
 * @author ljk
 */

public class SendFile {
    
    public static void main(String[] args) throws IOException {
        
        int sockNum = 1;
        ServerSocket servsock = null;
        String file = "dupa.mp3";
        
        while(true)
        {
            try {
                servsock = new ServerSocket(sockNum);
                System.out.println(sockNum);
                break;
            } catch(BindException e) {
                sockNum++;
            }
            
        }
        
        System.out.println(InetAddress.getLocalHost().getHostAddress());
        File myFile = new File(file);
        
        //while (true) {
            Socket sock = servsock.accept();
            byte[] mybytearray = new byte[(int) myFile.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
            bis.read(mybytearray, 0, mybytearray.length);
            OutputStream os = sock.getOutputStream();
            os.write(mybytearray, 0, mybytearray.length);
            os.flush();
            sock.close();
        //}
    }
}
