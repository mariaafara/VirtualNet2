/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualnet2;

import java.net.UnknownHostException;

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
        //f3();
      f1();

    }

    static void f1() throws UnknownHostException, InterruptedException {
        Router router1 = new Router();
        router1.start();

        //  Thread.sleep(3000);
        //router1.initializePort(1111);
        //router1.initializePort(1112);
        // router1.initializePort(1111);
        //Thread.sleep(1500);

        //   Sy4stem.out.println("*before routing");
        // router1.initializeRoutingProtocol();
    }

    static void f2() throws UnknownHostException, InterruptedException {
        Router router2 = new Router(); 
        router2.start();
       // router12.initializePort(2222);
       // router2.initializeConnection(2222, InetAddress.getLocalHost(), 1111);

        //  System.out.println("*before routing");
        // router2.initializeRoutingProtocol();
    }

    static void f3() throws UnknownHostException, InterruptedException {
        Router router3 = new Router();
        router3.start();
      //  router3.initializePort(3333);
        //router3.initializeConnection(3333, InetAddress.getLocalHost(), 1112);

        //System.out.println("*before routing");
        // router3.initializeRoutingProtocol();
    }
}
