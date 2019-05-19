package virtualnet2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import virtualnet2.RoutingTable;
import virtualnet2.SendRoutingTable;
import virtualnet2.Updater;

/**
 * This class listens on given port for incoming routing table information or
 * forwarding message from neighbors
 *
 * @author maria afara
 */
public class ListenAtPort extends Thread {

    InetAddress recieveipAddress;
    int recieveport;

    Socket socket;
    ServerSocket serversocket;

    RoutingTable routingTable;//the one recieved

    private RoutingService rs;
    private int i = 0;

    public ListenAtPort(int port) {

//        this.routerport = port;
//        this.routeripAddress = ipAddress;
        try {
            //Creating server socket
            serversocket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(ListenAtPort.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        try {

            socket = serversocket.accept();

            rs = RoutingService.getInstance();

        } catch (IOException ex) {
            Logger.getLogger(ListenAtPort.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {

            //if my routing table has been formed send the response 
            if (!rs.isEmptyTable()) {
                if (i == 0) {
                    i++;
                    new SendRoutingTable(socket).start();
                }

                try {
                    //recieve routing table
                    routingTable = recieveRoutingTable();
                    recieveipAddress = socket.getInetAddress();

                    //gets the port of which the router sent the RT  from.
                    for (Neighbor n : rs.getNeighbors()) {
                        if (n.neighborAddress.equals(recieveipAddress)) {
                            recieveport = n.neighborPort;
                        }
                    }

                    System.out.print("\n");
                    routingTable.printTable("Recieved from " + recieveport + " ");
                    System.out.println("\n");

                    // Check if this routing table's object needs to be updated
                    new Updater(routingTable, recieveipAddress, recieveport, socket).start();

//                }//else if a failure was noticed from a certain neighbor
//                else if (datafrompacket.equalsIgnoreCase("Failure")) {
//                    System.out.println("failure it is");
//                    //  InetAddress FailedNode = packet1.getAddress();
//                    //  Router.myRoutingTable.deleteEntry(FailedNode);
//                    //  Router.sendFailure(FailedNode);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
                } catch (IOException ex) {
                    Logger.getLogger(ListenAtPort.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ListenAtPort.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private RoutingTable recieveRoutingTable() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        // get RoutingTable object which is reccieved
        return routingTable = (RoutingTable) ois.readObject();
    }
}
