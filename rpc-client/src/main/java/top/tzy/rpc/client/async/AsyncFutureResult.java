package top.tzy.rpc.client.async;

import top.tzy.rpc.client.ClientConnection;
import top.tzy.rpc.common.protocol.RpcRequest;
import top.tzy.rpc.common.protocol.RpcResponse;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tuzhenyu on 17-12-4.
 * @author tuzhenyu
 */
public class AsyncFutureResult {

    private static final ConcurrentHashMap<String,AsyncFutureResult> results = new ConcurrentHashMap<String, AsyncFutureResult>();
    private RpcResponse response = null;
    private CountDownLatch countDownLatch = null;

    private CopyOnWriteArrayList<AsyncCallback> callbacks = new CopyOnWriteArrayList<AsyncCallback>();

    public AsyncFutureResult(RpcRequest request){
        results.put(request.getId(),this);
        countDownLatch = new CountDownLatch(1);
    }

    public AsyncFutureResult addCallback(AsyncCallback callback) {

        if (isDone()) {
            runCallback(callback);
        } else {
            this.callbacks.add(callback);
        }

        return this;
    }

    private void runCallback(final AsyncCallback callback) {
        final RpcResponse res = this.response;
        ClientConnection.submit(new Runnable() {
            public void run() {
                if (!res.isError()) {
                    callback.success(res.getContent());
                } else {
                    callback.fail(new RuntimeException("Response error", new Throwable(res.getError())));
                }
            }
        });
    }

    public RpcResponse get(){
        try {
            countDownLatch.await();
        }catch (Exception e){
            e.printStackTrace();
        }

        return response;
    }

    public static void receive(RpcResponse response){
        AsyncFutureResult result = results.get(response.getId());
        if (result!=null){
            result.setResponse(response);
        }
    }

    private boolean isDone(){
        return response!=null;
    }

    public RpcResponse getResponse() {
        return response;
    }

    public void setResponse(RpcResponse response) {
        this.response = response;
        countDownLatch.countDown();

        for (final AsyncCallback callback : callbacks) {
            runCallback(callback);
        }
    }
}
