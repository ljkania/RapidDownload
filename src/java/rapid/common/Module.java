/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rapid.common;

import java.net.*;
import rapid.common.Message;
import rapid.server.FileSharkModule;

/**
 *
 * @author ljk
 * 
 */
public enum Module {
    FILESHARK {
        @Override
        public Message checkLimit(){
            return FileSharkModule.checkLimit();
        }
        
        @Override
        public String downloadFromURL(URL url){
            return FileSharkModule.downloadFromURL(url);
        }
    };
    abstract public Message checkLimit();
    abstract public String downloadFromURL(URL url);    
}
