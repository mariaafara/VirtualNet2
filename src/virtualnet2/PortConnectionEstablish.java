/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maria afara
 */
public class PortConnectionEstablish extends Thread {

    private Port p;
    Socket socket;
    int port;
    InetAddress ip;

    public PortConnectionEstablish(InetAddress ip, int port, Port p) {
        this.port = port;
        this.p = p;
        this.ip = ip;
    }

    @Override
    public void run() {
        //  ObjectOutputStream objectOutputStream = null;
        ObjectInputStream objectInputStream = null;
        if (!p.connectionEstablished) {

            try {
                socket = new Socket(ip, port);
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                boolean bool = objectInputStream.readBoolean();
                if (bool) {
                    p.setSocket(socket);
                    p.setconnectionEstablished(true);
                } else {
                    System.out.println("Sorry connction already established with this destination");
                    //cannot connect to  this cnx
                }

            } catch (IOException ex) {
                Logger.getLogger(PortConnectionEstablish.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
