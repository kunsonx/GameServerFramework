/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.test;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author Administrator
 */
public class Mina2TesterServerHandler extends IoHandlerAdapter {

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session); //To change body of generated methods, choose Tools | Templates.
    }
}
