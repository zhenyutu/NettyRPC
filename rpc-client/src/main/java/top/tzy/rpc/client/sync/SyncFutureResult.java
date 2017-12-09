package top.tzy.rpc.client.sync;

import top.tzy.rpc.common.protocol.RpcRequest;
import top.tzy.rpc.common.protocol.RpcResponse;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tuzhenyu on 17-12-4.
 * @author tuzhenyu
 */
public class SyncFutureResult {

    private static final ConcurrentHashMap<String,SyncFutureResult> results = new ConcurrentHashMap<String, SyncFutureResult>();
    private RpcResponse response = null;

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public SyncFutureResult(RpcRequest request){
        results.put(request.getId(),this);
    }

    public RpcResponse get(){
        lock.lock();
        try {
            while (!isDone()){
                condition.await();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return response;
    }

    private boolean isDone(){
        return response!=null;
    }

    public static void receive(RpcResponse response){
        SyncFutureResult result = results.get(response.getId());
        if (result!=null){
            Lock lock = result.lock;
            lock.lock();
            try {
                result.setResponse(response);
                result.condition.signal();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

        }
    }

    public RpcResponse getResponse() {
        return response;
    }

    public void setResponse(RpcResponse response) {
        this.response = response;
    }
}
