package top.tzy.client;

/**
 * Created by tuzhenyu on 17-12-4.
 * @author tuzhenyu
 */
public class ClientResponse {
    private long id;
    private Object content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
