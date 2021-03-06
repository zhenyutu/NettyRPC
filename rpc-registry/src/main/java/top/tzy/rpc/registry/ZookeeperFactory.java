package top.tzy.rpc.registry;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by tuzhenyu on 17-12-4.
 * @author tuzhenyu
 */
public class ZookeeperFactory {
    private static  CuratorFramework client;

    public static CuratorFramework create(){
        if (client==null){
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
            client.start();
        }
        return client;
    }
}
