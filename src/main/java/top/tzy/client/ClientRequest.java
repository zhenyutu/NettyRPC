package top.tzy.client;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by tuzhenyu on 17-12-4.
 * @author tuzhenyu
 */
public class ClientRequest {
    private long id;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

    private final AtomicLong aid = new AtomicLong(0);

    public ClientRequest(){
        this.id = aid.incrementAndGet();
    }

    public long getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "requestId='" + id + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }

}
