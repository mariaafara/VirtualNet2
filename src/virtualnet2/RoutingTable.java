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

    HashMap<InetAddress, TableInfo> routingEntries;

    public RoutingTable() {
        try {
            routingEntries = new HashMap<InetAddress, TableInfo>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
	 * This method prints the routing table entries
     */
    public void printTable(String hint) {
        // creating iterator for HashMap 
        synchronized (this) {

            Iterator<HashMap.Entry<InetAddress, TableInfo>> routingEntriesIterator = routingEntries.entrySet().iterator();
            InetAddress destAddress;

            System.out.print("---------------------------------   Routing Table " + hint + " -----------------------------------" + "\n");
            System.out.print("---------------------|------------------|-----------------------|---------------|------------------------" + "\n");
            System.out.print("---------------------|\tDest Network \t|\tNext Hop Port\t|\tCost\t|------------------------" + "\n");
            System.out.print("---------------------|------------------|-----------------------|---------------|------------------------" + "\n");
            while (routingEntriesIterator.hasNext()) {

                HashMap.Entry<InetAddress, TableInfo> pair = (HashMap.Entry<InetAddress, TableInfo>) routingEntriesIterator.next();
                destAddress = (InetAddress) pair.getKey();
                TableInfo destForwardingInfo = (TableInfo) pair.getValue();

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
