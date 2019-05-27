package virtualnet2;
//localtest2

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public RoutingTable routingTable;

    final Object lockRouter = new Object();

    PortConxs portConxs;

    /*
     * Constructor 
     */
    @Override
    public void run() {

        super.run();
        Scanner scn = new Scanner(System.in);
        System.out.println("enter nbr ports...");
        int nb = Integer.parseInt(scn.nextLine());

        for (int i = 0; i < nb; i++) {
            System.out.println("enter a port...");
            initializePort(Integer.parseInt(scn.nextLine()));
        }
        for (int i = 0; i < nb; i++) {
            try {
                System.out.println("enter to establish connection...");
                String line = scn.nextLine();
                StringTokenizer st = new StringTokenizer(line, ":");
                int myport = Integer.parseInt(st.nextToken());
                int nexthop = Integer.parseInt(st.nextToken());
                initializeConnection(myport, InetAddress.getLocalHost(), nexthop);
            } catch (UnknownHostException ex) {
                Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (scn.nextLine().equals("start")) {
            initializeRoutingProtocol();
        }
        while (true) {
        }

    }

    public Router() throws UnknownHostException {

        portConxs = new PortConxs();

        routingTable = new RoutingTable();
        this.ipAddress = InetAddress.getLocalHost();

    }

    public Router(InetAddress ipAddress) {

        portConxs = new PortConxs();
        routingTable = new RoutingTable();

        this.ipAddress = ipAddress;

    }

    public void initializeConnection(int port, InetAddress neighboraddress, int neighborport) {
        synchronized (this) {
            if (!portConxs.containsPort(port)) {
                System.out.println("*This port does not exists");
                return;
            }
            portConxs.getPortInstance(port).connect(port, neighboraddress, neighborport);
        }
    }

    public void initializePort(int port) {
        synchronized (this) {
            if (portConxs.containsPort(port)) {
                System.out.println("*This port exists");
                return;
            }
            Port portclass = new Port(port, routingTable);
            portConxs.addPort(port, portclass);
            portclass.start();
        }

    }
///wrong wrong wrong wrong

    public void initializeRoutingProtocol() {

        new RoutingService( routingTable).start();
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

}
