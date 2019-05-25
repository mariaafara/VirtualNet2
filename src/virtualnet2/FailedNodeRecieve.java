/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author maria afara
 */
public class FailedNodeRecieve extends Thread {

    Socket socket;

    FailedNode fn;//the one recieved
    Object recievedObject;
    private RoutingService rs;

    public FailedNodeRecieve(Object recievedObject, Socket socket, RoutingService rs) {

        this.socket = socket;
        this.rs = rs;
        this.recievedObject = recievedObject;
    }

    @Override
    public void run() {

        fn = recieveFailedNode(recievedObject);
        rs.deleteEntry(fn.getInetaddress());

        //delete entry then send ne routing table
        for (HashMap.Entry<Integer, Port> entry : rs.getPortsConxs().entrySet()) {

            new RoutingTableSend(entry.getValue().getSocket(), rs).start();
        }
        System.out.print("\n");
        rs.routingTable.printTable("After Deleting Failed Node  ");
        System.out.println("\n");

    }

    private FailedNode recieveFailedNode(Object recievedObject) {

        // get failed node object which is reccieved
        return fn = (FailedNode) recievedObject;
    }

}
