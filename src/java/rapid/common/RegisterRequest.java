/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rapid.common;

/**
 *
 * @author ljk
 */
public class RegisterRequest {

    private String req;
    private boolean type;

    public RegisterRequest(boolean b) {
        type = b;
    }

    /**
     * @return the req
     */
    public String getReq() {
        return req;
    }

    /**
     * @param req the req to set
     */
    public void setReq(String req) {
        this.req = req;
    }

    /**
     * @return the type
     */
    public boolean isType() {
        return type;
    }

}