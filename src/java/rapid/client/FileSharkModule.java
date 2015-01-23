package rapid.client;

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.apache.tomcat.util.codec.binary.Base64;
import sun.net.www.http.HttpClient;


public class FileSharkModule {
    static String address = "fileshark.pl/pobierz/1524043/0h9v9#";
    
    public FileSharkModule(String address){
        this.address = address;
    }
    
    public static void main(String[] args) throws IOException, Exception, MalformedURLException {
        Pattern pattern = Pattern.compile("http://.*");
        Matcher matcher = pattern.matcher(address);
        
        if(matcher.matches() == false)
        {
            StringBuilder s =new StringBuilder("http://");
            s = s.append(address);
            address = s.toString();
        }
        pattern = Pattern.compile(".*/pobierz/.*");
        matcher = pattern.matcher(address);
        
        if(matcher.matches() == false)
            throw new Exception("Incorrect address");
        
        pattern = Pattern.compile(".*/normal/.*");
        matcher = pattern.matcher(address);
        
        if(matcher.matches() == false)
        {
            String[] parts = address.split("/pobierz/");
            StringBuilder s = new StringBuilder("");
            s = s.append(parts[0]);
            s = s.append("/pobierz/normal/");
            s = s.append(parts[1]);
            address = s.toString();
        }
        
        
        String fileName = "temp.html";
        System.out.println(address);
        URL url = new URL(address); //The file that you want to download
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            URL obj = new URL(address);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
            // optional default is GET
            con.setRequestMethod("GET");
 
            int responseCode = con.getResponseCode();
	
            BufferedReader in = new BufferedReader(
		new InputStreamReader(con.getInputStream()));
            
            String inputLine;
            StringBuffer response = new StringBuffer();
 
            while ((inputLine = in.readLine()) != null) {
		response.append(inputLine+"\n");
            }
            
            in.close();
            
            System.out.print(response.toString());
            pattern = Pattern.compile("\"data:image/jpeg;base64,(.*?)\"");
            matcher = pattern.matcher(response.toString());
            
            if(matcher.find() == true)
            {
                byte[] data = Base64.decodeBase64(matcher.group(1));
                try (OutputStream stream = new FileOutputStream("filesharkcaptcha.jpeg")) {
                    stream.write(data);
                }
            
                BufferedImage image = ImageIO.read(new File("filesharkcaptcha.jpeg"));
                JLabel picLabel = new JLabel(new ImageIcon(image));
                String captcha = JOptionPane.showInputDialog(null, picLabel, null);
                System.out.println(captcha);
                
                pattern = Pattern.compile("form._token.\" value=\"(.*?)\"");
                matcher = pattern.matcher(response.toString());
                matcher.find();
                
                String token = matcher.group(1);

                
                
                con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("form_captcha", captcha);
                con.setRequestProperty("form_token", token);
                
                responseCode = con.getResponseCode();

                in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            
                response = new StringBuffer();
            
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine+"\n");
                }
            
                in.close();
  
            
                System.out.print(response.toString());
                
            }
            else
            {
            }
            
            
            
            URLConnection urlConn = url.openConnection();  //connect

            is = urlConn.getInputStream();               //get connection inputstream
            fos = new FileOutputStream(fileName);   //open outputstream to local file

            byte[] buffer = new byte[4096];              //declare 4KB buffer
            int len;

            //while we have availble data, continue downloading and storing to local file
            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        } finally {
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
        System.out.println("Done!");
    }

    private Exception Exception(String incorrect_address) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}