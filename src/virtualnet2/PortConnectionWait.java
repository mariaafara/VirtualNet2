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
    int port;
    Connections connections;
    RoutingTable rt;

    public PortConnectionWait(int port, Port p, Connections connections, RoutingTable rt) {

        try {
            //Creating server socket
            System.out.println("*Port " + port + " waiting for a conx");
            serversocket = new ServerSocket(port);
            this.p = p;
            this.port = port;
            this.connections = connections;
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
                System.out.println("*Port " + port + " still waiting for a connection");

                socket = serversocket.accept();
                System.out.println("*socket :myport " + socket.getLocalPort() + " destport " + socket.getPort());

                System.out.println("*connection accepteed at port " + port);

                if (!p.isconnectionEstablished()) {

                    p.setSocket(socket);
                    p.setconnectionEstablished(true);
                    objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                    objectOutputStream.writeBoolean(true);
                    objectOutputStream.flush();
                    System.out.println("*true was sent");

                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                    Neighbor neighbor = (Neighbor) objectInputStream.readObject();

                    connections.addNeighbor(port, neighbor);
                    rt.addEntry(neighbor.getNeighborAddress(), neighbor.getNeighborPort(), 1);
                    rt.printTable("after add");
                    System.out.println("*connection is initialized at port " + port + " with neighb = " + neighbor.getNeighborAddress() + " , " + neighbor.getNeighborPort());

                } else {

                    objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeBoolean(false);
                    objectOutputStream.flush();
                    //khabera eno port taken cannot be connected to

                }
            } catch (IOException ex) {
                Logger.getLogger(PortConnectionWait.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(PortConnectionWait.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
