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
public class DownloadResponse implements Serializable {

    private String resp;
    

    public DownloadResponse( String resp) {
        this.resp = resp;
        
    }

    /**
     * @return the resp
     */
    public String getResp() {
        return resp;
    }

    /**
     * @param resp the resp to set
     */
    public void setResp(String resp) {
        this.resp = resp;
    }


}