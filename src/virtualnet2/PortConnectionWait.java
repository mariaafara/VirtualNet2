package virtualnet2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class listens on given port for incoming connection
 *
 * @author maria afara
 */
public class PortConnectionWait extends Thread {

    ServerSocket serversocket;
    private Port p;
    Socket socket;
    String msg;
    int myport;
   
    RoutingTable rt;

    public PortConnectionWait(int myport, Port p, RoutingTable rt) {

        try {
            //Creating server socket
            System.out.println("*port " + myport + " waiting for a conx");
            serversocket = new ServerSocket(myport);
            this.p = p;
            this.myport = myport;
          
            this.rt = rt;
        } catch (IOException ex) {
            Logger.getLogger(PortConnectionWait.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {

        ObjectOutputStream objectOutputStream;
        while (true) {
            try {

                System.out.println("*port " + myport + " still waiting for a connection");

                socket = serversocket.accept();
                
                rt.printTable("**Checking**");
                
                System.out.println("*socket :myport " + socket.getLocalPort() + " destport " + socket.getPort());

                System.out.println("*connection accepteed at port " + myport);

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Neighbor neighbor = (Neighbor) objectInputStream.readObject();
                //neighbor.neighborPort is the next hop 
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                if (rt.isExistandNotActive(neighbor.neighborPort)) {

                    rt.activateEntry(neighbor.neighborPort);
                    p.setSocket(socket);
                    p.setconnectionEstablished(true);

                    objectOutputStream.writeBoolean(true);
                    objectOutputStream.flush();

                    System.out.println("*true was sent");

                } else {
                    //rt.addEntry(neighbor.getNeighborAddress(), neighbor.getNeighborPort(), 1, port, p, false);
                    rt.addEntry(neighbor.getNeighborPort(), neighbor.getNeighborPort(), 1, myport, p, false);
                    objectOutputStream.writeBoolean(false);
                    objectOutputStream.flush();

                    System.out.println("*false was sent");
                    System.out.println("*my turn to establish the connection on my side with port " + myport );

                   
                    rt.printTable("--after add--");
                    socket.close();
                }

            } catch (IOException ex) {
                Logger.getLogger(PortConnectionWait.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(PortConnectionWait.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
