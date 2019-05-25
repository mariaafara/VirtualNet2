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
    private RoutingTable rt;
    PortConxs portConxs;

    public FailedNodeRecieve(Object recievedObject, Socket socket, PortConxs portConxs, RoutingTable rt) {

        this.socket = socket;
        this.rt = rt;
        this.recievedObject = recievedObject;
    }

    @Override
    public void run() {

        fn = recieveFailedNode(recievedObject);
        rt.deleteEntry(fn.getInetaddress());

        //delete entry then send ne routing table
        for (HashMap.Entry<Integer, Port> entry : portConxs.getPortsConxs().entrySet()) {

            new RoutingTableSend(entry.getValue().getSocket(), rt).start();
        }
        System.out.print("\n");
        rt.printTable("After Deleting Failed Node  ");
        System.out.println("\n");

    }

    private FailedNode recieveFailedNode(Object recievedObject) {

        // get failed node object which is reccieved
        return fn = (FailedNode) recievedObject;
    }

}
