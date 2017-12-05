package top.tzy.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by tuzhenyu on 17-12-5.
 * @author tuzhenyu
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server active");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ServerRequest request = JSONObject.parseObject(msg.toString(),ServerRequest.class);
        ServerResponse response = new ServerResponse();
        response.setId(request.getId());
        response.setContent("server:Hello world");
        ctx.writeAndFlush(JSON.toJSONString(response)+"\n");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("close");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("error");
        ctx.close();
    }
}
