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
    RoutingTableKey myRTK;

    public FailedNodeRecieve(Object recievedObject, RoutingTable rt, RoutingTableKey myRTK) {

        this.rt = rt;
        this.recievedObject = recievedObject;
        this.myRTK=myRTK;
    }

    @Override
    public void run() {

        fn = recieveFailedNode(recievedObject);
        //    RoutingTableKey ipHost = new RoutingTableKey(fn.getInetaddress(), fn.getHostname());
        RoutingTableKey dest = fn.getDestination();
        RoutingTableKey nextipHost = fn.getMyKey();

        //hon bde 23ml delete lal entry le bel table 3nde le lkey le 2ela huwe l ip lal failed node
        //m3 w7ad n lportet le 2ela le huwe ha ykoun lnext hop bel nsbe ele m3 hay lnode
        ///!!!!!!!!!!!!!!!!!!!!!!!! commented newely
        //ArrayList<FailedNode> arrayfn = new ArrayList<>();
        for (HashMap.Entry<RoutingTableKey, RoutingTableInfo> entry : rt.routingEntries.entrySet()) {
            //router m7et l a 
            //btuslne failed nodem7t l a 
            //iza 3nde dest a w busal mn khlel l a 
            //failed node class
            if (dest.equals(entry.getKey()) && nextipHost.equals(entry.getValue().getNextipHost())) {
             //   rt.deleteEntry(entry.getValue().getNextipHost());
                rt.deleteEntry(entry.getKey());
                //3m eb3t lentry le m7itaa
           //     FailedNode newfn = new FailedNode(entry.getKey().getIp(), entry.getKey().getHostname(), entry.getValue().getPort());
                FailedNode newfn = new FailedNode(dest,myRTK);
                System.out.print("\n"+newfn);
                //   arrayfn.add(newfn);
                for (HashMap.Entry<RoutingTableKey, RoutingTableInfo> entry2 : rt.routingEntries.entrySet()) {

                    if (entry2.getValue().cost == 1) {
                        try {
                            //lneighbors
                             System.out.print("\n broadcast newfn to " +entry2.getKey());
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
