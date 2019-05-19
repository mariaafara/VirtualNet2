package virtualnet2;

import virtualnet2.Neighbor;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import virtualnet2.RoutingTable;

/**
 * Router Class
 *
 * @descrip This class represents a router. It has connections between different
 * routers and maybe p c s. once a router is created its i p and ports are
 * assigned to it .Then is start listing on its ports. after that when it
 * activates the routing protocol its own routing service instance is created
 * along with its routing table then Start assign the networks(directly
 * connected) : -add to its neighbors -add to its RT -open the t c p socket c n
 * x with neighbor's port After configuration of routing protocol is done start
 * broadcasting...
 *
 * @author maria afara
 *
 */
public class Router {

    RoutingTable myRoutingTable;
    InetAddress ipAddress;
    DatagramSocket socket;
    ArrayList<Integer> ports;


    /*
     * Constructor 
     */
    public Router() {

    }

    public Router(InetAddress ipAddress, ArrayList<Integer> ports) {
        try {
            // Assign the ip and ports
            this.ipAddress = ipAddress;

            this.ports = ports;

            for (int i = 0; i < ports.size(); i++) {

                new ListenAtPort(ports.get(i)).start();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initiaizeListenAtPorts(ArrayList<Integer> ports) {
        for (int i = 0; i < ports.size(); i++) {

            new ListenAtPort(ports.get(i)).start();

        }
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public ArrayList<Integer> getPorts() {
        return ports;
    }

    public void setPorts(ArrayList<Integer> ports) {
        this.ports = ports;
    }

}
