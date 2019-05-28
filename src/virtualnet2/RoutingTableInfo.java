package virtualnet2;

import java.io.Serializable;

/**
 *
 * @author maria afara
 */
public class RoutingTableInfo implements Serializable {

    int nextHop;
    int cost;
    int port;
    transient Port portclass;
    boolean activated;
//  transient

    public RoutingTableInfo(int nextHop, int cost, int port, Port portclass, boolean activated) {
        this.nextHop = nextHop;
        this.cost = cost;
        this.port = port;
        this.portclass = portclass;
        this.activated = activated;
    }

    public int getNextHop() {
        return nextHop;
    }

    public void setNextHop(int nextHop) {
        this.nextHop = nextHop;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Port getPortclass() {
        return portclass;
    }

    public void setPortclass(Port portclass) {
        this.portclass = portclass;
    }

    @Override
    public String toString() {
        synchronized (this) {
            return "TableInfo{" + "nextHop=" + nextHop + ", cost=" + cost + '}';
        }
    }
}
