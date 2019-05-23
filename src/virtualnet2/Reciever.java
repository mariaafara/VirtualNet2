/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maria afara
 */
public class Reciever extends Thread {

    private Socket socket;

    private RoutingService rs;

    private int port;

    private Object recievedObject;
    ObjectInputStream ois;

    public Reciever(int port, Socket socket, RoutingService rs) {
        this.port = port;
        this.socket = socket;
        this.rs = rs;

    }

    @Override
    public void run() {
        try {
            ois = new ObjectInputStream(socket.getInputStream());

            while (true) {
                recievedObject = ois.readObject();
                if (recievedObject instanceof RoutingTable) {
                    new RoutingTableRecieve(recievedObject, port, socket, rs).start();
                } else if (recievedObject instanceof FailedNode) {
                    new FailedNodeRecieve(recievedObject, socket, rs).start();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Reciever.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Reciever.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
