package virtualnet2;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import virtualnet2.Router;
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

    DatagramSocket socket;
    RoutingTable routingTable;//the one recieved
    InetAddress recievedrouterInetAddress;//tb3 lrouter le ba3at
    int recievedrouterPort;

    public ListenAtPort(int port) {
        try {
            //Creating datagram socket

            System.out.println("--" + port + " i am created");

            socket = new DatagramSocket(port);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        DatagramPacket packet1;
        //  keep receiving packets
        while (true) {
            try {
                //recieve the hint message which is either request update or delete
                byte[] data1 = new byte[500];
                packet1 = new DatagramPacket(data1, data1.length);
                socket.receive(packet1);
                String datafrompacket = new String(packet1.getData(), 0, packet1.getLength());

                //if its comming after a request or an update recieve after it a routing table
                if (datafrompacket.equalsIgnoreCase("Request") || datafrompacket.equalsIgnoreCase("Update")) {

                    //if my routing table is formed send the response 
                    if (!Router.myRoutingTable.isEmptyTable()) {
                        SendRoutingTable sendingroutingtablethread = new SendRoutingTable(Router.neighbors, socket);
                        sendingroutingtablethread.start();
                    }
                    //store the second received packet into byte array
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    byte[] data = packet.getData();

                    // get byte date into ObjectInputStream
                    ByteArrayInputStream in = new ByteArrayInputStream(data);
                    ObjectInputStream is = new ObjectInputStream(in);

                    // get RoutingTable object which is reccieved
                    routingTable = (RoutingTable) is.readObject();
                    //"Routing table received from " + packet.getAddress().getHostAddress()

                    System.out.print("\n");
                    routingTable.printTable("Recieved from " + packet.getPort() + " ");
                    System.out.println("\n");

                    // Check if this routing table's object needs to be updated
                    recievedrouterInetAddress = packet.getAddress();

                    recievedrouterPort = packet.getPort();

                    Updater updaterthread = new Updater(routingTable, recievedrouterInetAddress, recievedrouterPort, socket);

                }//else if a failure was noticed from a certain neighbor
                else if (datafrompacket.equalsIgnoreCase("Failure")) {
                    System.out.println("failure it is");
                    //  InetAddress FailedNode = packet1.getAddress();
                    //  Router.myRoutingTable.deleteEntry(FailedNode);
                    //  Router.sendFailure(FailedNode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
