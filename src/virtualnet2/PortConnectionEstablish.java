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
    int myport;

    public PortConnectionEstablish(int myport, InetAddress ip, int port, Port p) {

        System.out.println("*****");
        System.out.println("myport is " + myport);
        this.port = port;
        this.myport = myport;
        this.p = p;
        this.ip = ip;
    }

    @Override
    public void run() {

        boolean bool;
        if (!p.isconnectionEstablished()) {

            try {
                System.out.println("---------");
                socket = new Socket(ip, port);
                System.out.println("---------");

//**********
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    System.out.println("---------");
                    bool = objectInputStream.readBoolean();
                
//                    objectInputStream.close();
                System.out.println(bool + " was recieved");

                if (bool) {
                    p.setSocket(socket);
                    p.setconnectionEstablished(true);
                    //3m b3tlo m3 min lconnection
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(new Neighbor(ip, myport));
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
