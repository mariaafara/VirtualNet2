/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.io.ObjectOutputStream;
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

    private final RoutingTable recievedroutingtable;
    private final int myport;
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
            String destAddress;

            int destCost;
            //gets the port of which the router sent the RT  from.
//            recieveport = connections.getNeighbor(port).getNeighborPort();

            // Iterate through the neighbor's routing table
            // Iterator<HashMap.Entry<InetAddress, RoutingTableInfo>> routingEntriesIterator = recievedroutingtable.routingEntries.entrySet().iterator();
            Iterator<HashMap.Entry<String, RoutingTableInfo>> routingEntriesIterator = recievedroutingtable.routingEntries.entrySet().iterator();
            //  HashMap.Entry<InetAddress, RoutingTableInfo> pair = (HashMap.Entry<InetAddress, RoutingTableInfo>) routingEntriesIterator.next();

            while (routingEntriesIterator.hasNext()) {
                //fetching routing table as pairs
                //    HashMap.Entry<InetAddress, RoutingTableInfo> pair = (HashMap.Entry<InetAddress, RoutingTableInfo>) routingEntriesIterator.next();
                HashMap.Entry<String, RoutingTableInfo> pair = (HashMap.Entry<String, RoutingTableInfo>) routingEntriesIterator.next();

//                destAddress = (InetAddress) pair.getKey();
                destAddress = (String) pair.getKey();

                //"Checking if my routing table has an entry for "  destAddress.getHostAddress()
                //Client.myRoutingTable.routingEntries.containsKey(destAddress) ? true or false
//                if (rt.routingEntries.containsKey(destAddress)) {
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
                //iza next hop nl row mano myport y3ne mano directly connected 3lye
                if (pair.getValue().nextHop != myport) {
                    Port p = rt.getPortClass(myport);
                    recievedport = rt.getNextHop(myport);

                    rt.addEntry(destAddress, recievedport, pair.getValue().cost + 1, myport, p, true,true);
                    isUpdated = true;
                    System.out.println("*updated");
                }

                //If yes send updates to all neighbors
                if (isUpdated) {
                    System.out.print("\n");
                    rt.printTable("After Update");
                    System.out.print("\n");
                    for (HashMap.Entry<String, RoutingTableInfo> entry : rt.routingEntries.entrySet()) {

                        if (entry.getValue().cost == 1) {
                            new RoutingTableSend(oos, rt).start();
                        }
                    }

                }
            }

        }
    }
}
