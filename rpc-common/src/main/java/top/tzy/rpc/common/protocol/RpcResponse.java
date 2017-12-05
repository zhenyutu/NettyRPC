package top.tzy.rpc.common.protocol;

/**
 * 组装rpc response
 * @author tuzhenyu
 */
public class RpcResponse {
    private String id;
    private String error;
    // 返回值不能是基本类型
    private Object content;

    public boolean isError() {
        return error != null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setRequestId(String id) {
        this.id = id;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "id='" + id + '\'' +
                ", error='" + error + '\'' +
                ", content=" + content +
                '}';
    }
}
