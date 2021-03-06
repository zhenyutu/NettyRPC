package top.tzy.rpc;

import top.tzy.rpc.api.HelloService;
import top.tzy.rpc.client.sync.SyncServiceProxy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by tuzhenyu on 17-12-7.
 * @author tuzhenyu
 */
public class ClientEvaluate {
    private static int processors = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws Exception{
        SyncServiceProxy proxy = new SyncServiceProxy();
        final HelloService service = proxy.create(HelloService.class);
        System.out.println(processors);

        long count0 = 5000;
        long start0 = System.currentTimeMillis();
        for (long i=0;i<count0;i++){
            service.hello("tuzhenyu");
        }
        long second0 = (System.currentTimeMillis() - start0) / 1000;
        System.out.println("Request count: " + count0 + ", time: " + second0 + " second, qps: " + count0 / second0);
        final int t = 50000;
        final int step = 2;
        long start = System.currentTimeMillis();
        final CountDownLatch latch = new CountDownLatch(processors * step);
        final AtomicLong count = new AtomicLong();
        for (int i = 0; i < (processors * step); i++) {
            new Thread(new Runnable() {

                public void run() {
                    for (int i = 0; i < t; i++) {
                        try {
                            service.hello("tuzhenyu");

                            if (count.getAndIncrement() % 10000 == 0) {
                                System.out.println(("count=" + count.get()));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    latch.countDown();
                }
            }).start();
        }
        try {
            latch.await();
            System.out.println("count=" + count.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long second = (System.currentTimeMillis() - start) / 1000;
        System.out.println("Request count: " + count.get() + ", time: " + second + " second, qps: " + count.get() / second);
    }

}
