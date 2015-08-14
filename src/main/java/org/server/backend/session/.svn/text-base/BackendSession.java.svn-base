/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.backend.session;

/**
 * 前台会话标识符
 *
 * @author Administrator
 */
public class BackendSession {

    private final int _frontendServerId;
    private final long _sessionId;

    public BackendSession(int _frontendServerId, long _sessionId) {
        this._frontendServerId = _frontendServerId;
        this._sessionId = _sessionId;
    }

    public int getFrontendServerId() {
        return _frontendServerId;
    }

    public long getSessionId() {
        return _sessionId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BackendSession) {
            BackendSession target = (BackendSession) obj;
            return target._frontendServerId == _frontendServerId
                    && target._sessionId == _sessionId;
        }
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this._frontendServerId;
        hash = 37 * hash + (int) (this._sessionId ^ (this._sessionId >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return String.format("%d-%d", _frontendServerId, _sessionId);
    }
}
