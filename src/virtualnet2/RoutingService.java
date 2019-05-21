package virtualnet2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import virtualnet2.Neighbor;
import virtualnet2.RoutingTable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author maria afara
 */
public class RoutingService {

    public static RoutingTable routingTable;
    private static RoutingService instance;
    private ArrayList<Neighbor> neighbors;

    private RoutingService() {
        routingTable = new RoutingTable();

    }

    /*
         * Returns singleton instance of RoutingService class
     */
    public static RoutingService getInstance() {
        if (instance == null) {
            // synchronized on the class of the current object 
            //so only one thread at a time can access any instances of this class.
            //which is nessecary because every thing concerning routing table will be managed through it
            synchronized (RoutingService.class) {
                if (instance == null) {
                    instance = new RoutingService();
                }
            }
        }
        return instance;
    }

    /*
         * This method return the neighbors
     */
    public ArrayList<Neighbor> getNeighbors() {
        return neighbors;
    }

    /*
         * This method return the routing table of this client
     */
    public RoutingTable getRoutingTable() {
        return routingTable;
    }

    /*
	 * This method adds an entry into the routing table
	 * @param destIP = destination  IP address
	 * @param nextHop = nextHop IP address
	 * @param cost = Cost to reach the destination
     */
    public void addEntry(InetAddress destIp, int nextHop, int cost) {
        routingTable.routingEntries.put(destIp, new RoutingTableInfo(nextHop, cost));
    }

    /*
        * This method checks if the routing table is formed and filled or not yet
     */
    public boolean isEmptyTable() {
        return routingTable.routingEntries.isEmpty();
    }

    /*
        * This method delets an entry from the routing table
     */
    public void deleteEntry(InetAddress destIp) {
        routingTable.routingEntries.remove(destIp);
    }

    /*
	 * this method updates cost to a given destination and its next hop
     */
    public void updateRoute(InetAddress destNtwk, int nxthopIp, int cost) {
        RoutingTableInfo ti = new RoutingTableInfo(nxthopIp, cost);
        routingTable.routingEntries.put(destNtwk, ti);
    }

    /*  
	 * This method adds a new neighbor to the neighbor list in case we wanted to add a static rout 
	 * @param neighborAddress = InetAddress of the neighbor to be added
	 * @param neighborport = Port at which the neighbor is connected
     */
    public void addNeighbor(InetAddress neighborAddress, int neighborport) {

        Neighbor newNeighbor = new Neighbor(neighborAddress, neighborport);
        neighbors.add(newNeighbor);
        addEntry(neighborAddress, neighborport, 1);

    }

    /*  
	 * This method establishes a socket connection with the dirclty connected network  
	 * @param routerAddress = InetAddress of the router that is establishing the connection
	 * @param neighborport = Port at which the connection is linked
     */
    public Socket establishConxWithNeihbor(InetAddress neighborAddress, int neighborport) throws IOException {

        return new Socket(neighborAddress, neighborport);

    }

    /*
        *This method start broadcasting the router's routing table 
        *@parm routerAddress = it takes its own ip address
     */
    public void startRouting() throws SocketException {
        
        new RouitngTableBroadcast().start();
        
    }

    /*
	 * This method adds a new neighbor to the neighbor list in case we wanted to add a static rout 
	 * @param neighbor = Neighbor object that needs to be added to the neighbor list
     */
    public void addNeighbor(Neighbor neighbor) {
        neighbors.add(neighbor);
    }

    /*
        *This methods fills the initial routing table once the directly connected neighbors are assigned
     */
    public void FillRoutingTable() {
        Iterator<Neighbor> neighborIterator = neighbors.iterator();
        // adding the directly  connected neighbors to the routing table i.e forming it
        while (neighborIterator.hasNext()) {

            Neighbor nextNeighbor = neighborIterator.next();
            addEntry(nextNeighbor.neighborAddress, nextNeighbor.neighborPort, 1);

        }
        routingTable.printTable("    Once Created   ");
    }
}
