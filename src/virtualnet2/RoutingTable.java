package virtualnet2;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
    // HashMap<String, RoutingTableInfo> routingEntries;
    HashMap<RoutingTableKey, RoutingTableInfo> routingEntries;

    //transient krmel ma ynb3to lobjects manon serilizable kmn 
    transient final Object lockRoutingTable = new Object();
    transient final Object lockPortconxs = new Object();

    public RoutingTable() {

//            routingEntries = new HashMap<InetAddress, RoutingTableInfo>();
//            routingEntries = new HashMap<String, RoutingTableInfo>();
        routingEntries = new HashMap<>();

    }
//if itcontain this network with the ip and port set the established boolean to true

    public void establishEntry(InetAddress ip, String hostname) {

        synchronized (lockRoutingTable) {
            RoutingTableKey ipHost = new RoutingTableKey(ip, hostname);
            System.out.println("establish entry for " + ip + " " + hostname + "\n");
            routingEntries.get(ipHost).setEstablished(true);

        }
    }

    public void establishEntry(RoutingTableKey ipPort) {
        synchronized (lockRoutingTable) {
            if (routingEntries.containsKey(ipPort)) {
                System.out.println("\n*" + ipPort.toString() + "  exist in routing table and is established now");

                routingEntries.get(ipPort).setEstablished(true);
            } else {
                System.out.println("\n*" + ipPort.toString() + " does not exist in routing table");
            }
        }
    }

///this method return the entryof the given  network or name
    public RoutingTableInfo getEntry(InetAddress ip, String hostname) {
        synchronized (lockRoutingTable) {
            RoutingTableKey ipHost = new RoutingTableKey(ip, hostname);
            return routingEntries.get(ipHost);
        }
    }

    public RoutingTableInfo getEntry(RoutingTableKey ipPort) {
        synchronized (lockRoutingTable) {

            return routingEntries.get(ipPort);
        }
    }

    public RoutingTableInfo getEntry(int portDst) {
        synchronized (lockRoutingTable) {
            for (HashMap.Entry<RoutingTableKey, RoutingTableInfo> entry : routingEntries.entrySet()) {
                if (entry.getValue().getNextHop() == portDst) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    public boolean isEstablishedEntry(InetAddress ip, String hostname) {
        synchronized (lockRoutingTable) {
            RoutingTableKey ipHost = new RoutingTableKey(ip, hostname);
            if (routingEntries.containsKey(ipHost)) {
                System.out.println(ip + " , " + hostname + " in routing table\n");
            } else {
                System.out.println(ip + " , " + hostname + " not in routing table\n");
            }

            return routingEntries.get(ipHost).isEstablished();
        }
    }

    /*
	 * This method adds an entry into the routing table
	 * @para synchronized (lockRoutingTable) {m destIP = destination  IP address
	 * @param nextHop = nextHop IP address
	 * @param cost = Cost to reach the destination
     */
    public void addEntry(InetAddress destIp, String hostname, int nextHop, int cost, int myport, Port portclass, boolean activated, boolean established) throws UnknownHostException {
        synchronized (lockRoutingTable) {
            RoutingTableKey ipPort = new RoutingTableKey(destIp, hostname);
            this.routingEntries.put(ipPort, new RoutingTableInfo(nextHop, cost,hostname, myport, portclass, activated, established));
        }
    }

    public void addEntry(RoutingTableKey ipPort, String hostname, int nextHop, int cost, int myport, Port portclass, boolean activated, boolean established) throws UnknownHostException {
        synchronized (lockRoutingTable) {

            this.routingEntries.put(ipPort, new RoutingTableInfo(nextHop, cost,hostname, myport, portclass, activated, established));
        }
    }
//    public void addEntry(String routername, int nextHop, int cost, int myport, Port portclass, boolean activated, boolean established) {
//        synchronized (lockRoutingTable) {
//
//            this.routingEntries.put(routername, new RoutingTableInfo(nextHop, cost, myport, portclass, activated, established));
//        }
//    }

    public int getNextHop(int myport) {
        synchronized (lockRoutingTable) {

            // return routingEntries.get(ipPort).nextHop;
            for (HashMap.Entry<RoutingTableKey, RoutingTableInfo> entry : routingEntries.entrySet()) {

                if (entry.getValue().port == myport) {
                    return entry.getValue().nextHop;
                }
            }

            return 0;
        }

    }

    public void activateEntry(InetAddress ip, String hostname) {
        synchronized (lockRoutingTable) {
            RoutingTableKey ipHost = new RoutingTableKey(ip, hostname);
            routingEntries.get(ipHost).setActivated(true);

        }
    }

    public void deactivateEntry(InetAddress ip,String hostname) {
        synchronized (lockRoutingTable) {
            RoutingTableKey ipHost = new RoutingTableKey(ip, hostname);
            routingEntries.get(ipHost).setActivated(false);
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
    public void deleteEntry(InetAddress ip, String hostname) {
        synchronized (lockRoutingTable) {
            RoutingTableKey ipHost = new RoutingTableKey(ip, hostname);
            this.routingEntries.remove(ipHost);
        }
    }

    public Port getPortClass(int myport) {
        Port p = null;
        for (HashMap.Entry<RoutingTableKey, RoutingTableInfo> entry : routingEntries.entrySet()) {

            if (entry.getValue().port == myport) {
                p = entry.getValue().getPortclass();
            }
        }
        return p;
    }

    /*
	 * this method updates cost to a given destination and its next hop
     */
    public void updateEntry(InetAddress destNtwk,String hostname, int nxthopIp, int cost) {
        synchronized (lockRoutingTable) {
            RoutingTableKey ipHost = new RoutingTableKey(destNtwk, hostname);
            this.routingEntries.get(ipHost).setCost(cost);
            this.routingEntries.get(ipHost).setNextHop(nxthopIp);
        }
    }
//this method updates the cost and next hop used when updating routing table only

//    public void updateEntry(String destNtwk, int nxthopIp, int cost) {
//        synchronized (lockRoutingTable) {
//            this.routingEntries.get(destNtwk).setCost(cost);
//            this.routingEntries.get(destNtwk).setNextHop(nxthopIp);
//
//        }
//    }
//this method checks if it contains its port
    public boolean containsPort(int port) {
        synchronized (lockRoutingTable) {
            boolean contains = false;
            for (HashMap.Entry<RoutingTableKey, RoutingTableInfo> entry : routingEntries.entrySet()) {

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
            for (HashMap.Entry<RoutingTableKey, RoutingTableInfo> entry : routingEntries.entrySet()) {

                if (entry.getValue().nextHop == port) {
                    contains = true;
                }
            }

            return contains;
        }
    }

    public boolean isExistandNotActive(InetAddress ip,String hostname) {
        synchronized (lockRoutingTable) {
            boolean contains = false;

            RoutingTableKey ipHost= new RoutingTableKey(ip, hostname);

            //   System.out.println("\n\n* @ and port "+ipPort.ip+" , "+ipPort.port);
            if (routingEntries.containsKey(ipHost)) {
                System.out.println("\n*" + ipHost.toString() + "  exist in routing table ");
            } else {
                System.out.println("\n*" + ipHost.toString() + " does not exist in routing table ");
            }

            return routingEntries.containsKey(ipHost) && !routingEntries.get(ipHost).activated;

        }
    }

    /*
	 * This method prints the routing table entries
     */
    public void toString(String hint) {
        // creating iterator for HashMap 
        synchronized (this) {

//            Iterator<HashMap.Entry<InetAddress, RoutingTableInfo>> routingEntriesIterator = routingEntries.entrySet().iterator();
            Iterator<HashMap.Entry<RoutingTableKey, RoutingTableInfo>> routingEntriesIterator = routingEntries.entrySet().iterator();

            //  InetAddress destAddress;
            String destAddress;
            System.out.print("---------------------------------   Routing Table " + hint + " -----------------------------------" + "\n");
            System.out.print("---------------------|------------------|-----------------------|---------------|------------------------" + "\n");
            System.out.print("---------------------|\tDest Network \t|\tNext Hop Port\t|\tCost\tmyport\t|------------------------" + "\n");
            System.out.print("---------------------|------------------|-----------------------|---------------|------------------------" + "\n");

            while (routingEntriesIterator.hasNext()) {

//                HashMap.Entry<InetAddress, RoutingTableInfo> pair = (HashMap.Entry<InetAddress, RoutingTableInfo>) routingEntriesIterator.next();
                HashMap.Entry<RoutingTableKey, RoutingTableInfo> pair = (HashMap.Entry<RoutingTableKey, RoutingTableInfo>) routingEntriesIterator.next();

                destAddress = pair.getKey().getIp() + "-" + pair.getKey().getHostname();

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
            Iterator<HashMap.Entry<RoutingTableKey, RoutingTableInfo>> routingEntriesIterator = routingEntries.entrySet().iterator();

//            InetAddress destAddress;
            String destAddress;
            System.out.print("----------------   Routing Table " + hint + " -----------------------------------------" + "\n");
            System.out.print("-----------|----------------|----------------|---------|----------|-------------|------" + "\n");
            System.out.print("-----------|  Dest Network  |  Next Hop Port |   Cost  |  myport  |  Activated  |  Established |------" + "\n");
            System.out.print("-----------|----------------|----------------|---------|----------|-------------|--------------|-" + "\n");

            while (routingEntriesIterator.hasNext()) {

//                HashMap.Entry<InetAddress, RoutingTableInfo> pair = (HashMap.Entry<InetAddress, RoutingTableInfo>) routingEntriesIterator.next();
                HashMap.Entry<RoutingTableKey, RoutingTableInfo> pair = (HashMap.Entry<RoutingTableKey, RoutingTableInfo>) routingEntriesIterator.next();

                destAddress = pair.getKey().getIp().getHostAddress() + "-" + pair.getKey().getHostname();
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
