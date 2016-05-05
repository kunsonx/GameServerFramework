package org.server.backend.io;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.server.backend.io.handle.ITransportHandlerLayer;
import org.server.backend.io.handle.impl.TransportHandlerTask;
import org.server.backend.io.handle.impl.layer.CallbackLayer;
import org.server.backend.io.handle.impl.layer.SerializeLayer;
import org.server.backend.io.handle.impl.layer.TransportLayer;
import org.server.backend.session.BackendSession;
import org.server.backend.session.GameSession;
import org.server.backend.session.GameSessionProvider;
import org.server.core.io.SessionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.MessageLite;

public class TransportV2 {
    
    static final Logger           log    = LoggerFactory.getLogger(TransportV2.class);
                                         
    public static boolean         ENABLE = false;
    static ITransportHandlerLayer first  = null;
    static ITransportHandlerLayer last   = null;
    static ExecutorService        callbackExecutor;
                                  
    public static void initialize() {
        int callbackExecutorCount = Runtime.getRuntime().availableProcessors();
        
        insertLayerToLast(new TransportLayer());
        insertLayerToLast(new SerializeLayer());
        insertLayerToLast(new CallbackLayer());
        
        callbackExecutor = Executors.newWorkStealingPool(callbackExecutorCount);
        ENABLE = true;
        
        log.info("[BackendServer::TransportV2] callback executor pool using thread size : {}.", callbackExecutorCount);
    }
    
    static void insertLayerToLast(ITransportHandlerLayer layer) {
        if (first == null) {
            first = layer;
        } else {
            layer.previous(last);
            last.next(layer);
        }
        last = layer;
    }
    
    public static void fireIn(int nodeId, long sessionId, SessionMessage data) {
        GameSession session = GameSessionProvider.getInstance().getGameSession(new BackendSession(nodeId, sessionId));
        TransportHandlerTask task = new TransportHandlerTask(first, session, true);
        task.setMessage(data);
        callbackExecutor.execute(task);
    }
    
    public static void fireOut(GameSession session, MessageLite messageLite) {
        TransportHandlerTask task = new TransportHandlerTask(last, session, false);
        task.setGeneratedMessage(messageLite);
        callbackExecutor.execute(task);
    }
}
