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

    //private PortConxs portConxs;
    RoutingTable routingTable;

    public RoutingService(RoutingTable routingTable) {

        //  this.portConxs = portConxs;
        this.routingTable = routingTable;

    }

    /*
        *This method start broadcasting the router's routing table 
     */
    @Override
    public void run() {

        super.run();
        try {
            //sar kel port 3ndo sender w reciever
            //allow to recieve objects at each port
            for (HashMap.Entry<Integer, RoutingTableInfo> entry : routingTable.routingEntries.entrySet()) {

                //fina bala lcondition kermel bel awal bs neighbors mwjudin bl table
                if (entry.getValue().cost == 1) {
                    System.out.println("*socket in reciever local= " + entry.getValue().portclass.getSocket().getLocalPort()+" port="+entry.getValue().portclass.getSocket().getPort());

                    System.out.println("*run reciever for the port " + entry.getKey());
                    new Reciever(entry.getKey(), entry.getValue().portclass.getSocket(), routingTable).start();
                }
            }

            new RoutingTableBroadcast(routingTable).start();

        } catch (SocketException ex) {
            Logger.getLogger(RoutingService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
