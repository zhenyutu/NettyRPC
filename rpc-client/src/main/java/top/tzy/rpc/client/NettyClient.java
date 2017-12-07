package top.tzy.rpc.client;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import top.tzy.rpc.common.protocol.RpcRequest;
import top.tzy.rpc.common.protocol.RpcResponse;

/**
 * Created by tuzhenyu on 17-12-4.
 * @author tuzhenyu
 */
public class NettyClient {
    private String host;
    private int port;

    public NettyClient(String host,int port){
        this.host = host;
        this.port = port;
    }

    public RpcResponse send(RpcRequest request)throws Exception{
        ChannelFuture f = null;
        Bootstrap b = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new StringEncoder());
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });

            f = b.connect(host,port).sync();

            f.channel().writeAndFlush(JSON.toJSONString(request)+"\n");
            FutureResult futureResult = new FutureResult(request);
            return futureResult.get();

        }finally {
            group.shutdownGracefully();
        }

    }

}