/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.io.Serializable;
import java.net.InetAddress;

/**
 *
 * @author maria afara
 */
public class RoutingTableKey implements Serializable  {

    int port;
    InetAddress ip;

    public RoutingTableKey(InetAddress ip, int port) {
        this.port = port;
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "RoutingTableKey{" + "port=" + port + ", ip=" + ip + '}';
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        RoutingTableKey other = (RoutingTableKey) o;
        if (this.port != other.port) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        // uses roll no to verify the uniqueness 
        // of the object of Student class 

        int ans = ip.hashCode() + port;
        return ans;  //To change body of generated methods, choose Tools | Templates.
    }

}
