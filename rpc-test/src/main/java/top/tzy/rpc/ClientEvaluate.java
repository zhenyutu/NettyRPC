package top.tzy.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.tzy.rpc.api.HelloService;
import top.tzy.rpc.client.RpcServiceProxy;

/**
 * Created by tuzhenyu on 17-12-7.
 * @author tuzhenyu
 */
public class ClientEvaluate {
    private static final Logger logger = LoggerFactory.getLogger(ClientEvaluate.class);

    public static void main(String[] args) throws Exception{
        RpcServiceProxy proxy = new RpcServiceProxy();
        HelloService service = proxy.create(HelloService.class);

        long start = System.currentTimeMillis();
        for (int i=0;i<5000;i++){
            String str = service.hello("tuzhenyu");
            System.out.println(str+i);
        }
        System.out.println(System.currentTimeMillis()-start);
    }

}
