package virtualnet2;

import sharedPackage.Neighbor;
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
    private Socket socket;
    private int neighborport;
    private InetAddress neighborip;

    private int myport;

    private RoutingTable rt;

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
        if (!p.isconnectionEstablished()) {

            try {

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
                    //  rt.activateEntry(neighborport);
                    p.setSocket(socket);
                    p.setStreams(objectInputStream, objectOutputStream);

                    p.setconnectionEstablished(true);
                    System.out.println("*connection is established at port " + myport + " with neighb = " + neighborip + " , " + neighborport);
                    //rt.addEntry(ip, port, 1 ,myport, p, true);
                    rt.addEntry(neighborip, neighborport, 1, myport, p, true, false);

                    ///sar jehez yst2bel 
                    //lneigh name hon bs krmel locally tssing bs b3den bdo ysir ip
                    System.out.println("\n*before initialize reciever at port " + myport + "\n");
                    new Reciever(neighborip, neighborport, myport, p.getOis(), p.getOos(), rt).start();
                    System.out.println("\n*after initialize reciever at port " + myport + "\n");
                    System.out.println("\n*neig name " + neighborip + "," + neighborport + "\n");
                    rt.printTable("--after add true--");
                } else {
                    //rt.addEntry(ip, port, 1 ,myport, p, true);
                    rt.addEntry(neighborip, neighborport, 1, myport, p, false, false);

                    System.out.println("*waiting a connection from " + neighborport);
                    //  rt.printTable("**Checking**");

                    rt.printTable("--after add false--");
                    socket.close();
                }

            } catch (IOException ex) {
                Logger.getLogger(PortConnectionEstablish.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            //already exist a conx at this port
            //and i have to implement a methode to delete the old conx
            System.out.println("*you have to delete the old connection");
        }
    }
}
