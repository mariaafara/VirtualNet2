/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 *
 * @author maria afara
 */
public class FailedNode {

    InetAddress inetaddress;
    int port;
////complete failed or entry 
    //failedentry aw failedrouter
    public FailedNode(InetAddress inetaddress, int port) {
        this.inetaddress = inetaddress;
        this.port = port;
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
