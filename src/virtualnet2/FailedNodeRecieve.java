/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author maria afara
 */
public class FailedNodeRecieve extends Thread {

    FailedNode fn;//the one recieved
    Object recievedObject;
    private RoutingTable rt;

    public FailedNodeRecieve(Object recievedObject, RoutingTable rt) {

        this.rt = rt;
        this.recievedObject = recievedObject;
    }

    @Override
    public void run() {

        fn = recieveFailedNode(recievedObject);
//hon bde 23ml delete lal entry le bel table 3nde le lkey le 2ela huwe l ip lal failed node
//m3 w7ad n lportet le 2ela le huwe ha ykoun lnext hop bel nsbe ele m3 hay lnode
        //   rt.deleteEntry(fn.getInetaddress());

        //delete entry then send ne routing table
        for (HashMap.Entry<RoutingTableKey, RoutingTableInfo> entry : rt.routingEntries.entrySet()) {

            if (entry.getValue().cost == 1) {
                new RoutingTableSend(entry.getValue().portclass.getOos(), rt).start();
            }
        }
        System.out.print("\n");
        rt.printTable("After Deleting Failed Node  ");
        System.out.println("\n");

    }

    private FailedNode recieveFailedNode(Object recievedObject) {

        // get failed node object which is reccieved
        return fn = (FailedNode) recievedObject;
    }

}
