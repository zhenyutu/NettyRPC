package top.tzy.sample;

import top.tzy.api.HelloService;
import top.tzy.server.RpcService;

/**
 * Created by tuzhenyu on 17-12-5.
 * @author tuzhenyu
 */
@RpcService(value = HelloService.class)
public class HelloServiceImpl implements HelloService{

    public String hello(String name) {
        return "server:"+name;
    }
}
