package virtualnet2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author maria afara
 */
public class RoutingService extends Thread {

    public RoutingTable routingTable;
    private HashMap<Integer, Port> portsConxs;
    private HashMap<Integer, Neighbor> connections;

    public RoutingService(HashMap<Integer, Port> portsConxs, HashMap<Integer, Neighbor> connections) {
        this.connections = connections;
        this.portsConxs = portsConxs;
        routingTable = new RoutingTable();
    }

    /*
        *This method start broadcasting the router's routing table 
     */
    @Override
    public void run() {

        super.run();
        FillRoutingTable();
        //allow to recieve routing table at each port
        for (HashMap.Entry<Integer, Port> entry : portsConxs.entrySet()) {

            new RoutingTableRecieve(entry.getKey(), entry.getValue().getSocket(), this);
        }
        try {
            new RoutingTableBroadcast(this).start();
        } catch (SocketException ex) {
            Logger.getLogger(RoutingService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    synchronized public HashMap<Integer, Neighbor> getConnections() {
        return connections;
    }

    synchronized public HashMap<Integer, Port> getPortsConxs() {
        return portsConxs;
    }

    /*
         * This method return the routing table of this client
     */
    synchronized public RoutingTable getRoutingTable() {
        return routingTable;
    }

    /*
	 * This method adds an entry into the routing table
	 * @param destIP = destination  IP address
	 * @param nextHop = nextHop IP address
	 * @param cost = Cost to reach the destination
     */
    synchronized public void addEntry(InetAddress destIp, int nextHop, int cost) {
        routingTable.routingEntries.put(destIp, new RoutingTableInfo(nextHop, cost));
    }

    /*
        * This method checks if the routing table is formed and filled or not yet
     */
    synchronized public boolean isEmptyTable() {
        return routingTable.routingEntries.isEmpty();
    }

    /*
        * This method delets an entry from the routing table
     */
    synchronized public void deleteEntry(InetAddress destIp) {
        routingTable.routingEntries.remove(destIp);
    }

    /*
	 * this method updates cost to a given destination and its next hop
     */
    synchronized public void updateEntry(InetAddress destNtwk, int nxthopIp, int cost) {
        RoutingTableInfo ti = new RoutingTableInfo(nxthopIp, cost);
        routingTable.routingEntries.put(destNtwk, ti);
    }

    /*  
	 * This method adds a new neighbor to the neighbor list in case we wanted to add a static rout 
	 * @param neighborAddress = InetAddress of the neighbor to be added
	 * @param neighborport = Port at which the neighbor is connected
     */
//    public void addNeighbor(InetAddress neighborAddress, int neighborport) {
//
//        Neighbor newNeighbor = new Neighbor(neighborAddress, neighborport);
//        neighbors.add(newNeighbor);
//        addEntry(neighborAddress, neighborport, 1);
//
//    }
    /*
        *This methods fills the initial routing table once the directly connected neighbors are assigned
     */
    public void FillRoutingTable() {

        // adding the directly  connected neighbors to the routing table i.e forming it
        for (HashMap.Entry<Integer, Neighbor> entry : connections.entrySet()) {

            addEntry(entry.getValue().neighborAddress, entry.getValue().neighborPort, 1);
        }

        routingTable.printTable("    Once Created   ");
    }
}
