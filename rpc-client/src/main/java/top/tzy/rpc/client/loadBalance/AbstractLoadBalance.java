package top.tzy.rpc.client.loadBalance;

import java.util.List;

/**
 * Created by tuzhenyu on 17-12-8.
 * @author tuzhenyu
 */
public abstract class AbstractLoadBalance {
    public abstract String select(List<String> list);
}
