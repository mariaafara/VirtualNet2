/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author maria afara
 */
public class Port extends Thread {

    Connections connections;
    boolean connectionEstablished;
    Socket socket;
    int port;
    PortConnectionWait portConnectionWait;
    PortConnectionEstablish portConnectionEstablish;

    final Object lockconnectionEstablished = new Object();
    final Object lockSocket = new Object();
    RoutingTable rt;

    public Port(int port, Connections connections, RoutingTable rt) {
        System.out.println("*Port " + port + " initialized");
        this.connectionEstablished = false;
        this.port = port;
        this.socket = null;
        this.rt = rt;
        portConnectionWait = new PortConnectionWait(port, this, connections, rt);
        this.connections = connections;
    }

    public boolean isconnectionEstablished() {
        synchronized (lockconnectionEstablished) {
            return connectionEstablished;
        }
    }

    public void setconnectionEstablished(boolean connectionEstablished) {
        synchronized (lockconnectionEstablished) {
            if (connectionEstablished) {
                this.notifyAll();
            }
            this.connectionEstablished = connectionEstablished;
        }
    }

    public Socket getSocket() {
        synchronized (lockSocket) {
            return socket;
        }
    }

    public void setSocket(Socket socket) {
        synchronized (lockSocket) {
            this.socket = socket;
        }
    }

    @Override
    public void run() {

        super.run();
        portConnectionWait.start();

    }

    public void connect(int port, InetAddress neighborAddress, int neighborport) {

        PortConnectionEstablish pce = new PortConnectionEstablish(port, neighborAddress, neighborport, this, connections, rt);
        pce.start();
    }
}
