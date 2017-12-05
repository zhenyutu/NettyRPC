package top.tzy.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import top.tzy.Constant;
import top.tzy.registry.ZookeeperFactory;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tuzhenyu on 17-12-3.
 * @author tuzhenyu
 */
@Component
public class NettyServer implements InitializingBean, ApplicationContextAware {

    private Map<String,Object> services = new HashMap<String,Object>();

    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workGroup).channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new ServerHandler(services));
                        }
                    });
            ChannelFuture f = b.bind(8008).sync();

            CuratorFramework zookeeper = ZookeeperFactory.create();
            InetAddress address = InetAddress.getLocalHost();
            zookeeper.create().withMode(CreateMode.EPHEMERAL).forPath(Constant.Server_Path+address.getHostAddress());

            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,Object> beans = applicationContext.getBeansWithAnnotation(RpcService.class);
        for (Object serviceBean : beans.values()){
            String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
            services.put(interfaceName,serviceBean);
        }
    }
}
