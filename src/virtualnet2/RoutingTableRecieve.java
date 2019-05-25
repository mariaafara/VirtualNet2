/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maria afara
 */
public class RoutingTableRecieve extends Thread {

    int recieveport;

    Socket socket;

    RoutingTable routingTable;//the one recieved

    private RoutingTable rt;
    private int i = 0;
    private int port;

    Object recievedObject;
    Connections connections;
    PortConxs portConxs;

    public RoutingTableRecieve(Object recievedObject, int port, Socket socket, PortConxs portConxs, Connections connections, RoutingTable rt) {
        System.out.println("routing table recieve initialized");
        this.port = port;
        this.socket = socket;
        this.rt = rt;
        this.recievedObject = recievedObject;
        this.connections = connections;
        this.portConxs = portConxs;
    }

    @Override
    public void run() {

        //recieve routing table
        routingTable = recieveRoutingTable(recievedObject);

        //if my routing table has been formed send the response
        if (!rt.isEmptyTable()) {
            if (i == 0) {
                i++;
                new RoutingTableSend(socket, rt).start();
            }

            //gets the port of which the router sent the RT  from.
            recieveport = connections.getNeighbor(port).getNeighborPort();

            System.out.print("\n");
            routingTable.printTable("Recieved from " + recieveport + " ");
            System.out.println("\n");

            // Check if this routing table's object needs to be updated
            new RoutingTableUpdate(routingTable, recieveport, socket,portConxs, rt).start();

        }

    }

    private RoutingTable recieveRoutingTable(Object recievedObject) {

        return routingTable = (RoutingTable) recievedObject;
    }

}
