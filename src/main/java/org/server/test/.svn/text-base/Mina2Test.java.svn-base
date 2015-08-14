/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.test;

import java.net.InetSocketAddress;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 *
 * @author Administrator
 */
public class Mina2Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.setHandler(new Mina2TesterServerHandler());

        try {
            acceptor.bind(new InetSocketAddress(7890));
        } catch (Exception e) {
            System.out.println("Config Error:" + e);
        }

        System.out.println("监听中...");

    }

}
