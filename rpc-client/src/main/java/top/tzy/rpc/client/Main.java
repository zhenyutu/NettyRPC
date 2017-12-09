package top.tzy.rpc.client;


import top.tzy.rpc.api.HelloService;
import top.tzy.rpc.client.async.AsyncCallback;
import top.tzy.rpc.client.async.AsyncFutureResult;
import top.tzy.rpc.client.async.AsyncServiceProxy;
import top.tzy.rpc.client.sync.SyncServiceProxy;

/**
 * Created by tuzhenyu on 17-12-5.
 * @author tuzhenyu
 */
public class Main {
    public static void main(String[] args) throws Exception{
        SyncServiceProxy proxy = new SyncServiceProxy();
        HelloService service = proxy.create(HelloService.class);
        System.out.println(service.hello("tuzhenyu"));

//        AsyncServiceProxy<HelloService> proxy1 = new AsyncServiceProxy<HelloService>(HelloService.class);
//        AsyncFutureResult futureResult = proxy1.call("hello","tuzhenyu");
//        futureResult.addCallback(new AsyncCallback() {
//            public void success(Object result) {
//                System.out.println("success");
//            }
//
//            public void fail(Exception e) {
//                System.out.println("error");
//            }
//        });
//        System.out.println(futureResult.get().getContent().toString());
    }
}
