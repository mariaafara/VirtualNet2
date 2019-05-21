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
public class Port {

    boolean connectionEstablished;
    Socket socket;
    int port;
    private static Port instance;
    HashMap<InetAddress, Integer> connection;

    public Port(int port) {
        this.connectionEstablished = false;
        this.port = port;
        this.socket = null;
        connection = new HashMap<InetAddress, Integer>();//destip,destport
        new PortConnectionWait(port).start();
        new PortConnectionEstablish(port).start();
    }

    /*
         * Returns singleton instance of POrt class
     */
    public static Port getInstance(int port) {
        if (instance == null) {
            synchronized (Port.class) {
                if (instance == null) {
                    instance = new Port(port);
                }
            }
        }
        return instance;
    }

    public void setConnection(InetAddress inetAddress, int port) {

        connection.put(inetAddress, port);

    }

    public void resetConnection() {
        connection.clear();
    }

    public HashMap<InetAddress, Integer> getConnection() {
        return connection;
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

}
