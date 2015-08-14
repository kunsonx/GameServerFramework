/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.frontend.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.server.core.io.SessionMessage;

/**
 * 消息编码器
 *
 * @author Hxms
 */
public class MessageEncoder implements ProtocolEncoder {

    @Override
    public void encode(IoSession is, Object o, ProtocolEncoderOutput peo) throws Exception {
        SessionMessage message = (SessionMessage) o;
        if (message != null) {
            peo.write(MessageCodec.writeMessage(is, message));
        }
    }

    @Override
    public void dispose(IoSession is) throws Exception {
        // do nothing
    }
}
