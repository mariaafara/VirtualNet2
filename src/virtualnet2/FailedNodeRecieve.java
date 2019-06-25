/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.io.IOException;
import sharedPackage.RoutingTableKey;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        RoutingTableKey ipHost = new RoutingTableKey(fn.getInetaddress(), fn.getHostname());
//hon bde 23ml delete lal entry le bel table 3nde le lkey le 2ela huwe l ip lal failed node
//m3 w7ad n lportet le 2ela le huwe ha ykoun lnext hop bel nsbe ele m3 hay lnode
///!!!!!!!!!!!!!!!!!!!!!!!! commented newely
        //ArrayList<FailedNode> arrayfn = new ArrayList<>();
        for (HashMap.Entry<RoutingTableKey, RoutingTableInfo> entry : rt.routingEntries.entrySet()) {

            if (ipHost == entry.getValue().getRtkey()) {
                rt.deleteEntry(entry.getValue().getRtkey());
                //3m eb3t lentry le m7itaa
                FailedNode newfn = new FailedNode(entry.getKey().getIp(), entry.getKey().getHostname(), entry.getValue().getPort());
                //   arrayfn.add(newfn);
                for (HashMap.Entry<RoutingTableKey, RoutingTableInfo> entry2 : rt.routingEntries.entrySet()) {

                    if (entry2.getValue().cost == 1) {
                        try {
                            //lneighbors
                            entry2.getValue().portclass.getOos().writeObject(newfn);
                        } catch (IOException ex) {
                            Logger.getLogger(FailedNodeRecieve.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
        // 

        System.out.print("\n");
        rt.printTable("After Deleting Failed Node  ");
        System.out.println("\n");

    }

    private FailedNode recieveFailedNode(Object recievedObject) {

        // get failed node object which is reccieved
        return fn = (FailedNode) recievedObject;
    }

}
