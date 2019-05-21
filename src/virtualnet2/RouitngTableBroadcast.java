/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import virtualnet2.RoutingTableSend;

/**
 *
 * @author maria afara
 */
public class RouitngTableBroadcast extends Thread {

    private RoutingService rs;
    //InetAddress routerAddress;
    ArrayList<Socket> sockets;

    public RouitngTableBroadcast() throws SocketException {

        //this.routerAddress = routerAddress;

        rs = RoutingService.getInstance();

    }

    /*
        *This method first establishes a socket connection with the dirclty connected network  (neighbors)
        * This method then sends routing table to all the neighbors every 60 or 30 sec.
     */
    @Override
    public void run() {

        for (Neighbor n : rs.getNeighbors()) {
            try {
                sockets.add(new Socket(n.neighborAddress, n.neighborPort));
            } catch (IOException ex) {
                Logger.getLogger(RouitngTableBroadcast.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //for the first time lzm tb3t request lal neighs ino yb3tula lrt 
        //l2elonn b3den btb3t le 2ela wbtsh8el ltimer
        while (true) {
            try {
                for (int i = 0; i < sockets.size(); i++) {
                    new RoutingTableSend(sockets.get(i)).start();
                }

                Thread.sleep(60000);

            } catch (InterruptedException ex) {
                Logger.getLogger(RouitngTableBroadcast.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
