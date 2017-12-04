package top.tzy.client;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by tuzhenyu on 17-12-4.
 * @author tuzhenyu
 */
public class ClientRequest {
    private long id;
    private Object content;
    private final AtomicLong aid = new AtomicLong(0);

    public ClientRequest(){
        this.id = aid.incrementAndGet();
    }

    public long getId() {
        return id;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
