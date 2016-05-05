package org.server.backend.io.handle.impl;

import org.server.backend.io.handle.ILogicDataModel;
import org.server.backend.io.handle.ITransportHandlerLayer;
import org.server.backend.io.handle.impl.callsite.LogicDeadHandleCallsite;
import org.server.backend.io.handle.impl.exception.TransportException;
import org.server.backend.session.GameSession;
import org.server.core.io.SessionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.MessageLite;

public class TransportHandlerTask implements Runnable {
    
    static final Logger    log = LoggerFactory.getLogger(TransportHandlerTask.class);
                               
    ITransportHandlerLayer item;
    GameSession            session;
    SessionMessage         message;
    MessageLite            generatedMessage;
    ILogicDataModel        dataModel;
    boolean                inDirection;
                           
    public TransportHandlerTask(ITransportHandlerLayer item, GameSession session, boolean inDirection) {
        super();
        this.item = item;
        this.session = session;
        this.inDirection = inDirection;
    }
    
    /**
     * @return the message
     */
    public SessionMessage getMessage() {
        return message;
    }
    
    /**
     * @param message
     *            the message to set
     */
    public void setMessage(SessionMessage message) {
        this.message = message;
    }
    
    /**
     * @return the generatedMessage
     */
    public MessageLite getGeneratedMessage() {
        return generatedMessage;
    }
    
    /**
     * @param generatedMessage
     *            the generatedMessage to set
     */
    public void setGeneratedMessage(MessageLite generatedMessage) {
        this.generatedMessage = generatedMessage;
    }
    
    /**
     * @return the dataModel
     */
    public ILogicDataModel getDataModel() {
        return dataModel;
    }
    
    /**
     * @param dataModel
     *            the dataModel to set
     */
    public void setDataModel(ILogicDataModel dataModel) {
        this.dataModel = dataModel;
    }
    
    @Override
    public void run() {
        ITransportHandlerLayer layer = item;
        while (layer != null) {
            try {
                if (inDirection) {
                    layer.in(session, message, this);
                    layer = layer.next();
                } else {
                    layer.out(session, message, this);
                    layer = layer.previous();
                }
            } catch (TransportException e) {
                try {
                    for (LogicDeadHandleCallsite callsite : TransportMeta.DEAD_HANDLE_META)
                        callsite.invoke(session, message, generatedMessage, e);
                } catch (Throwable e2) {
                    log.error("error at dead handler layer execute ::", e2);
                }
                break;
            } catch (Throwable e2) {
                log.error("error at transportV2 handler layer execute ::", e2);
                break;
            }
        }
    }
}
