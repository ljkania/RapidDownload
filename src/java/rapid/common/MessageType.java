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
public enum MessageType {
    GetFromURL,
    LimitInfo,
    Notify,
    FileReadyToSend
}

/* GetFromURL
    args[] = {status, module, URL};
*/
/* LimitInfo
    args[] = {status, siteID, timeToWait} 
*/
/* RequestReceived
    args[] = {status} 
*/
/* ErrorOccured
    args[] = {status}
    0 - Unknown
    1 - limitReached
*/
/* FileReadyToSend
    args[] = {status, socket, url, filePath} 
*/