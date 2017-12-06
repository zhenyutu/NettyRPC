package top.tzy.rpc.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import top.tzy.rpc.common.Constant;

/**
 * Created by tuzhenyu on 17-12-6.
 * @author tuzhenyu
 */

public class ServiceRegistry {
    public static void register(String interfaceName,String serverHost)throws Exception{
        CuratorFramework client = ZookeeperFactory.create();
        String rootPath = Constant.Server_Path;
        if (client.checkExists().forPath(rootPath)==null){
            client.create().forPath(rootPath);
        }
        String servicePath = rootPath+"/"+interfaceName;
        if (client.checkExists().forPath(servicePath)==null){
            client.create().forPath(servicePath);
        }
        String addressPath = servicePath+"/"+"address-";
        client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(addressPath,serverHost.getBytes());
    }
}
