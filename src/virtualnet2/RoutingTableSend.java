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
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * this service sends routing table to all the directly connected neighbors
 *
 * @author maria afara
 */
public class RoutingTableSend extends Thread {

    private static Socket socket;
    private RoutingService rs;

    static int i = 0;

    public RoutingTableSend(Socket socket, RoutingService rs) {

        this.socket = socket;
        this.rs = rs;

    }

    /*
        * This method sends the routing table to all it's neighbors
     */
    @Override
    public void run() {

        // publish routing table here.
        System.out.println("SendingRoutingTable");
        for (HashMap.Entry<Integer, Neighbor> entry : rs.getConnections().entrySet()) {

            //send myRoutingTable to neighbor
            sendRoutingTable(rs.getRoutingTable(), entry.getValue().neighborAddress, entry.getValue().neighborPort);

        }

    }

    /*
        * This method sends given routing table to given i p and port via t c p protocol
     */
    private void sendRoutingTable(RoutingTable rt, InetAddress ip, int port) {
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            oos.writeObject(rt);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * This method sends the failed node 
    
     */
//    private void sendFailure(InetAddress FailedNode) {
//        DatagramPacket packet1;
//        byte[] data;
//        data = (new String(FailedNode + "")).getBytes();
//        DatagramPacket packet;
//        byte[] data1;
//        data1 = (new String("Failure")).getBytes();
//        //("Sending failed node  to neighbors.");
//        Iterator<Neighbor> neighborIterator = neighbors.iterator();
//        try {
//            while (neighborIterator.hasNext()) {
//
//                //send failure keyword first as a hint
//                Neighbor nextNeighbor = neighborIterator.next();
//                packet1 = new DatagramPacket(data1, data1.length, nextNeighbor.neighborAddress, nextNeighbor.neighborPort);
//                socket.send(packet1);
//                //send failed node after it
//                packet = new DatagramPacket(data, data.length, nextNeighbor.neighborAddress, nextNeighbor.neighborPort);
//                socket.send(packet);
//
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
