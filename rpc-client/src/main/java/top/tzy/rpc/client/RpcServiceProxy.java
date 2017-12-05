package top.tzy.rpc.client;

import java.lang.reflect.Proxy;

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
