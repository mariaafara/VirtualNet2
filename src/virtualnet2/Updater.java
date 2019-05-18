/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maria afara
 */
public class Updater extends Thread {

    private RoutingTable recievedroutingtable;
    InetAddress recievedrouterip;
    private int recievedrouterPort;
    private DatagramSocket socket;

    public Updater(RoutingTable recievedroutingtable, InetAddress recievedrouterip, int recievedrouterPort, DatagramSocket socket) {
        this.recievedroutingtable = recievedroutingtable;
        this.recievedrouterip = recievedrouterip;
        this.recievedrouterPort = recievedrouterPort;
        this.socket = socket;
    }

    /**
     * This method updates router's routing table based on routing table
     * received from the neighbor
     */
    @Override
    public void run() {
        try {
            checkForUpdates();
        } catch (SocketException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
	 * This method checks if it's own routing table needs to be updated
     */
    public void checkForUpdates() throws SocketException {
        // System.out.println("***In check for updates method***");

        synchronized (Router.myRoutingTable) {

            System.out.println("In check for updates method");

            boolean isUpdated = false;
            InetAddress destAddress;
            int destCost;

            //Check if the routing table needs to be updated
            // Iterate through the neighbor's routing table
            Iterator<HashMap.Entry<InetAddress, TableInfo>> routingEntriesIterator = recievedroutingtable.routingEntries.entrySet().iterator();

            while (routingEntriesIterator.hasNext()) {
                HashMap.Entry<InetAddress, TableInfo> pair = (HashMap.Entry<InetAddress, TableInfo>) routingEntriesIterator.next();
                destAddress = (InetAddress) pair.getKey();

                //"Checking if my routing table has an entry for " + destAddress.getHostAddress()
                //Client.myRoutingTable.routingEntries.containsKey(destAddress) ? true or false
                if (Router.myRoutingTable.routingEntries.containsKey(destAddress)) {

                    //"It does" get first my  cost
                    destCost = Router.myRoutingTable.routingEntries.get(destAddress).cost;

                    //"Cost for this destination in my routing table is " + destCost
                    //"Cost for this destination in received routing table is " + pair.getValue().cost
                    if (destCost > (1 + pair.getValue().cost)) {
                        //"which is smaller than my cost for the destination"
                        Router.myRoutingTable.routingEntries.get(destAddress).cost = 1 + pair.getValue().cost;
                        Router.myRoutingTable.routingEntries.get(destAddress).nextHop = recievedrouterPort;
                        isUpdated = true;
                        //"Routing table updated for " + destAddress.getHostAddress()
                    }

                } else//it does not contain it so add it 
                {

                    Router.myRoutingTable.addEntry(destAddress, pair.getValue().nextHop, pair.getValue().cost + 1);
                }
            }

            //If yes send updates to all neighbors
            if (isUpdated) {
                Router.myRoutingTable.printTable("After Update");
                SendRoutingTable sendingroutingtablethread = new SendRoutingTable(Router.neighbors, socket);
                sendingroutingtablethread.start();

            }
        }

    }
}
