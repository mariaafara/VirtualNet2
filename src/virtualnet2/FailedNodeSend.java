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
import java.util.HashMap;

/**
 *
 * @author maria afara
 */
public class FailedNodeSend extends Thread {

    private Socket socket;
    private FailedNode fn;

    public FailedNodeSend(FailedNode fn, Socket socket) {

        this.socket = socket;

        this.fn = fn;
    }

    /*
        * This method sends the routing table to all it's neighbors
     */
    @Override
    public void run() {

        System.out.println("SendingFailedNode");

        sendFailedNode(fn);

    }

    /*
        * This method sends given failed node
     */
    private void sendFailedNode(FailedNode fn) {
        synchronized (this) {
            try {

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                oos.writeObject(fn);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
