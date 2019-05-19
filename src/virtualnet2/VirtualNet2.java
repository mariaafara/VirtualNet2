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
        ArrayList<Neighbor> r1n = new ArrayList<>();
        ArrayList<Integer> r1p = new ArrayList<>();
        // InetAddress r1i = InetAddress.getByName("1.1.1.1");

        ArrayList<Neighbor> r2n = new ArrayList<>();
        ArrayList<Integer> r2p = new ArrayList<>();
        //InetAddress r2i = InetAddress.getByName("2.2.2.2");

        ArrayList<Neighbor> r3n = new ArrayList<>();
        ArrayList<Integer> r3p = new ArrayList<>();
        //InetAddress r3i = InetAddress.getByName("3.3.3.3");

        r1n.add(new Neighbor(InetAddress.getLocalHost(), 2222));

        //r1n.add(new Neighbor(r2i, 2221));
        // r1n.add(new Neighbor(r3i, 3331));
        r2n.add(new Neighbor(InetAddress.getLocalHost(), 1111));
        // r2n.add(new Neighbor(r3i, 3332));
        // r3n.add(new Neighbor(r2i, 2222));
        //r3n.add(new Neighbor(r1i, 1112));
        //r1p.add(1112);
        r1p.add(1111);
        // r2p.add(2221);
        r2p.add(2222);

        // r3p.add(3331);
        // r3p.add(3332);
        //trying something
        //  Router r1 = new Router(InetAddress.getLocalHost(), 1111, r1n);
        // Thread.sleep(30000);
        // Router r2 = new Router(InetAddress.getLocalHost(), 2222, r2n);
        //r2.start();
        //Router r3 = new Router(r3i, r3n, r3p);
        //r3.start();   
    }

}
