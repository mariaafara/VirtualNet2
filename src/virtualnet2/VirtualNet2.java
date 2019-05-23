/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author maria afara
 */
public class VirtualNet2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, UnknownHostException {

        Router router1 = new Router();

        Thread.sleep(3000);

        Router router2 = new Router();

        Thread.sleep(3000);

        router1.initializePort(1111);
        router1.initializePort(1112);
        router1.initializePort(1111);

        Thread.sleep(3000);

        router2.initializePort(2222);

        Thread.sleep(3000);

        router1.initializeConnection(1111, InetAddress.getLocalHost(), 2222);
       // router2.initializeConnection(2222, InetAddress.getLocalHost(), 1111);
     
        
    }

}
