/*
 * 1, download from URL for me, {URL, status on site}
 * 2, 
 * 
 */
package rapid.common;

import java.io.Serializable;

/**
 *
 * @author ljk
 */
public class Message implements Serializable{
    int messageId;
    String messageInfo;
    Object[] args;
    
    public Message() {}
    public Message(int messageId, String messageInfo, Object[] args) {
        this.messageId = messageId;
        this.messageInfo = messageInfo;
        this.args = args;
    }
    
    @Override
    public String toString() {
        return messageInfo;
    }
    
    public int getId() {
        return messageId;
    }
    
    public Object[] getArgs() {
        return args;
    }
}
