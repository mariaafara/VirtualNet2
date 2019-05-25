/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maria afara
 */
public class Reciever extends Thread {
    
    private Socket socket;
    
    private RoutingTable rt;
    
    private int port;
    
    private Object recievedObject;
    ObjectInputStream ois;
    Connections connections;
    PortConxs portConxs;
    
    public Reciever(int port, Socket socket, PortConxs portConx, Connections connections, RoutingTable rt) {
        System.out.println("*reciever initialized");
        this.port = port;
        this.socket = socket;
        this.rt = rt;
        this.connections = connections;
        this.portConxs = portConxs;
        
    }
    
    @Override
    public void run() {
        try {
            portConxs.getPortInstance(port).wait();
            ois = new ObjectInputStream(socket.getInputStream());
            
            while (true) {
                System.out.println("*waiting to recieve object");
                recievedObject = ois.readObject();
                System.out.println("*recieved object =" + recievedObject);
                if (recievedObject instanceof RoutingTable) {
                    System.out.println("*recieved routing table");
                    
                    new RoutingTableRecieve(recievedObject, port, socket, portConxs, connections, rt).start();
                } else if (recievedObject instanceof FailedNode) {
                    new FailedNodeRecieve(recievedObject, socket, portConxs, rt).start();
                } else {
                    System.out.println("*recieved unknown type of object " + recievedObject.getClass());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Reciever.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Reciever.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Reciever.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
