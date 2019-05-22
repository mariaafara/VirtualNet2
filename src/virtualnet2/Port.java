/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author maria afara
 */
public class Port extends Thread {

    boolean connectionEstablished;
    Socket socket;
    int port;

    PortConnectionWait portConnectionWait;
    PortConnectionEstablish portConnectionEstablish;

    public Port(int port) {
        this.connectionEstablished = false;
        this.port = port;
        this.socket = null;
        portConnectionWait = new PortConnectionWait(port, this);

    }

    public boolean isconnectionEstablished() {
        return connectionEstablished;
    }

    public void setconnectionEstablished(boolean connectionEstablished) {
        this.connectionEstablished = connectionEstablished;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        super.run();
        portConnectionWait.start();
    }

    public void connect(InetAddress inetAddress, int port) {
        new PortConnectionEstablish(inetAddress, port, this).start();
    }
}
