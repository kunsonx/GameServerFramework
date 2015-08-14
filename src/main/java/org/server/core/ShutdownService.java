/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.core;

/**
 * 关机服务
 *
 * @author Administrator
 */
public class ShutdownService implements Runnable {

    private final AbstractStandardServer<?> _server;
    private Thread _thread;

    public ShutdownService(AbstractStandardServer<?> _server) {
        this._server = _server;
    }

    /**
     * 开始关机过程
     */
    public void shutdown() {
        if (_thread == null) {
            _thread = new Thread(this);
            _thread.start();
        }
    }

    @Override
    public void run() {
        _server.stop();
    }
}
