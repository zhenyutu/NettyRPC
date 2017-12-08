package top.tzy.rpc.client.loadBalance;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tuzhenyu on 17-12-8.
 * @author tuzhenyu
 */
public enum LoadBalanceStrategies {

    Random_Strategy(new AbstractLoadBalance() {
        @Override
        public String select(List<String> list) {
            Random random = new Random();
            int index = random.nextInt(list.size());
            return list.get(index);
        }
    }),

    Round_Robin_Strategy(new AbstractLoadBalance() {
        private AtomicInteger index = new AtomicInteger(0);

        @Override
        public String select(List<String> list) {
            return list.get(index.incrementAndGet()%list.size());
        }
    });

    private AbstractLoadBalance abstractLoadBalance;

    LoadBalanceStrategies(AbstractLoadBalance abstractLoadBalance) {
        this.abstractLoadBalance = abstractLoadBalance;
    }

    public String loadBalance(List<String> list){
        return abstractLoadBalance.select(list);
    }
}
