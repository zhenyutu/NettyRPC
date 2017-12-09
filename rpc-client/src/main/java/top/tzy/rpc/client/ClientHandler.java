package top.tzy.rpc.client;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.tzy.rpc.client.sync.SyncFutureResult;
import top.tzy.rpc.common.protocol.RpcResponse;


/**
 * Created by tuzhenyu on 17-12-5.
 * @author tuzhenyu
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcResponse response = JSONObject.parseObject(msg.toString(),RpcResponse.class);
        SyncFutureResult.receive(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("error");
        cause.printStackTrace();
        ctx.close();
    }
}
