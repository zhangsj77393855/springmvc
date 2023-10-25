package com.atguigu.spring.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author：zsj
 * @Date：2023/6/28
 */

public class ProxyInterface implements MethodInterceptor ,Calculator{


    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        // 通过 MethodProxy 调用被代理对象的方法
        System.out.println("Before " + method.getName());
        Object result = methodProxy.invokeSuper(o, args);
        System.out.println("After " + method.getName());
        return result;
    }

    @Override public int add(final int i, final int j) {

        return 0;
    }

    @Override public int sub(final int i, final int j) {

        return 0;
    }

    @Override public int mul(final int i, final int j) {

        return 0;
    }

    @Override public int div(final int i, final int j) {

        return 0;
    }

}
