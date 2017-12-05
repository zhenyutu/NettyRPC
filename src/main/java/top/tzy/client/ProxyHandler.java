package top.tzy.client;

import top.tzy.protocol.RpcRequest;
import top.tzy.protocol.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by tuzhenyu on 17-12-5.
 * @author tuzhenyu
 */
public class ProxyHandler implements InvocationHandler {

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
