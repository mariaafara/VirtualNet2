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

    private PortConxs portConxs;
    RoutingTable routingTable;
    Connections connections;

    public RoutingService(PortConxs portConxs, Connections connections, RoutingTable routingTable) {

        this.portConxs = portConxs;
        this.routingTable = routingTable;
        this.connections = connections;
    }

    /*
        *This method start broadcasting the router's routing table 
     */
    @Override
    public void run() {

        super.run();

//        FillRoutingTable();
        //allow to recieve objects at each port
        for (HashMap.Entry<Integer, Port> entry : portConxs.getPortsConxs().entrySet()) {
            System.out.println("*run reciever for the port " + entry.getKey());
       
            new Reciever(entry.getKey(), entry.getValue().getSocket(), portConxs, connections, routingTable).start();
        }
        try {
            new RoutingTableBroadcast(portConxs, routingTable).start();
        } catch (SocketException ex) {
            Logger.getLogger(RoutingService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//   
//    public void FillRoutingTable() {
//
//        // adding the directly  connected neighbors to the routing table i.e forming it
//        for (HashMap.Entry<Integer, Neighbor> entry : connections.entrySet()) {
//            System.out.println("adrs "+entry.getValue().neighborAddress+" entry.getValue().neighborPort");
//            addEntry(entry.getValue().neighborAddress, entry.getValue().neighborPort, 1);
//        }
//
//        routingTable.printTable("    Once Created   ");
//    }
}
