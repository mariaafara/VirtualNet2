/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author maria afara
 */
public class RoutingTableRecieve extends Thread {

    int recieveport;

    ObjectInputStream ois;
    ObjectOutputStream oos;

    RoutingTable routingTable;//the one recieved

    private RoutingTable rt;
    private static int i = 0;
    private int port;

    Object recievedObject;

    public RoutingTableRecieve(Object recievedObject, int port, ObjectInputStream ois, ObjectOutputStream oos, RoutingTable rt) {

        System.out.println("routing table recieve initialized");
        this.port = port;
        this.ois = ois;
        this.oos = oos;
        this.rt = rt;
        this.recievedObject = recievedObject;

    }

    @Override
    public void run() {

        //recieve routing table
        routingTable = recieveRoutingTable(recievedObject);

        //if my routing table has been formed send the response
        if (!rt.isEmptyTable()) {
            if (i == 0) {
                i++;
                //System.out.println("");
                new RoutingTableSend(oos, rt).start();
            }

            recieveport = rt.getNextHop(port);

            System.out.print("\n");
            routingTable.printTable("Recieved from " + recieveport + " ");
            System.out.println("\n");

            // Check if this routing table's object needs to be updated
            new RoutingTableUpdate(routingTable, port, oos, rt).start();

        }

    }

    private RoutingTable recieveRoutingTable(Object recievedObject) {

        return routingTable = (RoutingTable) recievedObject;
    }

}
