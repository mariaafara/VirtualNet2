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
        ///  for (HashMap.Entry<Integer, Neighbor> entry : rs.getConnections().entrySet()) {

        //send myRoutingTable to neighbor
        sendRoutingTable(rs.getRoutingTable());//, entry.getValue().neighborAddress, entry.getValue().neighborPort);

        // }
    }

    /*
        * This method sends given routing table 
     */
    private void sendRoutingTable(RoutingTable rt) {
        try {
            //  ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            oos.writeObject(rt);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

  
}
