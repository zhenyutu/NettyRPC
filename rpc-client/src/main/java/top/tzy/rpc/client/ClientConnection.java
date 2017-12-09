package top.tzy.rpc.client;

import com.alibaba.fastjson.JSON;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
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
import top.tzy.rpc.client.async.AsyncFutureResult;
import top.tzy.rpc.client.sync.SyncFutureResult;
import top.tzy.rpc.common.protocol.RpcRequest;
import top.tzy.rpc.common.protocol.RpcResponse;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by tuzhenyu on 17-12-4.
 * @author tuzhenyu
 */
public class ClientConnection {
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));
    private ChannelFuture f = null;

    public ClientConnection(String host, int port){
        Bootstrap b = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new StringEncoder());
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });

            f = b.connect(host, port).sync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public RpcResponse send(RpcRequest request)throws Exception{
        if (f==null)
            throw new RuntimeException("channel is empty");
        SyncFutureResult futureResult = new SyncFutureResult(request);
        f.channel().writeAndFlush(JSON.toJSONString(request)+"\n");

        return futureResult.get();
    }

    public RpcResponse sendTimeout(RpcRequest request)throws Exception{
        RpcResponse response = null;
        if (f==null)
            throw new RuntimeException("channel is empty");
        SyncFutureResult futureResult = new SyncFutureResult(request);
        f.channel().writeAndFlush(JSON.toJSONString(request)+"\n");

        for (;;){
            response = futureResult.get(100);
            if (response!=null)
                break;
        }
        return response;
    }

    public AsyncFutureResult call(RpcRequest request){
        if (f==null)
            throw new RuntimeException("channel is empty");
        AsyncFutureResult futureResult = new AsyncFutureResult(request);
        f.channel().writeAndFlush(JSON.toJSONString(request)+"\n");
        return futureResult;
    }

    public static void submit(Runnable task){
        threadPoolExecutor.submit(task);
    }

}