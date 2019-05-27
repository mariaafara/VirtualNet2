/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.net.SocketException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import virtualnet2.RoutingTableSend;

/**
 *
 * @author maria afara
 */
public class RoutingTableBroadcast extends Thread {

    // PortConxs portConxs;
    RoutingTable routingTable;

    public RoutingTableBroadcast(RoutingTable routingTable) throws SocketException {

        //this.portConxs = portConxs;
        this.routingTable = routingTable;

    }

    /*
        *This method first establishes a socket connection with the dirclty connected network  (neighbors)
        * This method then sends routing table to all the neighbors every 60 or 30 sec.
     */
    @Override
    public void run() {

        //for the first time lzm tb3t request lal neighs ino yb3tula lrt 
        //l2elonn b3den btb3t le 2ela wbtsh8el ltimer
        while (true) {
            try {
                for (HashMap.Entry<Integer, RoutingTableInfo> entry : routingTable.routingEntries.entrySet()) {

                    if ( entry.getValue().cost==1 && entry.getValue().portclass.isconnectionEstablished()) {
                        new RoutingTableSend(entry.getValue().portclass.getSocket(), routingTable).start();
                    }
                }
            //    System.out.println("socket in broadcast" + entry.getValue().getSocket());

                Thread.sleep(30000);

            } catch (InterruptedException ex) {
                Logger.getLogger(RoutingTableBroadcast.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
