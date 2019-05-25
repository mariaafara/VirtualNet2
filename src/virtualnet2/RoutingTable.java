package virtualnet2;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author maria afara
 */
public class RoutingTable implements Serializable {

    HashMap<InetAddress, RoutingTableInfo> routingEntries;
    transient final Object lockRoutingTable = new Object();

    public RoutingTable() {
        try {
            routingEntries = new HashMap<InetAddress, RoutingTableInfo>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
         * This method return the routing table of this client
     */
    public RoutingTable getRoutingTable() {
        synchronized (lockRoutingTable) {
            return this;
        }
    }

    /*
	 * This method adds an entry into the routing table
	 * @param destIP = destination  IP address
	 * @param nextHop = nextHop IP address
	 * @param cost = Cost to reach the destination
     */
    public void addEntry(InetAddress destIp, int nextHop, int cost) {
        synchronized (lockRoutingTable) {
            
            this.routingEntries.put(destIp, new RoutingTableInfo(nextHop, cost));
        }
    }

    /*
        * This method checks if the routing table is formed and filled or not yet
     */
    public boolean isEmptyTable() {
        synchronized (lockRoutingTable) {
            return this.routingEntries.isEmpty();
        }
    }

    /*
        * This method delets an entry from the routing table
     */
    public void deleteEntry(InetAddress destIp) {
        synchronized (lockRoutingTable) {
            this.routingEntries.remove(destIp);
        }
    }

    /*
	 * this method updates cost to a given destination and its next hop
     */
    public void updateEntry(InetAddress destNtwk, int nxthopIp, int cost) {
        synchronized (lockRoutingTable) {
            RoutingTableInfo ti = new RoutingTableInfo(nxthopIp, cost);
            this.routingEntries.put(destNtwk, ti);
        }
    }

    /*
	 * This method prints the routing table entries
     */
    public void printTable(String hint) {
        // creating iterator for HashMap 
        synchronized (this) {

            Iterator<HashMap.Entry<InetAddress, RoutingTableInfo>> routingEntriesIterator = routingEntries.entrySet().iterator();
            InetAddress destAddress;

            System.out.print("---------------------------------   Routing Table " + hint + " -----------------------------------" + "\n");
            System.out.print("---------------------|------------------|-----------------------|---------------|------------------------" + "\n");
            System.out.print("---------------------|\tDest Network \t|\tNext Hop Port\t|\tCost\t|------------------------" + "\n");
            System.out.print("---------------------|------------------|-----------------------|---------------|------------------------" + "\n");
            while (routingEntriesIterator.hasNext()) {

                HashMap.Entry<InetAddress, RoutingTableInfo> pair = (HashMap.Entry<InetAddress, RoutingTableInfo>) routingEntriesIterator.next();
                destAddress = (InetAddress) pair.getKey();
                RoutingTableInfo destForwardingInfo = (RoutingTableInfo) pair.getValue();

                System.out.print("---------------------|\t" + destAddress.getHostAddress() + "\t|\t");//bs ntb3 linet address btbi3to 3m berj3 forword slash bas destAddress.getHostName() 3m trj3 aw2et msln one.one.one.
                System.out.print("" + destForwardingInfo.nextHop + "\t\t|\t  ");
                System.out.print(destForwardingInfo.cost + " \t|------------------------");
                System.out.println();
            }
            System.out.print("---------------------|------------------|-----------------------|---------------|------------------------" + "\n");
            System.out.print("---------------------------------------------------------------------------------------------------------" + "\n");

        }
    }
}
