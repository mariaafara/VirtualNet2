package virtualnet2;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;

/**
 * this class represents the routing table which is a hash map
 *
 * @author maria afara
 */
public class RoutingTable implements Serializable {

//    HashMap<InetAddress, RoutingTableInfo> routingEntries;
//    HashMap<Integer, RoutingTableInfo> routingEntries;
    HashMap<String, RoutingTableInfo> routingEntries;
    //transient krmel ma ynb3to lobjects manon serilizable kmn 
    transient final Object lockRoutingTable = new Object();
    transient final Object lockPortconxs = new Object();

    public RoutingTable() {
        try {
//            routingEntries = new HashMap<InetAddress, RoutingTableInfo>();
            routingEntries = new HashMap<String, RoutingTableInfo>();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//if itcontain this network set the established boolean to true

    public void establishEntry(String network) {
        synchronized (lockRoutingTable) {
            System.out.println("establish entry for " + network + "\n");
            routingEntries.get(network).setEstablished(true);

        }
    }

    public void establishEntry(InetAddress network) {
        synchronized (lockRoutingTable) {

        }
    }
    //lezm yn3ml mtla 3l ip lal neighbor
///this method return the entryof the given  network or name

    public RoutingTableInfo getEntry(String name) {
        synchronized (lockRoutingTable) {

            return routingEntries.get(name);
        }
    }

    public boolean isEstablishedEntry(String name) {
        synchronized (lockRoutingTable) {
            if (routingEntries.containsKey(name)) {
                System.out.println(name + " in routing table\n");
            } else {
                System.out.println(name + " not in routing table\n");
            }

            return routingEntries.get(name).isEstablished();
        }
    }

    /*
	 * This method adds an entry into the routing table
	 * @para synchronized (lockRoutingTable) {m destIP = destination  IP address
	 * @param nextHop = nextHop IP address
	 * @param cost = Cost to reach the destination
     */
    public void addEntry(InetAddress destIp, int nextHop, int cost, int myport, Port portclass, boolean activated) {
        synchronized (lockRoutingTable) {

            //  this.routingEntries.put(destIp, new RoutingTableInfo(nextHop, cost,myport, portclass, activated));
        }
    }

    public void addEntry(String routername, int nextHop, int cost, int myport, Port portclass, boolean activated, boolean established) {
        synchronized (lockRoutingTable) {

            this.routingEntries.put(routername, new RoutingTableInfo(nextHop, cost, myport, portclass, activated, established));
        }
    }

    public int getNextHop(int myport) {
        synchronized (lockRoutingTable) {
            int senderport = 0;//???
            for (HashMap.Entry<String, RoutingTableInfo> entry : routingEntries.entrySet()) {

                if (entry.getValue().port == myport) {
                    senderport = entry.getValue().nextHop;
                }
            }

            return senderport;
        }
    }

    public void activateEntry(int nexthop) {
        synchronized (lockRoutingTable) {
            for (HashMap.Entry<String, RoutingTableInfo> entry : routingEntries.entrySet()) {

                if (entry.getValue().nextHop == nexthop) {
                    entry.getValue().setActivated(true);
                }
            }
        }
    }

    public void deactivateEntry(int nexthop) {
        synchronized (lockRoutingTable) {
            for (HashMap.Entry<String, RoutingTableInfo> entry : routingEntries.entrySet()) {

                if (entry.getValue().nextHop == nexthop) {
                    entry.getValue().setActivated(false);
                }
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

    public Port getPortClass(int myport) {
        Port p = null;
        for (HashMap.Entry<String, RoutingTableInfo> entry : routingEntries.entrySet()) {

            if (entry.getValue().port == myport) {
                p = entry.getValue().getPortclass();
            }
        }
        return p;
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

    public void updateEntry(String destNtwk, int nxthopIp, int cost) {
        synchronized (lockRoutingTable) {
            this.routingEntries.get(destNtwk).setCost(cost);
            this.routingEntries.get(destNtwk).setNextHop(nxthopIp);

        }
    }

//this method checks if it contains its port
    public boolean containsPort(int port) {
        synchronized (lockRoutingTable) {
            boolean contains = false;
            for (HashMap.Entry<String, RoutingTableInfo> entry : routingEntries.entrySet()) {

                if (entry.getValue().port == port) {
                    contains = true;
                }
            }

            return contains;
        }
    }

    public boolean containsNextHop(int port) {
        synchronized (lockRoutingTable) {
            boolean contains = false;
            for (HashMap.Entry<String, RoutingTableInfo> entry : routingEntries.entrySet()) {

                if (entry.getValue().nextHop == port) {
                    contains = true;
                }
            }

            return contains;
        }
    }

    public boolean isExistandNotActive(int port) {
        synchronized (lockRoutingTable) {
            boolean contains = false;
            for (HashMap.Entry<String, RoutingTableInfo> entry : routingEntries.entrySet()) {

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
    public void toString(String hint) {
        // creating iterator for HashMap 
        synchronized (this) {

//            Iterator<HashMap.Entry<InetAddress, RoutingTableInfo>> routingEntriesIterator = routingEntries.entrySet().iterator();
            Iterator<HashMap.Entry<String, RoutingTableInfo>> routingEntriesIterator = routingEntries.entrySet().iterator();

//            InetAddress destAddress;
            String destAddress;
            System.out.print("---------------------------------   Routing Table " + hint + " -----------------------------------" + "\n");
            System.out.print("---------------------|------------------|-----------------------|---------------|------------------------" + "\n");
            System.out.print("---------------------|\tDest Network \t|\tNext Hop Port\t|\tCost\tmyport\t|------------------------" + "\n");
            System.out.print("---------------------|------------------|-----------------------|---------------|------------------------" + "\n");

            while (routingEntriesIterator.hasNext()) {

//                HashMap.Entry<InetAddress, RoutingTableInfo> pair = (HashMap.Entry<InetAddress, RoutingTableInfo>) routingEntriesIterator.next();
                HashMap.Entry<String, RoutingTableInfo> pair = (HashMap.Entry<String, RoutingTableInfo>) routingEntriesIterator.next();

//                destAddress = (InetAddress) pair.getKey();
                destAddress = (String) pair.getKey();
                RoutingTableInfo destForwardingInfo = (RoutingTableInfo) pair.getValue();
//destAddress.getHostAddress()
                System.out.print("---------------------|\t" + destForwardingInfo.activated + "\t|\t");//bs ntb3 linet address btbi3to 3m berj3 forword slash bas destAddress.getHostName() 3m trj3 aw2et msln one.one.one.
                System.out.print("" + destForwardingInfo.nextHop + "\t\t|\t  ");
                System.out.print(destForwardingInfo.cost + " \t|------------------------");
                System.out.print(destForwardingInfo.port + " \t|------------------------");
                System.out.println();
            }
            System.out.print("---------------------|------------------|-----------------------|---------------|------------------------" + "\n");
            System.out.print("---------------------------------------------------------------------------------------------------------" + "\n");

        }
    }

//    public void printTable(String hint) {
//        // creating iterator for HashMap 
//        synchronized (this) {
//
////            Iterator<HashMap.Entry<InetAddress, RoutingTableInfo>> routingEntriesIterator = routingEntries.entrySet().iterator();
//            Iterator<HashMap.Entry<String, RoutingTableInfo>> routingEntriesIterator = routingEntries.entrySet().iterator();
//
////            InetAddress destAddress;
//            String destAddress;
//            System.out.print("----------------   Routing Table " + hint + " -----------------------------------------" + "\n");
//            System.out.print("-----------|----------------|----------------|---------|----------|-------------|------" + "\n");
//            System.out.print("-----------|  Dest Network  |  Next Hop Port |   Cost  |  myport  |  Activated  |------" + "\n");
//            System.out.print("-----------|----------------|----------------|---------|----------|-------------|------" + "\n");
//
//            while (routingEntriesIterator.hasNext()) {
//
////                HashMap.Entry<InetAddress, RoutingTableInfo> pair = (HashMap.Entry<InetAddress, RoutingTableInfo>) routingEntriesIterator.next();
//                HashMap.Entry<String, RoutingTableInfo> pair = (HashMap.Entry<String, RoutingTableInfo>) routingEntriesIterator.next();
//
////                destAddress = (InetAddress) pair.getKey();
//                destAddress = (String) pair.getKey();
//                RoutingTableInfo destForwardingInfo = (RoutingTableInfo) pair.getValue();
////destAddress.getHostAddress()
//                System.out.print("-----------|\t" + destAddress + "\t    |\t");//bs ntb3 linet address btbi3to 3m berj3 forword slash bas destAddress.getHostName() 3m trj3 aw2et msln one.one.one.
//                System.out.print(" " + destForwardingInfo.nextHop + "\t    |    ");
//                System.out.print(destForwardingInfo.cost + "      |   ");
//                System.out.print(destForwardingInfo.port + "   |    ");
//                System.out.print(destForwardingInfo.activated + "\t|------");
//
//                System.out.println();
//            }
//            System.out.print("-----------|----------------|----------------|---------|----------|-------------|------" + "\n");
//            System.out.print("---------------------------------------------------------------------------------------" + "\n");
//
//        }
//    }
    public void printTable(String hint) {
        // creating iterator for HashMap 
        synchronized (this) {

//            Iterator<HashMap.Entry<InetAddress, RoutingTableInfo>> routingEntriesIterator = routingEntries.entrySet().iterator();
            Iterator<HashMap.Entry<String, RoutingTableInfo>> routingEntriesIterator = routingEntries.entrySet().iterator();

//            InetAddress destAddress;
            String destAddress;
            System.out.print("----------------   Routing Table " + hint + " -----------------------------------------" + "\n");
            System.out.print("-----------|----------------|----------------|---------|----------|-------------|------" + "\n");
            System.out.print("-----------|  Dest Network  |  Next Hop Port |   Cost  |  myport  |  Activated  |  Established |------" + "\n");
            System.out.print("-----------|----------------|----------------|---------|----------|-------------|--------------|-" + "\n");

            while (routingEntriesIterator.hasNext()) {

//                HashMap.Entry<InetAddress, RoutingTableInfo> pair = (HashMap.Entry<InetAddress, RoutingTableInfo>) routingEntriesIterator.next();
                HashMap.Entry<String, RoutingTableInfo> pair = (HashMap.Entry<String, RoutingTableInfo>) routingEntriesIterator.next();

//                destAddress = (InetAddress) pair.getKey();
                destAddress = (String) pair.getKey();
                RoutingTableInfo destForwardingInfo = (RoutingTableInfo) pair.getValue();
//destAddress.getHostAddress()
                System.out.print("-----------|\t" + destAddress + "\t    |\t");//bs ntb3 linet address btbi3to 3m berj3 forword slash bas destAddress.getHostName() 3m trj3 aw2et msln one.one.one.
                System.out.print(" " + destForwardingInfo.nextHop + "\t    |    ");
                System.out.print(destForwardingInfo.cost + "      |   ");
                System.out.print(destForwardingInfo.port + "   |    ");
                System.out.print(destForwardingInfo.activated + "   |    ");
                System.out.print(destForwardingInfo.established + "\t|------");

                System.out.println();
            }
            System.out.print("-----------|----------------|----------------|---------|----------|-------------|------------|--" + "\n");
            System.out.print("---------------------------------------------------------------------------------------" + "\n");

        }
    }
}
