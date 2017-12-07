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

        for (int i=0;i<500;i++){
            String str = service.hello("tuzhenyu");
            Thread.sleep(50);
            System.out.println(str);
        }
    }

}
