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

       //f2();
       f1();
       
    }

    static void f1() throws UnknownHostException, InterruptedException {
        Router router1 = new Router();
        router1.start();

        //  Thread.sleep(3000);
        router1.initializePort(1111);
        router1.initializePort(1112);
        // router1.initializePort(1111);
        //Thread.sleep(1500);
        router1.initializeConnection(1111, InetAddress.getLocalHost(), 2222);
        Thread.sleep(10000);
        router1.initializeRoutingProtocol();
    }

    static void f2() throws UnknownHostException, InterruptedException {
        Router router2 = new Router();
        router2.start();
        router2.initializePort(2222);
        Thread.sleep(10000);
        router2.initializeRoutingProtocol();
    }
}
