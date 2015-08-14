/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.backend.io;

import org.server.backend.session.GameSession;
import org.server.core.io.SessionMessage;

/**
 * 回调异步包装
 *
 * @author Hxms
 */
public class TransportCallbackRunnable implements Runnable {

    TransportCallback _callback;
    GameSession _session;
    SessionMessage _data;

    public TransportCallbackRunnable(TransportCallback _callback, GameSession _session, SessionMessage _data) {
        this._callback = _callback;
        this._session = _session;
        this._data = _data;
    }

    @Override
    public void run() {
        Thread cur = Thread.currentThread();
        String name = cur.getName();
        cur.setName(String.format("MessageCallback Work Thread:[%d]", cur.getId()));
        _callback.received(_session, _data);
        cur.setName(name);
    }
}
