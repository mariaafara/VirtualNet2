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

//    HashMap<InetAddress, RoutingTableInfo> routingEntries;
    HashMap<Integer, RoutingTableInfo> routingEntries;

    transient final Object lockRoutingTable = new Object();
    transient final Object lockPortconxs = new Object();

    public RoutingTable() {
        try {
//            routingEntries = new HashMap<InetAddress, RoutingTableInfo>();
            routingEntries = new HashMap<Integer, RoutingTableInfo>();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
         * This method return the routing table of this client
     */
 /*
	 * This method adds an entry into the routing table
	 * @param destIP = destination  IP address
	 * @param nextHop = nextHop IP address
	 * @param cost = Cost to reach the destination
     */
    public void addEntry(InetAddress destIp, int nextHop, int cost, int myport, Port portclass, boolean activated) {
        synchronized (lockRoutingTable) {

            //  this.routingEntries.put(destIp, new RoutingTableInfo(nextHop, cost,myport, portclass, activated));
        }
    }

    public void addEntry(int destIp, int nextHop, int cost, int myport, Port portclass, boolean activated) {
        synchronized (lockRoutingTable) {

            this.routingEntries.put(destIp, new RoutingTableInfo(nextHop, cost, myport, portclass, activated));
        }
    }

    public int getNextHop(int myport) {
        synchronized (lockRoutingTable) {
            int senderport = 0;//???
            for (HashMap.Entry<Integer, RoutingTableInfo> entry : routingEntries.entrySet()) {

                if (entry.getValue().port == myport) {
                    senderport = entry.getValue().nextHop;
                }
            }

            return senderport;
        }
    }

    public void activateEntry(int nexthop) {
        for (HashMap.Entry<Integer, RoutingTableInfo> entry : routingEntries.entrySet()) {

            if (entry.getValue().nextHop == nexthop) {
                entry.getValue().setActivated(true);
            }
        }
    }

    public void deactivateEntry(int nexthop) {
        for (HashMap.Entry<Integer, RoutingTableInfo> entry : routingEntries.entrySet()) {

            if (entry.getValue().nextHop == nexthop) {
                entry.getValue().setActivated(false);
            }
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
            //  RoutingTableInfo ti = new RoutingTableInfo(nxthopIp, cost);
            //   this.routingEntries.put(destNtwk, ti);
            this.routingEntries.get(destNtwk).setCost(cost);
            this.routingEntries.get(destNtwk).setNextHop(nxthopIp);
        }
    }
//this method updates the cost and next hop used when updating routing table only

    public void updateEntry(int destNtwk, int nxthopIp, int cost) {
        synchronized (lockRoutingTable) {
            this.routingEntries.get(destNtwk).setCost(cost);
            this.routingEntries.get(destNtwk).setNextHop(nxthopIp);

        }
    }

//this method checks if it contains its port
    public boolean containsPort(int port) {
        synchronized (lockPortconxs) {
            boolean contains = false;
            for (HashMap.Entry<Integer, RoutingTableInfo> entry : routingEntries.entrySet()) {

                if (entry.getValue().port == port) {
                    contains = true;
                }
            }

            return contains;
        }
    }

    public boolean containsNextHop(int port) {
        synchronized (lockPortconxs) {
            boolean contains = false;
            for (HashMap.Entry<Integer, RoutingTableInfo> entry : routingEntries.entrySet()) {

                if (entry.getValue().nextHop == port) {
                    contains = true;
                }
            }

            return contains;
        }
    }

    public boolean isExistandNotActive(int port) {
        synchronized (lockPortconxs) {
            boolean contains = false;
            for (HashMap.Entry<Integer, RoutingTableInfo> entry : routingEntries.entrySet()) {

                if (entry.getValue().nextHop == port && !entry.getValue().activated) {
                    contains = true;
                }
            }

            return contains;
        }
    }

    /*
	 * This method prints the routing table entries
     */
    public void printTable(String hint) {
        // creating iterator for HashMap 
        synchronized (this) {

//            Iterator<HashMap.Entry<InetAddress, RoutingTableInfo>> routingEntriesIterator = routingEntries.entrySet().iterator();
            Iterator<HashMap.Entry<Integer, RoutingTableInfo>> routingEntriesIterator = routingEntries.entrySet().iterator();

//            InetAddress destAddress;
            int destAddress;
            System.out.print("---------------------------------   Routing Table " + hint + " -----------------------------------" + "\n");
            System.out.print("---------------------|------------------|-----------------------|---------------|------------------------" + "\n");
            System.out.print("---------------------|\tDest Network \t|\tNext Hop Port\t|\tCost\t|------------------------" + "\n");
            System.out.print("---------------------|------------------|-----------------------|---------------|------------------------" + "\n");
            while (routingEntriesIterator.hasNext()) {

//                HashMap.Entry<InetAddress, RoutingTableInfo> pair = (HashMap.Entry<InetAddress, RoutingTableInfo>) routingEntriesIterator.next();
                HashMap.Entry<Integer, RoutingTableInfo> pair = (HashMap.Entry<Integer, RoutingTableInfo>) routingEntriesIterator.next();

//                destAddress = (InetAddress) pair.getKey();
                destAddress = (Integer) pair.getKey();
                RoutingTableInfo destForwardingInfo = (RoutingTableInfo) pair.getValue();
//destAddress.getHostAddress()
                System.out.print("---------------------|\t" + destAddress + "\t|\t");//bs ntb3 linet address btbi3to 3m berj3 forword slash bas destAddress.getHostName() 3m trj3 aw2et msln one.one.one.
                System.out.print("" + destForwardingInfo.nextHop + "\t\t|\t  ");
                System.out.print(destForwardingInfo.cost + " \t|------------------------");
                System.out.println();
            }
            System.out.print("---------------------|------------------|-----------------------|---------------|------------------------" + "\n");
            System.out.print("---------------------------------------------------------------------------------------------------------" + "\n");

        }
    }
}
