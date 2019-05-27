/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.io.ObjectOutputStream;
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
    private int myport;
    private ObjectOutputStream oos;
    private RoutingTable rt;
    int recievedport;

    public RoutingTableUpdate(RoutingTable recievedroutingtable, int myport, ObjectOutputStream oos, RoutingTable rt) {

        this.recievedroutingtable = recievedroutingtable;
        this.myport = myport;
        this.oos = oos;
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
            //gets the port of which the router sent the RT  from.
//            recieveport = connections.getNeighbor(port).getNeighborPort();
            recievedport = rt.getNextHop(myport);

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
                        rt.updateEntry(destAddress, myport, 1 + pair.getValue().cost);

                        isUpdated = true;

                    }

                } else//it does not contain it so add it 
                {
//////////////////////???????/////////////////////////////////

             //       rt.addEntry(destAddress, pair.getValue().nextHop, pair.getValue().cost + 1, myport, ?,true);
                }
            }

            //If yes send updates to all neighbors
            if (isUpdated) {
                rt.printTable("After Update");
                for (HashMap.Entry<Integer, RoutingTableInfo> entry : rt.routingEntries.entrySet()) {

                    if (entry.getValue().cost == 1) {
                        new RoutingTableSend(oos, rt).start();
                    }
                }

            }
        }

    }
}
