/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rapid.common;

import java.net.URL;

/**
 *
 * @author ljk
 */
public class Config {
    public final static String SERVER_IP = "127.0.0.1";
    public final static int SERVER_PORT = 1234;
    
    public final static int FILESHARK = 0;
    public final static int MODULES = 1;
    
    public final static int REQUEST_LIMIT = 10;
    
    
    public final static int OK = 0;
    public final static int UNKNOWN = 1;
    public final static int LIMIT = 2;
    
    public final static int STATUS = 0;
    
    public final static int SOCKET =1;
    public final static int FILEPATH = 3;
    
    public final static int MODULE = 1;
    public final static int URL = 2;
    public final static int TIME = 2;

    public final static int MAX_BUFFER_SIZE = 4096;
    public static int FILESHARK_COUNTER = 0;
    
    
    
    public final static String STATUSES[] = {"Downloading", "Complete", "Limit reached", "Already on list", "Error"};
    
    public static final int DOWNLOADING = 0;
    public static final int COMPLETE = 1;
    public static final int ERROR = 4;

    public static Module getModule(URL url){
        System.out.println(url);
        if(url.toString().toLowerCase().startsWith("http://fileshark.pl") || url.toString().toLowerCase().startsWith("http://www.fileshark.pl"))
            return Module.FILESHARK;
        System.out.println("A jednak chujnia");
        return null;        
    }
    
}
