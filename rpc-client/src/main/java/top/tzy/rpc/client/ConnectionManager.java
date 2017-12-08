package top.tzy.rpc.client;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tuzhenyu on 17-12-7.
 * @author tuzhenyu
 */
public class ConnectionManager {
    private volatile static ConnectionManager MANAGER_INSTANCE = null;
    private ConcurrentHashMap<String,ClientConnection> connections = new ConcurrentHashMap<String, ClientConnection>();

    public static ConnectionManager getInstance(){
        if (MANAGER_INSTANCE==null){
            synchronized (ConnectionManager.class){
                if (MANAGER_INSTANCE==null){
                    MANAGER_INSTANCE = new ConnectionManager();
                }
            }
        }
        return MANAGER_INSTANCE;
    }

    public ClientConnection getConnection(String interfaceName)throws Exception{
        ClientConnection clientConnection = null;
        String serviceAddress = ServiceDiscover.getInstance().discover(interfaceName);
        if (connections.get(serviceAddress)==null){
            String host = serviceAddress.split(":")[0];
            int port = Integer.parseInt(serviceAddress.split(":")[1]);
            clientConnection = new ClientConnection(host,port);
            connections.put(serviceAddress,clientConnection);
        }else {
            clientConnection = connections.get(serviceAddress);
        }

        return clientConnection;
    }

    public void removeConnection(String adress){
        connections.remove(adress);
    }
}
