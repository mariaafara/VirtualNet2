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

    public PortConnectionWait(int port, Port p) {

        try {
            //Creating server socket
            System.out.println("Port " + port + " waiting for a conx");
            serversocket = new ServerSocket(port);
            this.p = p;
            this.port = port;

        } catch (IOException ex) {
            Logger.getLogger(PortConnectionWait.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {

        ObjectOutputStream objectOutputStream;
        while (true) {
            try {
                System.out.println("still waiting for a connection");

                socket = serversocket.accept();
                System.out.println("myport " + socket.getLocalPort() + "destport " + socket.getPort());

                System.out.println("connection accepteed at port " + port);

                if (!p.isconnectionEstablished()) {

                    //bdon synchronization
                    p.setSocket(socket);
                    p.setconnectionEstablished(true);
                    objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//                    objectOutputStream.writeBoolean(true);
                    objectOutputStream.writeBoolean(true);
                    objectOutputStream.flush();
                    System.out.println("true was sent");
                    System.out.println("---------");
                    //**********
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    System.out.println("---------");
                    Neighbor neighbor = (Neighbor) objectInputStream.readObject();

                    System.out.println("---------");
                    Router.connections.put(port, neighbor);
                    System.out.println("2connection is initialized at port " + port + " with neighb= " + neighbor.getNeighborAddress() + " , " + neighbor.getNeighborPort());

                } else {

                    // Socket s = p.getSocket();
                    objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeBoolean(false);
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
