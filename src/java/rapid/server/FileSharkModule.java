package rapid.server;

import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.regex.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.apache.tomcat.util.codec.binary.Base64;
import rapid.common.*;
import static rapid.common.Config.*;

public class FileSharkModule {
    
    private static URL parseURL(URL address) {
        String url = address.toString();
        
        Pattern pattern = Pattern.compile(".*/pobierz/.*");
        Matcher matcher = pattern.matcher(url);
        
        if(matcher.matches() == false)
            return null;
        
        pattern = Pattern.compile(".*/normal/.*");
        matcher = pattern.matcher(url);
        
        if(matcher.matches() == false)
        {
            String[] parts = url.split("/pobierz/");
            StringBuilder s = new StringBuilder("");
            s = s.append(parts[0]);
            s = s.append("/pobierz/normal/");
            s = s.append(parts[1]);
            url = s.toString();
        }
        
        
        try {
            return new URL(url);
        }
        catch(Exception e) {
            return null;
        }
    }
    
    private static String solveCaptcha(String captchaAddress) {
        byte[] data = Base64.decodeBase64(captchaAddress);
        try (OutputStream stream = new FileOutputStream(new File("Downloads/filesharkcaptcha.jpeg"))){
            stream.write(data);
        } catch (IOException ex) {
        }
        
        BufferedImage image = null;
        
        try {
            image = ImageIO.read(new File("Downloads/filesharkcaptcha.jpeg"));
        } catch (IOException ex) {
        }
        
        if(image==null)
            return null;
        
        JLabel picLabel = new JLabel(new ImageIcon(image));
        String captcha = JOptionPane.showInputDialog(null, picLabel, null);
        
        return captcha;
    }
    
    private static String getToken(String response) {
        Pattern pattern = Pattern.compile("form._token.\" value=\"(.*?)\"");
        Matcher matcher = pattern.matcher(response);
        matcher.find();
        
        return matcher.group(1);
    }
    
    private static String downloadFile(URL url, String captcha, String token) throws Exception {
        InputStream is =null;
        FileOutputStream fos=null;
        FILESHARK_COUNTER++;
        try {
            URLConnection urlConn = url.openConnection();  //connect

            is = urlConn.getInputStream();               //get connection inputstream
            File f = new File("Downloads/filesharkfile");
            while(f.isFile())
                f=new File("Downloads/fileshark"+FILESHARK_COUNTER);
            
            fos = new FileOutputStream(f);   //open outputstream to local file

            byte[] buffer = new byte[4096];              //declare 4KB buffer
            int len;

            //while we have availble data, continue downloading and storing to local file
            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        } catch(Exception e) {System.out.println(e.getMessage()); return null;}
        finally {
            try {
                if (is != null) {
                    is.close();
                }
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }
        
        return "Downloads/fileshark"+ FILESHARK_COUNTER;
    }
    
    private static int getTimeToWait(String response){
        Pattern pattern = Pattern.compile("timeToDownload = (.*?);");
        Matcher matcher = pattern.matcher(response.toString());
        matcher.find();
        return Integer.parseInt(matcher.group(1));
    }
    
    public static Message checkLimit() {
        String test = "http://fileshark.pl/pobierz/normal/1524043/0h9v9";
        InputStream is = null;
        BufferedReader in = null;
                    return new Message(MessageType.LimitInfo, null, new Object[] {OK, Module.FILESHARK, 0});

        
        try {
            URL url = parseURL(new URL(test));
            
            System.out.println(url.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            System.out.println("dupcia");
            
 
            con.setRequestMethod("GET");
            
            int responseCode = con.getResponseCode();
	
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            
            String inputLine;
            StringBuffer response = new StringBuffer();
 
            while((inputLine = in.readLine())!=null)
		response.append(inputLine+"\n");
                        
            Pattern pattern = Pattern.compile("\"data:image/jpeg;base64,(.*?)\"");
            Matcher matcher = pattern.matcher(response.toString());
            
            int timeToWait;
            
            if(matcher.find()==true)
                timeToWait = 0;
            else
                timeToWait = getTimeToWait(response.toString());
            
            return new Message(MessageType.LimitInfo, null, new Object[] {OK, Module.FILESHARK, timeToWait});
         
        } catch (Exception e) {
            return new Message(MessageType.Notify, null, new Object[] {UNKNOWN});
        } finally {
            try{
                is.close();
            } catch (Exception e){}
            try{
                in.close();
            } catch (Exception e){}
        }
    }
    
    public static String downloadFromURL(URL address) {
        //try{
            return "Downloads/filesharkcaptcha.jpeg";
            //return downloadFile(address, null, null);}
        //catch(Exception e){
          //  return null;
        //}
    }
}