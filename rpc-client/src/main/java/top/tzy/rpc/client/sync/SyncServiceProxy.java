package top.tzy.rpc.client.sync;

import top.tzy.rpc.client.ClientConnection;
import top.tzy.rpc.client.ConnectionManager;
import top.tzy.rpc.common.protocol.RpcRequest;
import top.tzy.rpc.common.protocol.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * Created by tuzhenyu on 17-12-5.
 * @author tuzhenyu
 */
public class SyncServiceProxy {
    public <T> T create(Class interfaceClass){
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new SyncProxyHandler()
        );
    }
}

class SyncProxyHandler implements InvocationHandler {

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest();
        request.setId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);

        String interfaceName = method.getDeclaringClass().getName();
        ClientConnection client = ConnectionManager.getInstance().getConnection(interfaceName);
        RpcResponse response = client.send(request);
        return response.getContent();
    }
}

