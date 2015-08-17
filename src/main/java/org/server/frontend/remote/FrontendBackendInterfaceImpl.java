/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.frontend.remote;

import java.rmi.RemoteException;

import org.server.core.io.SessionMessage;
import org.server.core.remote.RMIServiceInterfaceImpl;
import org.server.frontend.FrontendServer;

public class FrontendBackendInterfaceImpl extends RMIServiceInterfaceImpl<FrontendServer> implements FrontendBackendInterface {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1784040854920716813L;
	public FrontendBackendInterfaceImpl(FrontendServer server) throws RemoteException {
        super(server);
    }

    @Override
    public int getId() throws RemoteException {
        return getServer().getNodeId();
    }

    @Override
    public void setId(int id) throws RemoteException {
        getServer().setNodeId(id);
    }

    @Override
    public void write(long sessionId, SessionMessage message) throws RemoteException {
        getServer().getFrontendSocketEventHandler().write(sessionId, message);
    }

    @Override
    public String getAddress(long sessionId) throws RemoteException {
        return getServer().getFrontendSocketEventHandler().getAddress(sessionId);
    }
}
