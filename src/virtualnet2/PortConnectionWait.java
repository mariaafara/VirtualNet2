package virtualnet2;

import java.io.IOException;
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

    public PortConnectionWait(int port,Port p) {

        try {
            //Creating server socket
            serversocket = new ServerSocket(port);
            this.p =p;
            
        } catch (IOException ex) {
            Logger.getLogger(PortConnectionWait.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        ObjectOutputStream objectOutputStream = null;
        while (true) {
            try {

                socket = serversocket.accept();

                if (!p.isconnectionEstablished()) {

                    //bdon synchronization
                    p.setSocket(socket);
                    p.setconnectionEstablished(true);
                    objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeBoolean(true);
                } else {

                   // Socket s = p.getSocket();
                    objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeBoolean(false);
                    //khabera eno port taken cannot be connected to

                }
            } catch (IOException ex) {
                Logger.getLogger(PortConnectionWait.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }
}
}
