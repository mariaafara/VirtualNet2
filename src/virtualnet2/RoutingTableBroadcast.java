/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
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

    public RoutingTableBroadcast(RoutingTable routingTable) {

        //this.portConxs = portConxs;
        this.routingTable = routingTable;
        System.out.println("*in broadcast constructor");
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
                System.out.println("*in broadcast infinte loop");

                for (HashMap.Entry<String, RoutingTableInfo> entry : routingTable.routingEntries.entrySet()) {

                    System.out.println("*in broadcast before  RoutingTableSend");
                    if (entry.getValue().cost == 1 && entry.getValue().portclass.isconnectionEstablished()) {
                        new RoutingTableSend(entry.getValue().portclass.getOos(), routingTable).start();
                        System.out.println("*in broadcast after RoutingTableSend ");
                    }

                }
                //    System.out.println("socket in broadcast" + entry.getValue().getSocket());
                System.out.println("***********");

                System.out.println("*before sleep");
                System.out.println("***********");

                long startTime = System.currentTimeMillis();

                /* ... the code being measured starts ... */
                // sleep for 5 seconds
                //  TimeUnit.SECONDS.sleep(5);
                Thread.sleep(69000);
                /* ... the code being measured ends ... */
                long endTime = System.currentTimeMillis();

                long timeElapsed = endTime - startTime;
                System.out.println("***********");

                System.out.println("*after sleep " + timeElapsed);
                System.out.println("***********");

            } catch (InterruptedException ex) {
                Logger.getLogger(RoutingTableBroadcast.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
