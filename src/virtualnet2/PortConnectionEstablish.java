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
    int neighborport;
    InetAddress neighborip;
    int myport;

    RoutingTable rt;

    public PortConnectionEstablish(int myport, InetAddress neighborip, int neighborport, Port p, RoutingTable rt) {

        this.neighborport = neighborport;
        this.myport = myport;
        this.p = p;
        this.neighborip = neighborip;

        this.rt = rt;
    }

    @Override
    public void run() {

        boolean bool;
//        if (!p.isconnectionEstablished()) {

        try {
            rt.printTable("**Checking**");
            System.out.println("*establishing connection with ip=" + neighborip + " port=" + neighborport);
            socket = new Socket(neighborip, neighborport);
            System.out.println("*socket : myport " + socket.getLocalPort() + " destport " + socket.getPort());

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(new Neighbor(InetAddress.getLocalHost(), myport));
            System.out.println("*sending my self as a neighbor to ip=" + InetAddress.getLocalHost() + " port=" + myport);

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            bool = objectInputStream.readBoolean();

            System.out.println("*" + bool + " was recieved");

            if (bool) {
                rt.activateEntry(neighborport);
                p.setSocket(socket);
                p.setconnectionEstablished(true);
                System.out.println("*connection is established at port " + myport + " with neighb = " + neighborip + " , " + neighborport);

                //rt.addEntry(ip, port, 1 ,myport, p, true);
                rt.addEntry(neighborport, neighborport, 1, myport, p, true);

            } else {
                System.out.println("*waiting a connection from " + neighborport);

                socket.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(PortConnectionEstablish.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
//    }
}
