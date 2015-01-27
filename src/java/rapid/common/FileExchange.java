package rapid.common;

import java.io.*;
import java.net.*;

/**
 *
 * @author ljk
 */
public class FileExchange {
    
    public static boolean GetFile(Socket sock, String filePath) {
        boolean flag = true;
        InputStream is = null;
        FileOutputStream fos = null;
        
        try {
            byte[] mybytearray = new byte[4096];
            is = sock.getInputStream();
            fos = new FileOutputStream((String)filePath);
            byte[] buffer = new byte[4096];
            int len;
            
            while((len = is.read(buffer)) > 0)
                fos.write(buffer, 0, len);
                        
        } catch (Exception e) {
            flag = false;
        }finally {
            try{
                is.close();
            }catch(Exception e){}
            try{
                fos.close();
            } catch(Exception e){}
        }
        
        
        
        return flag;
    }
    
    public static boolean SendFile(Socket sock, String filePath) {        
        boolean flag= true;
        BufferedInputStream bis = null;
        OutputStream os = null;
        FileInputStream fis = null;
        
        try {
            File myFile = new File(filePath);
            
            byte[] mybytearray = new byte[(int) myFile.length()];
            fis = new FileInputStream(myFile);
            bis = new BufferedInputStream(new FileInputStream(myFile));
            bis.read(mybytearray, 0, mybytearray.length);
            os = sock.getOutputStream();
            os.write(mybytearray, 0, mybytearray.length);
            os.flush();
            
            bis.close();
            os.close();
        } catch(Exception e) {
            flag = false;
        }finally {
            try{
                os.close();
            }catch(Exception e){}
            try{
                fis.close();
            } catch(Exception e){}
            try{
                bis.close();
            } catch(Exception e){}
        }

        return flag;
    }
}