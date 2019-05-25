/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * this service sends routing table to all the directly connected neighbors
 *
 * @author maria afara
 */
public class RoutingTableSend extends Thread {

    private Socket socket;
    private RoutingTable rt;

    static int i = 0;

    public RoutingTableSend(Socket socket, RoutingTable rt) {

        this.socket = socket;
        this.rt = rt;

    }

    /*
        * This method sends the routing table to all it's neighbors
     */
    @Override
    public void run() {

        // publish routing table here.
        System.out.println("*SendingRoutingTable");

        rt.getRoutingTable().printTable("Sending");
        //send myRoutingTable to neighbor
        sendRoutingTable(rt.getRoutingTable());

    }
    
    /*
        * This method sends given routing table 
     */
    private void sendRoutingTable(RoutingTable RT) {

        try {
            //  ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            oos.writeObject(RT);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
