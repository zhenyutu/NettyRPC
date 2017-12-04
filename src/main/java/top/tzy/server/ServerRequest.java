package top.tzy.server;

/**
 * Created by tuzhenyu on 17-12-4.
 * @author tuzhenyu
 */
public class ServerRequest {
    private long id;
    private String cotent;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCotent() {
        return cotent;
    }

    public void setCotent(String cotent) {
        this.cotent = cotent;
    }
}
