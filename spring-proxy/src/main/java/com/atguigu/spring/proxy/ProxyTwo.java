package com.atguigu.spring.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author：zsj
 * @Date：2023/6/27
 */

public class ProxyTwo implements InvocationHandler {
    private CalculatorImpl target;

    public ProxyTwo(final CalculatorImpl target) {

        this.target = target;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        System.out.println("Before say hello...");
        Object result = method.invoke(target,args);
        System.out.println("After say hello...");
        return result;
    }

}
