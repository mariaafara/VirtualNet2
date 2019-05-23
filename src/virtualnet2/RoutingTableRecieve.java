/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maria afara
 */
public class RoutingTableRecieve extends Thread {

    int recieveport;

    Socket socket;

    RoutingTable routingTable;//the one recieved

    private RoutingService rs;
    private int i = 0;
    private int port;

    public RoutingTableRecieve(int port, Socket socket, RoutingService rs) {
        this.port = port;
        this.socket = socket;
        this.rs = rs;

    }

    @Override
    public void run() {

        while (true) {

            try { //recieve routing table
                routingTable = recieveRoutingTable();

                //if my routing table has been formed send the response
                if (!rs.isEmptyTable()) {
                    if (i == 0) {
                        i++;
                        new RoutingTableSend(socket, rs).start();
                    }

                    //gets the port of which the router sent the RT  from.
                    recieveport = rs.getConnections().get(port).getNeighborPort();

                    System.out.print("\n");
                    routingTable.printTable("Recieved from " + recieveport + " ");
                    System.out.println("\n");

// Check if this routing table's object needs to be updated
                    new RoutingTableUpdate(routingTable, recieveport, socket, rs).start();

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
                }
            } catch (IOException ex) {
                Logger.getLogger(RoutingTableRecieve.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(RoutingTableRecieve.class.getName()).log(Level.SEVERE, null, ex);
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
