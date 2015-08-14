package org.server.frontend.ace;

import java.io.IOException;

/**
 *
 * @author Hxms
 */
public class AceTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        System.load("C:\\Workspace\\NetBeansProjects\\ServerEngine\\x64\\Debug\\acecore.dll");
        AceCore.getInstance().sayHello();
        if (AceCore.getInstance().start(8888)) {
            System.out.println("服务器监听成功！");
        }

        System.in.read();
    }

}
