package virtualnet2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Router Class
 *
 * @descrip This class represents a router. It has connections between different
 * routers and maybe p c s. once a router is created its i p and ports are
 * assigned to it .Then is start listing and allowing connection on and to its
 * ports. after that when it activates the routing protocol its own routing
 * service instance is created along with its routing table then Start assign
 * the networks(directly connected) : -add to its neighbors -add to its RT -open
 * the t c p socket c n x with neighbor's port After configuration of routing
 * protocol is done start broadcasting...
 *
 * @author maria afara
 *
 */
public class Router extends Thread {

    //fi oset neighbors 
    InetAddress ipAddress;

    static HashMap<Integer, Port> portsConxs;//kel port 3ndo thread port khas fi

    static HashMap<Integer, Neighbor> connections;//each port with its directy connected to it

    /*
     * Constructor 
     */
    @Override
    public void run() {

        super.run();
        while (true) {

        }

    }

    public Router() throws UnknownHostException {
        this.ipAddress = InetAddress.getLocalHost();
        connections = new HashMap<Integer, Neighbor>();
        portsConxs = new HashMap<Integer, Port>();
        System.out.println("connections and portsConxs are created");
    }

    public Router(InetAddress ipAddress) {

        // Assign the ip 
        this.ipAddress = ipAddress;
        connections = new HashMap<Integer, Neighbor>();
        portsConxs = new HashMap<Integer, Port>();

    }

    public void initializeConnection(int port, InetAddress neighboraddress, int neighborport) {
        synchronized (this) {
            if (!portsConxs.containsKey(port)) {
                System.out.println("This port does not exists");
                return;
            }
            Neighbor newNeighbor = new Neighbor(neighboraddress, neighborport);
            connections.put(port, newNeighbor);
            System.out.println("1connection is initialized at port " + port + " with neighb= " + neighboraddress + " , " + neighborport);
            getPorts().get(port).connect(port, neighboraddress, neighborport);
        }
    }

    public void initializePort(int port) {
        synchronized (this) {
            if (portsConxs.containsKey(port)) {
                System.out.println("This port exists");
                return;
            }
            Port portclass = new Port(port);
            portsConxs.put(port, portclass);
            portclass.start();
        }

    }

    public void initializeRoutingProtocol() {
        new RoutingService(portsConxs, connections).start();
    }

    synchronized public HashMap<Integer, Port> getPorts() {
        return portsConxs;
    }

//    public void connect(int myport, InetAddress inetAddress, int port) {
//        //tayeb wbrke lnetwork l3m b3ml 3le connect ma mwjud
//        if (!portsConxs.containsKey(port)) {
//            System.out.println("This port does not exists");
//            return;
//        }
//        getPorts().get(myport).connect(inetAddress, port);
//    }
    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

}
