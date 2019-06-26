/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.net.InetAddress;

/**
 *
 * @author maria afara
 */
public class FailedNode {

    InetAddress inetaddress;
    int port;
    String hostname;
////complete failed or entry 
    //failedentry aw failedrouter

    //ana router a m7et l b 
    // aya dest wmin ma7eha  
    public FailedNode(InetAddress inetaddress, String hostname, int port) {
        this.inetaddress = inetaddress;
        this.port = port;
        this.hostname = hostname;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public InetAddress getInetaddress() {
        return inetaddress;
    }

    public void setInetaddress(InetAddress inetaddress) {
        this.inetaddress = inetaddress;
    }

}
