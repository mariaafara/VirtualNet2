package virtualnet2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

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

    InetAddress ipAddress;

    HashMap<Integer,Port> ports;//kel port 3ndo thread port khas fi
    private Port p;

    /*
     * Constructor 
     */
    
    public Router() throws UnknownHostException {
        this.ipAddress = InetAddress.getLocalHost();
    }

    public Router(InetAddress ipAddress) {

        // Assign the ip 
        this.ipAddress = ipAddress;

    }

    public void initializePort(int port) {
        if(ports.containsKey(port)){
            System.out.println("This port exists");
            return;
        }
        ports.put(port,new Port(port));
        
    }

   

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }


}
