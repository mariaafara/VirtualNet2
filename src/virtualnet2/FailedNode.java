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
    ArrayList<Integer> myports;

    public FailedNode(InetAddress inetaddress, ArrayList<Integer> myports) {
        this.inetaddress = inetaddress;
    }

    public InetAddress getInetaddress() {
        return inetaddress;
    }

    public void setInetaddress(InetAddress inetaddress) {
        this.inetaddress = inetaddress;
    }

}
