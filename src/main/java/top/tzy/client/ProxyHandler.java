package top.tzy.client;

import javax.xml.ws.Response;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by tuzhenyu on 17-12-5.
 * @author tuzhenyu
 */
public class ProxyHandler implements InvocationHandler {

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ClientRequest request = new ClientRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);

        ClientResponse response = NettyClient.send(request);
        return response.getContent();
    }
}
