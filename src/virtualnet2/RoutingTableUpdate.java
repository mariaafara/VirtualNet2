/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This Thread updates router ' s routing table
 *
 * @author maria afara
 */
public class RoutingTableUpdate extends Thread {

    private RoutingTable recievedroutingtable;
    private int recievedrouterPort;
    private Socket socket;
    private RoutingTable rt;

    public RoutingTableUpdate(RoutingTable recievedroutingtable, int recievedrouterPort, Socket socket, RoutingTable rt) {

        this.recievedroutingtable = recievedroutingtable;
        this.recievedrouterPort = recievedrouterPort;
        this.socket = socket;
        this.rt = rt;

    }

    /*
        * This method updates router ' s routing table based on  routing table received from the neighbor
     */
    @Override
    public void run() {
        try {
            checkForUpdates();
        } catch (SocketException ex) {
            Logger.getLogger(RoutingTableUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
	 * This method checks if it's own routing table needs to be updated
     */
    public void checkForUpdates() throws SocketException {
///aw 3alock
        synchronized (this) {
            System.out.println("In check for updates method");

            boolean isUpdated = false;
            //InetAddress destAddress;
            int destAddress;

            int destCost;

            // Iterate through the neighbor's routing table
            // Iterator<HashMap.Entry<InetAddress, RoutingTableInfo>> routingEntriesIterator = recievedroutingtable.routingEntries.entrySet().iterator();
            Iterator<HashMap.Entry<Integer, RoutingTableInfo>> routingEntriesIterator = recievedroutingtable.routingEntries.entrySet().iterator();
            //  HashMap.Entry<InetAddress, RoutingTableInfo> pair = (HashMap.Entry<InetAddress, RoutingTableInfo>) routingEntriesIterator.next();

            while (routingEntriesIterator.hasNext()) {
                //fetching routing table as pairs
                //    HashMap.Entry<InetAddress, RoutingTableInfo> pair = (HashMap.Entry<InetAddress, RoutingTableInfo>) routingEntriesIterator.next();
                HashMap.Entry<Integer, RoutingTableInfo> pair = (HashMap.Entry<Integer, RoutingTableInfo>) routingEntriesIterator.next();

//                destAddress = (InetAddress) pair.getKey();
                destAddress = (Integer) pair.getKey();

                //"Checking if my routing table has an entry for "  destAddress.getHostAddress()
                //Client.myRoutingTable.routingEntries.containsKey(destAddress) ? true or false
                if (rt.routingEntries.containsKey(destAddress)) {

                    destCost = rt.routingEntries.get(destAddress).cost;

                    //"Cost for this destination in my routing table is "  destCost
                    //"Cost for this destination in received routing table is "  pair.getValue().cost
                    if (destCost > (1 + pair.getValue().cost)) {
                        //which is smaller than my cost for the destination
                        rt.updateEntry(destAddress, recievedrouterPort, 1 + pair.getValue().cost);

                        isUpdated = true;

                    }

                } else//it does not contain it so add it 
                {

                    //rt.addEntry(destAddress, pair.getValue().nextHop, pair.getValue().cost + 1);
                }
            }

            //If yes send updates to all neighbors
            if (isUpdated) {
                rt.printTable("After Update");
                for (HashMap.Entry<Integer, RoutingTableInfo> entry : rt.routingEntries.entrySet()) {

                    if (entry.getValue().cost == 1) {
                        new RoutingTableSend(entry.getValue().portclass.getSocket(), rt).start();
                    }
                }

            }
        }

    }
}
