/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * this service sends routing table to all the directly connected neighbors
 *
 * @author maria afara
 */
public class SendRoutingTable extends Thread {

    private static ArrayList<Neighbor> neighbors;
    private static DatagramSocket socket;

    static int i = 0;

    public SendRoutingTable(ArrayList<Neighbor> neighbors, DatagramSocket socket) throws SocketException {
        this.neighbors = neighbors;
        this.socket = socket;

    }

    /*
     * This method sends the routing table to all it's neighbors
     */
    @Override
    public void run() {

        System.out.println("SendingRoutingTable");
        Iterator<Neighbor> neighborIterator = neighbors.iterator();

        while (neighborIterator.hasNext()) {
            Neighbor nextNeighbor = neighborIterator.next();

            try {

                //send request in first time in the rest send update
                sendString(nextNeighbor.neighborAddress, nextNeighbor.neighborPort);
                //send myRoutingTable to neighbor
                sendObj(Router.myRoutingTable, nextNeighbor.neighborAddress, nextNeighbor.neighborPort);

            } catch (IOException ex) {
                Logger.getLogger(SendRoutingTable.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /**
     * This method sends given object to given ip and port via udp protocol
     *
     * @param obj
     * @param ip
     * @param port
     *
     */
    private void sendObj(Object obj, InetAddress ip, int port) {
        try {
            // Pack object into datagram packet
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(obj);
            byte[] data = baos.toByteArray();

            DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
            socket.send(packet);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendString(InetAddress ip, int port) throws IOException {

        try {
            byte[] data;

            if (i == 0) {
                data = (new String("Request")).getBytes();
                i++;
            } else {
                data = (new String("Update")).getBytes();
            }
            DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * This method sends the failed node 
    
     */
    private void sendFailure(InetAddress FailedNode) {
        DatagramPacket packet1;
        byte[] data;
        data = (new String(FailedNode + "")).getBytes();
        DatagramPacket packet;
        byte[] data1;
        data1 = (new String("Failure")).getBytes();
        //("Sending failed node  to neighbors.");
        Iterator<Neighbor> neighborIterator = neighbors.iterator();
        try {
            while (neighborIterator.hasNext()) {

                //send failure keyword first as a hint
                Neighbor nextNeighbor = neighborIterator.next();
                packet1 = new DatagramPacket(data1, data1.length, nextNeighbor.neighborAddress, nextNeighbor.neighborPort);
                socket.send(packet1);
                //send failed node after it
                packet = new DatagramPacket(data, data.length, nextNeighbor.neighborAddress, nextNeighbor.neighborPort);
                socket.send(packet);

            }
        } catch (IOException ex) {
            Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
