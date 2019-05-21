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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maria afara
 */
public class PortConnectionEstablish extends Thread {

    private Port p;
    Socket socket;

    public PortConnectionEstablish(int port) {
        p = Port.getInstance(port);
    }

    @Override
    public void run() {
        //  ObjectOutputStream objectOutputStream = null;
        ObjectInputStream objectInputStream = null;
        while (true) {
            //iza b3d ma fi cnx wfi cnx t7dadet bda tn3mal 
            if (!p.connectionEstablished && !p.getConnection().isEmpty()) {

                for (HashMap.Entry<InetAddress, Integer> entry : p.getConnection().entrySet()) {

                    try {
                        socket = new Socket(entry.getKey(), entry.getValue());
                        objectInputStream = new ObjectInputStream(socket.getInputStream());
                        boolean bool = objectInputStream.readBoolean();
                        if (bool) {
                            p.setSocket(socket);
                            p.setconnectionEstablished(true);
                        } else {
                            p.resetConnection();
                            //cannot connect to  this cnx
                        }

                    } catch (IOException ex) {
                        Logger.getLogger(PortConnectionEstablish.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}
