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
    private RoutingService rs;

    public RoutingTableUpdate(RoutingTable recievedroutingtable, int recievedrouterPort, Socket socket, RoutingService rs) {

        this.recievedroutingtable = recievedroutingtable;
        this.recievedrouterPort = recievedrouterPort;
        this.socket = socket;
        this.rs = rs;
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

        synchronized (rs.routingTable) {
            System.out.println("In check for updates method");

            boolean isUpdated = false;
            InetAddress destAddress;
            int destCost;

            // Iterate through the neighbor's routing table
            Iterator<HashMap.Entry<InetAddress, RoutingTableInfo>> routingEntriesIterator = recievedroutingtable.routingEntries.entrySet().iterator();

            while (routingEntriesIterator.hasNext()) {
                //fetching routing table as pairs
                HashMap.Entry<InetAddress, RoutingTableInfo> pair = (HashMap.Entry<InetAddress, RoutingTableInfo>) routingEntriesIterator.next();
                destAddress = (InetAddress) pair.getKey();

                //"Checking if my routing table has an entry for "  destAddress.getHostAddress()
                //Client.myRoutingTable.routingEntries.containsKey(destAddress) ? true or false
                if (rs.getRoutingTable().routingEntries.containsKey(destAddress)) {

                    destCost = rs.getRoutingTable().routingEntries.get(destAddress).cost;

                    //"Cost for this destination in my routing table is "  destCost
                    //"Cost for this destination in received routing table is "  pair.getValue().cost
                    if (destCost > (1 + pair.getValue().cost)) {
                        //which is smaller than my cost for the destination
                        rs.updateEntry(destAddress, recievedrouterPort, 1 + pair.getValue().cost);

                        isUpdated = true;

                    }

                } else//it does not contain it so add it 
                {

                    rs.addEntry(destAddress, pair.getValue().nextHop, pair.getValue().cost + 1);
                }
            }

            //If yes send updates to all neighbors
            if (isUpdated) {
                rs.getRoutingTable().printTable("After Update");
                new RoutingTableSend(socket,rs).start();

            }
        }

    }
}
