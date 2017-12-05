package top.tzy.rpc.sample;

import top.tzy.rpc.api.HelloService;
import top.tzy.rpc.server.RpcService;

/**
 * Created by tuzhenyu on 17-12-5.
 * @author tuzhenyu
 */
@RpcService(value = HelloService.class)
public class HelloServiceImpl implements HelloService {

    public String hello(String name) {
        return "server:"+name;
    }
}
