package top.tzy.rpc.registry;

import org.apache.curator.framework.CuratorFramework;
import top.tzy.rpc.common.Constant;

import java.util.List;
import java.util.Random;

/**
 * Created by tuzhenyu on 17-12-6.
 * @author tuzhenyu
 */

public class ServiceDiscover {
    public static String discover(String interfaceName)throws Exception{
        CuratorFramework client = ZookeeperFactory.create();
        String servicePath = Constant.Server_Path+"/"+interfaceName;
        if (client.checkExists().forPath(servicePath)==null){
            throw new RuntimeException("no service node");
        }
        List<String> addressList = client.getChildren().forPath(servicePath);
        String address;
        int size = addressList.size();
        if (size == 1) {
            address = addressList.get(0);
        } else {
            address = addressList.get(new Random().nextInt(size));
        }

        String addressPath = servicePath + "/" + address;
        return new String(client.getData().forPath(addressPath));
    }
}
