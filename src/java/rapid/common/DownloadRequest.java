/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rapid.common;

import java.io.Serializable;

/**
 *
 * @author ljk
 */
public class DownloadRequest implements Serializable{
    
    private String urlToDownload;
    private User user;
    

    public DownloadRequest(User user,String text) {
        urlToDownload = text;
        this.user = user;
    }

    /**
     * @return the urlToDownload
     */
    public String getUrlToDownload() {
        return urlToDownload;
    }

    /**
     * @param urlToDownload the urlToDownload to set
     */
    public void setUrlToDownload(String urlToDownload) {
        this.urlToDownload = urlToDownload;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
    
    
    
    
    
}