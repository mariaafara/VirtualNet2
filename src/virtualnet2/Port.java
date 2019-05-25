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

    boolean connectionEstablished;
    Socket socket;
    int port;
    PortConnectionWait portConnectionWait;
    PortConnectionEstablish portConnectionEstablish;
    final Object lockInput = new Object();

    public Port(int port) {
        System.out.println("Port " + port + " initialized");
        this.connectionEstablished = false;
        this.port = port;
        this.socket = null;
        portConnectionWait = new PortConnectionWait(port, this);

    }

    public boolean isconnectionEstablished() {
        synchronized(lockInput){
            return connectionEstablished;
        }
    }

    synchronized public void setconnectionEstablished(boolean connectionEstablished) {
        this.connectionEstablished = connectionEstablished;
    }

    synchronized public Socket getSocket() {
        return socket;
    }

    synchronized public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        super.run();
        portConnectionWait.start();

    }

    public void connect(int port, InetAddress neighborAddress, int neighborport) {
        System.out.println("*****");
        System.out.println("in connect method in port class ");
        PortConnectionEstablish pce = new PortConnectionEstablish(port, neighborAddress, neighborport, this);
        pce.start();
    }
}
