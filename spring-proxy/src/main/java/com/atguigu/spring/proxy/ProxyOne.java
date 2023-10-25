package com.atguigu.spring.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @Author：zsj
 * @Date：2023/6/27
 */

public class ProxyOne {
    private Object target;

    public ProxyOne(final Object target) {
        this.target = target;
    }
    public Object getProxy(){

        final ClassLoader classLoader = target.getClass().getClassLoader();
        final Class<?>[] interfaces = target.getClass().getInterfaces();
        final InvocationHandler invocationHandler = (proxy, method, args) -> {
            Object result = null;
            try {
                System.out.println("日志，方法：" + method.getName() + "，参数：" + Arrays.toString(args));
                //proxy表示代理对象，method表示要执行的方法，args表示要执行的方法到的参数列表
                result = method.invoke(target, args);
                System.out.println("日志，方法："+method.getName()+"，结果："+ result);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("日志，方法："+method.getName()+"，异常："+ e.getMessage());
            } finally {
                System.out.println("日志，方法："+method.getName()+"，方法执行完毕");
            }
            return result;
        };
        return Proxy.newProxyInstance(classLoader,interfaces,invocationHandler);
    }

}
