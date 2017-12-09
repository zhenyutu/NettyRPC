package top.tzy.rpc.client;


import top.tzy.rpc.api.HelloService;
import top.tzy.rpc.client.sync.SyncServiceProxy;

/**
 * Created by tuzhenyu on 17-12-5.
 * @author tuzhenyu
 */
public class Main {
    public static void main(String[] args) {
        SyncServiceProxy proxy = new SyncServiceProxy();
        HelloService service = proxy.create(HelloService.class);
        System.out.println(service.hello("tuzhenyu"));
    }
}
