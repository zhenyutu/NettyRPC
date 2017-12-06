package top.tzy.rpc.client;

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
public class RpcServiceProxy {
    public <T> T create(Class interfaceClass){
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new ProxyHandler()
        );
    }
}

class ProxyHandler implements InvocationHandler {

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest();
        request.setId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);

        RpcResponse response = NettyClient.send(request);
        return response.getContent();
    }
}

