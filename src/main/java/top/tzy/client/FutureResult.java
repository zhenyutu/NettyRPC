package top.tzy.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tuzhenyu on 17-12-4.
 * @author tuzhenyu
 */
public class FutureResult {

    private static final ConcurrentHashMap<Long,FutureResult> results = new ConcurrentHashMap<Long, FutureResult>();
    private ClientResponse response = null;

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public FutureResult(ClientRequest request){
        results.put(request.getId(),this);
    }

    public ClientResponse get(){
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

    public static void recive(ClientResponse response){
        FutureResult result = results.get(response.getId());
        if (result!=null){
            result.setResponse(response);
            result.condition.signal();
        }
    }

    public ClientResponse getResponse() {
        return response;
    }

    public void setResponse(ClientResponse response) {
        this.response = response;
    }
}
