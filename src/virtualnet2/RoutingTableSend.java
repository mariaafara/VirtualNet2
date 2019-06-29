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

    private ObjectOutputStream oos;
    private RoutingTable rt;

    public RoutingTableSend(ObjectOutputStream oos, RoutingTable rt) {

        this.oos = oos;
        this.rt = rt;

    }

    /*
        * This method sends the routing table to all it's neighbors
     */
    @Override
    public void run() {

        // publish routing table here.
        //  System.out.println("*SendingRoutingTable");
        rt.printTable("Sending");
        //send myRoutingTable to neighbor
        sendRoutingTable(rt);

    }

    /*
        * This method sends given routing table 
     */
    private void sendRoutingTable(RoutingTable RT) {

        try {
            
            //  ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(RT);
            oos.flush();
            oos.reset();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
