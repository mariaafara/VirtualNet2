/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import virtualnet2.Neighbor;
import virtualnet2.Router;
import virtualnet2.SendRoutingTable;

/**
 *
 * @author maria afara
 */
public class BroadcastRouitngTable extends Thread {

    private static ArrayList<Neighbor> neighbors;
    private static DatagramSocket socket;
    Router router;

    public BroadcastRouitngTable(ArrayList<Neighbor> neighbors, DatagramSocket socket) throws SocketException {
        this.neighbors = neighbors;
        this.socket = socket;

    }

    @Override
    public void run() {
//for the first time lzm tb3t request lal neighs ino yb3tula lrt l2elonn b3den btb3t le 2ela wbtsh8el ltimer

        while (true) {
            try {

                SendRoutingTable sendingroutingtablethread = new SendRoutingTable(neighbors, socket);
                sendingroutingtablethread.start();

                Thread.sleep(60000);

            } catch (SocketException ex) {
                Logger.getLogger(BroadcastRouitngTable.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(BroadcastRouitngTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
