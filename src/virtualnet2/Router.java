package virtualnet2;
//localtest2

import sharedPackage.FailedNode;
import java.io.IOException;
import sharedPackage.RoutingTableKey;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
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

    String hostname;

    InetAddress ipAddress;

    public RoutingTable routingTable;

    final Object lockRouter = new Object();

    PortConxs portConxs;

    ArrayList<RoutingTableKey> networks;
    RoutingService routingService;

    /*
     * Constructor 
     */
    @Override
    public void run() {

        super.run();
        Scanner scn = new Scanner(System.in);
        System.out.println("enter name of router.............");
        String name = scn.nextLine();
        setHostname(name);
        System.out.println("enter nbr ports..................");
        int nb = Integer.parseInt(scn.nextLine());

        for (int i = 0; i < nb; i++) {
            System.out.println("enter a port.................");
            initializePort(Integer.parseInt(scn.nextLine()));
        }
        for (int i = 0; i < nb; i++) {

            try {
                System.out.println("enter to establish connection....(myport:neighname:nexthop).......");
                String line = scn.nextLine();
                StringTokenizer st = new StringTokenizer(line, ":");
                int myport = Integer.parseInt(st.nextToken());
                String neighname = st.nextToken();
                int nexthop = Integer.parseInt(st.nextToken());

                initializeConnection(myport, InetAddress.getLocalHost(), neighname, nexthop);
            } catch (UnknownHostException ex) {
                Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        if (scn.nextLine().equals("start")) {
            while (true) {
                try {
                    System.out.println("enter a network host name:");
                    String nextHost = scn.nextLine();
                    if (nextHost.equals("end")) {
                        System.out.println("end");
                        initializeRoutingProtocol(networks);
                        break;
                    }
                    System.out.println("adding" + nextHost);
                    networks.add(new RoutingTableKey(InetAddress.getLocalHost(), nextHost));
                } catch (UnknownHostException ex) {
                    Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        System.out.println("enter 'stop' to stop working");

        if (scn.nextLine().equals("stop")) {
            FailedNode mefn = new FailedNode(new RoutingTableKey(ipAddress, hostname), new RoutingTableKey(ipAddress, hostname));

            for (HashMap.Entry<RoutingTableKey, RoutingTableInfo> entry2 : routingTable.routingEntries.entrySet()) {

                if (entry2.getValue().cost == 1) {
                    try {
                        /////hon/////
                        //lezm eb3t lkel jar eno m7eet lkel                        

                        System.out.print("\n*broadcast ");
                        ///hyda be 2lb tene loop
                        int i = 0;
                        for (HashMap.Entry<RoutingTableKey, RoutingTableInfo> entry3 : routingTable.routingEntries.entrySet()) {
                            FailedNode newfn = new FailedNode(entry3.getKey(), new RoutingTableKey(ipAddress, hostname));
                            if (i == 0) {
                                entry2.getValue().portclass.getOos().writeObject(mefn);
                            }
                            i++;
                            entry2.getValue().portclass.getOos().writeObject(newfn);
                        }

                    } catch (IOException ex) {
                        Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            //////fi hon big problem 
            ///// bnsbe lal connectios lmftu7in ben l tafet wll neighbors le ela 
//step 1
            routingService.routingTableBroadcast.stopBroadcast();

            for (HashMap.Entry<RoutingTableKey, RoutingTableInfo> entry2 : routingTable.routingEntries.entrySet()) {

                if (entry2.getValue().cost == 1) {
                    try {
                        ///////////////stop recieving then stop cnxs (close socket)
                        Reciever reciver = entry2.getValue().portclass.reciever;
                        ///////////////

                        reciver.stopRecieving();

                        System.out.print("\n*closing cnx with " + entry2.getKey());
                        entry2.getValue().portclass.getSocket().close();
                        entry2.getValue().portclass.portConnectionWait.stopWaitingForConnection();
                        //stoping reciving on the adjacent ports where the cnx between them is stoped(finished) due to the failure in one node

                    } catch (IOException ex) {
                        Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            //stopRouter();
            this.stop();

        }
        while (true) {
        }

    }

    public void stopRouter() {

        this.stop();
        routingService.routingTableBroadcast.stopBroadcast();
    }

    public Router() throws UnknownHostException {

        networks = new ArrayList<RoutingTableKey>();

        portConxs = new PortConxs();

        routingTable = new RoutingTable();

        this.ipAddress = InetAddress.getLocalHost();

    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void initializeConnection(int port, InetAddress neighboraddress, String neighborhostname, int neighborport) {
        synchronized (this) {
            if (!portConxs.containsPort(port)) {
                System.out.println("*This port does not exists");
                return;
            }
            portConxs.getPortInstance(port).connect(neighboraddress, neighborhostname, neighborport);
        }
    }

    public void initializePort(int port) {
        synchronized (this) {
            if (portConxs.containsPort(port)) {
                System.out.println("*This port exists");
                return;
            }
            Port portclass = new Port(port, hostname, routingTable);

            portConxs.addPort(port, portclass);//3m syv 3ndee lport

            portclass.start();
        }

    }

    public void initializeRoutingProtocol(ArrayList<RoutingTableKey> networks) {

        routingService = new RoutingService(routingTable, networks);
        routingService.start();
        System.out.println("*initializeRoutingProtocol");
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

}
