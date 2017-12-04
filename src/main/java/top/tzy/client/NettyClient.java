package top.tzy.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import top.tzy.constant.Constant;

/**
 * Created by tuzhenyu on 17-12-4.
 * @author tuzhenyu
 */
public class NettyClient {
    private static final Bootstrap b = new Bootstrap();
    private static ChannelFuture f = null;
    static {
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new StringEncoder());
                            socketChannel.pipeline().addLast(new SimpleClientHandler());
                        }
                    });

            f = b.connect(Constant.Host,Constant.Port).sync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ClientResponse send(ClientRequest request){
        f.channel().writeAndFlush(JSON.toJSONString(request)+"\n");
        FutureResult futureResult = new FutureResult(request);
        return futureResult.get();
    }

    public static void main(String[] args) {
        ClientRequest request = new ClientRequest();
        request.setContent("client:hello hello");
        ClientResponse response = NettyClient.send(request);
        System.out.println(response.getContent());
    }

}

class SimpleClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client active");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ClientResponse response = JSONObject.parseObject(msg.toString(),ClientResponse.class);
        FutureResult.receive(response);
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