package top.tzy.server;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tuzhenyu on 17-12-5.
 * @author tuzhenyu
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {
    // 用来指定实现的接口
    Class<?> value();

    // 指定服务版本
    String version() default "";
}
