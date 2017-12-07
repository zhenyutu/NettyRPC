package top.tzy.rpc.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by tuzhenyu on 17-12-3.
 * @author tuzhenyu
 */
@Configuration
@ComponentScan("top.tzy")
public class Provider {
    public static void main(String[] args) {
        start();
    }

    public static void start(){
        ApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(Provider.class);
    }
}
