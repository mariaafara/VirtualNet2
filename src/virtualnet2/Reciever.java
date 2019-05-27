/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maria afara
 */
public class Reciever extends Thread {

    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;
    private RoutingTable rt;

    private int port;

    private Object recievedObject;

    //   PortConxs portConxs;
    public Reciever(int port, ObjectInputStream ois, ObjectOutputStream oos, RoutingTable rt) {
        System.out.println("*reciever initialized");
        this.port = port;
        this.ois = ois;
        this.oos = oos;
        this.rt = rt;
        // System.out.println("*socket in reciever local= " + socket.getLocalPort() + " port=" + socket.getPort());
        //  this.portConxs = portConxs;

    }

    @Override
    public void run() {

        try {
            //  portConxs.getPortInstance(port).wait();

            int i = 1;
            while (true) {

                System.out.println("*waiting to recieve object   " + i);
                //System.out.println("*reciever* socket :myport " + socket.getLocalPort() + " destport " + socket.getPort());

                recievedObject = ois.readObject();
                i++;
                System.out.println("*recieved object =" + recievedObject);
                if (recievedObject instanceof RoutingTable) {
                    System.out.println("*recieved routing table");

                    new RoutingTableRecieve(recievedObject, port, ois, oos, rt).start();
                } else if (recievedObject instanceof FailedNode) {
                    //        new FailedNodeRecieve(recievedObject, socket, rt).start();
                } else {
                    System.out.println("*recieved unknown type of object " + recievedObject.getClass());
                }
                Thread.sleep(2000);
            }
        } catch (IOException ex) {
            Logger.getLogger(Reciever.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Reciever.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Reciever.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
