package virtualnet2;
//localtest2

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
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

    InetAddress ipAddress;

    public RoutingTable routingTable;

    final Object lockRouter = new Object();

    PortConxs portConxs;

    ArrayList<RoutingTableKey> networks;

    /*
     * Constructor 
     */
    @Override
    public void run() {

        super.run();
        Scanner scn = new Scanner(System.in);

        System.out.println("enter nbr ports..................");
        int nb = Integer.parseInt(scn.nextLine());

        for (int i = 0; i < nb; i++) {
            System.out.println("enter a port.................");
            initializePort(Integer.parseInt(scn.nextLine()));
        }
        for (int i = 0; i < nb; i++) {

            try {
                System.out.println("enter to establish connection....(myport nexthop).......");
                String line = scn.nextLine();
                StringTokenizer st = new StringTokenizer(line, " ");
                int myport = Integer.parseInt(st.nextToken());
               
                int nexthop = Integer.parseInt(st.nextToken());

                initializeConnection(myport, InetAddress.getLocalHost(), nexthop);
            } catch (UnknownHostException ex) {
                Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        if (scn.nextLine().equals("start")) {
            while (true) {
                try {
                    System.out.println("enter a network :");
                    String nexthop = scn.nextLine();
                    if (nexthop.equals("end")) {
                        System.out.println("end");
                        initializeRoutingProtocol(networks);
                        continue;
                    }
                    System.out.println("adding" + nexthop);
                    networks.add(new RoutingTableKey(InetAddress.getLocalHost(), Integer.parseInt(nexthop)));
                } catch (UnknownHostException ex) {
                    Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        while (true) {
        }

    }

    public Router() throws UnknownHostException {

        networks = new ArrayList<RoutingTableKey>();

        portConxs = new PortConxs();

        routingTable = new RoutingTable();

        this.ipAddress = InetAddress.getLocalHost();

    }

    public Router(InetAddress ipAddress) {

        networks = new ArrayList<RoutingTableKey>();

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
//ya name aw lip

//    public void initializeConnection(int port, InetAddress neighboraddress, int neighborport) {
//        synchronized (this) {
//            if (!portConxs.containsPort(port)) {
//                System.out.println("*This port does not exists");
//                return;
//            }
//            portConxs.getPortInstance(port).connect(port, neighboraddress, neighborport);
//        }
//    }
    public void initializePort(int port) {
        synchronized (this) {
            if (portConxs.containsPort(port)) {
                System.out.println("*This port exists");
                return;
            }
            Port portclass = new Port(port, routingTable);

            portConxs.addPort(port, portclass);//3m syv 3ndee lport

            portclass.start();
        }

    }

//    public void initializeRoutingProtocol() {
//   
//        new RoutingService(routingTable).start();
//        System.out.println("*initializeRoutingProtocol");
//    }
//    public void initializeRoutingProtocol(ArrayList<InetAddress> networks) {
//        new RoutingService(routingTable).start();
//        System.out.println("*initializeRoutingProtocol");
//    }
    public void initializeRoutingProtocol(ArrayList<RoutingTableKey> networks) {

        new RoutingService(routingTable, networks).start();
        System.out.println("*initializeRoutingProtocol");
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

}
