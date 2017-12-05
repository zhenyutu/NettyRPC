package top.tzy.rpc.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.tzy.rpc.common.protocol.RpcRequest;
import top.tzy.rpc.common.protocol.RpcResponse;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by tuzhenyu on 17-12-5.
 * @author tuzhenyu
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private Map<String,Object> services;

    public ServerHandler(Map<String,Object> services){
        this.services = services;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server active");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest request = JSONObject.parseObject(msg.toString(),RpcRequest.class);
        RpcResponse response = new RpcResponse();
        response.setId(request.getId());
        response.setContent(handler(request));
        ctx.writeAndFlush(JSON.toJSONString(response)+"\n");
    }

    private Object handler(RpcRequest request)throws Exception{
        String className = request.getClassName();
        Object bean = services.get(className);

        Class<?> serviceClass = bean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        Method method = serviceClass.getMethod(methodName,parameterTypes);
        method.setAccessible(true);
        return method.invoke(bean,parameters);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("close");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("error");
        cause.printStackTrace();
        ctx.close();
    }
}
