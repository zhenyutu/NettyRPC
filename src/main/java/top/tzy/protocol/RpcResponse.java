package top.tzy.protocol;

/**
 * 组装rpc response
 * @author tuzhenyu
 */
public class RpcResponse {
    private String requestId;
    private String error;
    // 返回值不能是基本类型
    private Object result;

    public boolean isError() {
        return error != null;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "requestId='" + requestId + '\'' +
                ", error='" + error + '\'' +
                ", result=" + result +
                '}';
    }
}
