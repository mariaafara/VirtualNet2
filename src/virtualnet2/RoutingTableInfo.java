package virtualnet2;

import java.io.Serializable;

/**
 *
 * @author maria afara
 */
public class RoutingTableInfo implements Serializable {

    int nextHop;
    int cost;

    public RoutingTableInfo(int nextHop, int cost) {
        this.nextHop = nextHop;
        this.cost = cost;
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

    @Override
    public String toString() {
        return "TableInfo{" + "nextHop=" + nextHop + ", cost=" + cost + '}';
    }
}
