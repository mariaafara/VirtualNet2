package virtualnet2;

import virtualnet2.BroadcastRouitngTable;
import virtualnet2.Neighbor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import virtualnet2.RoutingTable;

/**
 * Router Class
 *
 * @descrip This class represents a router. It will act as an independent
 * thread. It has connections between different routers.
 * @author maria afara
 *
 */
public class Router {

    static RoutingTable myRoutingTable;
    static ArrayList<Neighbor> neighbors;
    static InetAddress ipAddress;
    static DatagramSocket socket;
    static int port;
    static int i = 0;

    /*
     * Constructor initializing the socket object and others
     */
    public Router(InetAddress ipAddress, int port, ArrayList<Neighbor> neighbors) {
        try {
            // Assign the ip and neighbors
            this.ipAddress = ipAddress;
            this.neighbors = neighbors;
            this.port = port;

            socket = new DatagramSocket(port);

            // Create the initial routing table
            myRoutingTable = new RoutingTable();

            fillRoutingTable(neighbors);

//            ListenAtPort listenAtPort = new ListenAtPort(port);
//            listenAtPort.start();
            BroadcastRouitngTable broadcastroutingtablethread = new BroadcastRouitngTable(neighbors, socket);
            broadcastroutingtablethread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Neighbor> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(ArrayList<Neighbor> neighbors) {
        Router.neighbors = neighbors;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        Router.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        Router.port = port;
    }

    private void fillRoutingTable(ArrayList<Neighbor> neighbors) {
        Iterator<Neighbor> neighborIterator = neighbors.iterator();
        // adding the directly  connected neighbors to my routing table i.e forming it
        while (neighborIterator.hasNext()) {
            //System.out.println("adding to table");
            Neighbor nextNeighbor = neighborIterator.next();
            myRoutingTable.addEntry(nextNeighbor.neighborAddress, nextNeighbor.neighborPort, 1);

        }
        myRoutingTable.printTable("    Once Created   ");
    }

    /*
     * This method return the routing table of this client
     */
    public RoutingTable getRoutingTable() {
        return myRoutingTable;
    }

    /*  
	 * This method adds a new neighbor to the neighbor list in case we wanted to add a static rout 
	 * @param neighborAddress= InetAddress of the neighbor to be added
	 * @param port= Port at which the neighbor is connected
     */
    public void addNeighborInfo(InetAddress neighborAddress, int port) {
        Neighbor newNeighbor = new Neighbor(neighborAddress, port);

        neighbors.add(newNeighbor);
    }

    /*
	 * This method adds a new neighbor to the neighbor list in case we wanted to add a static rout 
	 * @param neighbor= Neighbor object that needs to be added to the neighbor list
     */
    public void addNeighborInfo(Neighbor neighbor) {
        neighbors.add(neighbor);
    }

  
}
