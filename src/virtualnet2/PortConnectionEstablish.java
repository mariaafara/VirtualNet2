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
    Connections connections;
    RoutingTable rt;

    public PortConnectionEstablish(int myport, InetAddress ip, int port, Port p, Connections connections, RoutingTable rt) {
        this.port = port;
        this.myport = myport;
        this.p = p;
        this.ip = ip;
        this.connections = connections;
        this.rt = rt;
    }

    @Override
    public void run() {

        boolean bool;
        if (!p.isconnectionEstablished()) {

            try {
                System.out.println("*connection is being established");
                socket = new Socket(ip, port);
                System.out.println("*socket : myport " + socket.getLocalPort() + " destport " + socket.getPort());

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                bool = objectInputStream.readBoolean();

                System.out.println("*" + bool + " was recieved");

                if (bool) {
                    p.setSocket(socket);
                    p.setconnectionEstablished(true);
                    //3m b3tlo m3 min lconnection
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(new Neighbor(ip, myport));
                    Neighbor newNeighbor = new Neighbor(ip, port);
                    connections.addNeighbor(port, newNeighbor);
                    //rt.addEntry(ip, port, 1);
                    rt.addEntry(port, port, 1);

                    
                    rt.printTable("**after add**");
                    System.out.println("*connection is initialized at port " + myport + " with neighb = " + ip + " , " + port);

                } else {
                    System.out.println("*Sorry connction already established with this destination");
                    //cannot connect to  this cnx
                }

            } catch (IOException ex) {
                Logger.getLogger(PortConnectionEstablish.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
