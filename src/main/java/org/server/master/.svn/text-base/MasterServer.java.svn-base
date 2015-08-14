/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.master;

import org.server.master.remote.MasterRMIServerInterfaceImpl;
import java.rmi.RemoteException;
import org.server.core.AbstractRMIServerClient;
import org.server.core.remote.RMIServiceInterfaceImpl;
import org.server.startup.StartupServer;

/**
 *
 * @author Administrator
 */
public class MasterServer extends AbstractRMIServerClient<MasterServerConfig> {

    public MasterServer() {
        super(new MasterServerConfig());
    }

    @Override
    protected boolean stop0() {
        for (ManagedServerInformationNode node : getServerConfig().getManagedServers().values()) {
            node.clearService();
        }
        return true;
    }

    public static void main(String[] args) {
        StartupServer startupServer = new StartupServer("org.server.master.MasterServer");
        startupServer.run();
    }

    @Override
    protected String getRemoteObjectName() {
        return "MasterServer";
    }

    @Override
    protected MasterRMIServerInterfaceImpl createRemoteObject() throws RemoteException {
        return new MasterRMIServerInterfaceImpl(this);
    }

    @Override
    protected RMIServiceInterfaceImpl<?> createMasterInterfaceImpl() throws RemoteException {
        return null;
    }
}
