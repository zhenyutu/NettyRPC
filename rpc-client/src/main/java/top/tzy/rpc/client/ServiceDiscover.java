package top.tzy.rpc.client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import top.tzy.rpc.client.loadBalance.LoadBalanceStrategies;
import top.tzy.rpc.common.Constant;
import top.tzy.rpc.registry.ZookeeperFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tuzhenyu on 17-12-6.
 * @author tuzhenyu
 */

public class ServiceDiscover {

    private volatile static ServiceDiscover INSTANCE = new ServiceDiscover();
    private ConcurrentHashMap<String,List<String>> services  = new ConcurrentHashMap<String, List<String>>();


    public static ServiceDiscover getInstance(){
        return INSTANCE;
    }

    private ServiceDiscover(){
        final CuratorFramework client = ZookeeperFactory.create();
        try {
            List<String> interfaceNames = client.getChildren().forPath(Constant.Server_Path);
            final PathChildrenCache cache = new PathChildrenCache(client,Constant.Server_Path,true);
            cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            cache.getListenable().addListener(new PathChildrenCacheListener() {
                public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                    switch (pathChildrenCacheEvent.getType()){
                        case CHILD_ADDED:
                            register(client,convertPathToName(pathChildrenCacheEvent.getData().getPath()));
                            break;
                        case CHILD_REMOVED:
                            remove(convertPathToName(pathChildrenCacheEvent.getData().getPath()));
                            break;
                        default:
                            break;
                    }
                }
            });

            for (String interfaceName : interfaceNames){
                register(client,interfaceName);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void register(final CuratorFramework client,String interfaceName)throws Exception{
        final String servicePath = Constant.Server_Path+"/"+interfaceName;
        List<String> addressNode = client.getChildren().forPath(servicePath);

        final PathChildrenCache cache1 = new PathChildrenCache(client,servicePath,true);
        cache1.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache1.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                switch (pathChildrenCacheEvent.getType()){
                    case CHILD_ADDED:
                        registerNode(client,pathChildrenCacheEvent.getData().getPath());
                        break;
                    case CHILD_REMOVED:
                        removeNode(client,pathChildrenCacheEvent.getData().getPath());
                        break;
                    default:
                        break;
                }
            }
        });

        List<String> list = new ArrayList<String>();
        for (String node : addressNode){
            list.add(new String(client.getData().forPath(servicePath+"/"+node)));
        }
        services.put(interfaceName,list);
    }

    private void remove(String interfaceName){
        List<String> list = services.get(interfaceName);
        for (String address : list){
            ConnectionManager.getInstance().removeConnection(address);
        }
    }

    private void registerNode(CuratorFramework client,String path)throws Exception{
        String[] strs = path.split("/");
        String interfaceName = strs[strs.length-2];
        String address = new String(client.getData().forPath(path));
        List<String> list = services.get(interfaceName);
        list.add(address);
    }

    private void removeNode(CuratorFramework client,String path)throws Exception{
        String[] strs = path.split("/");
        String interfaceName = strs[strs.length-2];
        String address = new String(client.getData().forPath(path));
        List<String> list = services.get(interfaceName);
        list.remove(address);

        ConnectionManager.getInstance().removeConnection(address);
    }

    public String discover(String interfaceName)throws Exception{
        List<String> addressList = services.get(interfaceName);
        String address;
        int size = addressList.size();
        if (size == 1) {
            address = addressList.get(0);
        } else {
            address = LoadBalanceStrategies.Random_Strategy.loadBalance(addressList);
        }

        return address;
    }



    private String convertPathToName(String path){
        String[] strs = path.split("/");
        return strs[strs.length-1];
    }
}
