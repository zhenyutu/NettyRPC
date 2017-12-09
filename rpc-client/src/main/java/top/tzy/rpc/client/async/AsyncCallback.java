package top.tzy.rpc.client.async;

/**
 * @author tuzhenyu
 */
public interface AsyncCallback {

    void success(Object result);

    void fail(Exception e);

}
