package virtualnet2;

import java.util.HashMap;

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

        //sar kel port 3ndo sender w reciever
        //allow to recieve objects at each port
//        for (HashMap.Entry<String, RoutingTableInfo> entry : routingTable.routingEntries.entrySet()) {
//            System.out.println("*in RoutingService in hashmap loop ");
//            //fina bala lcondition kermel bel awal bs neighbors mwjudin bl table
//            if (entry.getValue().cost == 1) {
//
//                System.out.println("*socket in reciever local= " + entry.getValue().portclass.getSocket().getLocalPort() + " port=" + entry.getValue().portclass.getSocket().getPort());
//
//                System.out.println("*run reciever for the port " + entry.getKey());
//
//                new Reciever(entry.getKey(), entry.getValue().port, entry.getValue().portclass.getOis(), entry.getValue().portclass.getOos(), routingTable).start();
//            }
//        }
        System.out.println("*in RoutingService before broadcast");
        
        new RoutingTableBroadcast(routingTable).start();
    }

}
