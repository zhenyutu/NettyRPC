package top.tzy.rpc.client.async;

import top.tzy.rpc.client.ClientConnection;
import top.tzy.rpc.client.ConnectionManager;
import top.tzy.rpc.client.sync.SyncFutureResult;
import top.tzy.rpc.common.protocol.RpcRequest;

import java.util.UUID;

/**
 * Created by tuzhenyu on 17-12-9.
 * @author tuzhenyu
 */
public class AsyncServiceProxy<T> {
    private Class<T> clazz;

    public AsyncServiceProxy(Class<T> clazz){
        this.clazz = clazz;
    }

    public AsyncFutureResult call(String method, Object... args) throws Exception{
        String interfaceName = clazz.getName();
        RpcRequest request = createRequest(interfaceName,method,args);

        ClientConnection client = ConnectionManager.getInstance().getConnection(interfaceName);
        return client.call(request);
    }

    private RpcRequest createRequest(String className, String methodName, Object[] args){
        RpcRequest request = new RpcRequest();
        request.setId(UUID.randomUUID().toString());
        request.setClassName(className);
        request.setMethodName(methodName);
        request.setParameters(args);

        Class[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        request.setParameterTypes(parameterTypes);

        return request;
    }

}
