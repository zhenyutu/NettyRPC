package top.tzy.client;

import top.tzy.api.HelloService;

/**
 * Created by tuzhenyu on 17-12-5.
 * @author tuzhenyu
 */
public class Main {
    public static void main(String[] args) {
        RpcServiceProxy proxy = new RpcServiceProxy();
        HelloService service = proxy.create(HelloService.class);
        System.out.println(service.hello("tuzhenyu"));
    }
}
