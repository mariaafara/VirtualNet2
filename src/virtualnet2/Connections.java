package virtualnet2;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.HashMap;

/**
 *
 * @author maria afara
 */
public class Connections {

    public HashMap<Integer, Neighbor> connections;//each port with its directy connected to it

    public Connections() {
        connections = new HashMap<Integer, Neighbor>();
        System.out.println("*connections is created");
    }

    synchronized public HashMap<Integer, Neighbor> getConnections() {
        return connections;
    }

    synchronized public void setConnections(HashMap<Integer, Neighbor> connections) {
        this.connections = connections;
    }

    synchronized public void addNeighbor(int port, InetAddress neighborip, int neighborport) {
        Neighbor newNeighbor = new Neighbor(neighborip, neighborport);
        connections.put(port, newNeighbor);
    }

    synchronized public void addNeighbor(int port, Neighbor neighbor) {

        connections.put(port, neighbor);
    }

    synchronized public Neighbor getNeighbor(int port) {
        return connections.get(port);
    }

    synchronized public boolean containsPort(int key) {
        return connections.containsKey(key);
    }
}
