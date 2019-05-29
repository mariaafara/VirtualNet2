package virtualnet2;

import java.io.Serializable;
import java.net.InetAddress;

/**
 *
 * @author maria afara
 */
public class Neighbor implements Serializable{

    InetAddress neighborAddress;
    Integer neighborPort;

    public Neighbor(InetAddress neighborAddress, Integer neighborPort) {
        this.neighborAddress = neighborAddress;
        this.neighborPort = neighborPort;
    }

    public InetAddress getNeighborAddress() {
        return neighborAddress;
    }

    public void setNeighborAddress(InetAddress neighborAddress) {
        this.neighborAddress = neighborAddress;
    }

    public Integer getNeighborPort() {
        return neighborPort;
    }

    public void setNeighborPort(Integer neighborPort) {
        this.neighborPort = neighborPort;
    }

    @Override
    public String toString() {
        return "Neighbor{" + "neighborAddress=" + neighborAddress + ", neighborPort=" + neighborPort + '}';
    }
}
