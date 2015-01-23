package rapid.client;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;

/**
 *
 * @author ljk
 */

public class GetFile {
    public static void main(String[] argv) throws Exception {
        InputStream is = null;
        FileOutputStream fos = null;
        String ip = "127.0.0.1";
        int sockNum = 1024;
        String file = "dupcia.mp3";
        
        try {
            Socket sock = new Socket(ip, sockNum);
            
            byte[] mybytearray = new byte[4096];
            is = sock.getInputStream();
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int len;
            
            while((len = is.read(buffer)) > 0)
                fos.write(buffer, 0, len);
        
        } finally {
            try {
                if(is != null)
                    is.close();
            } finally {
                if(fos != null)
                    fos.close();
            }
        }
    }
}