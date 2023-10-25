package com.atguigu.proxy;

import com.atguigu.spring.proxy.Calculator;
import com.atguigu.spring.proxy.CalculatorImpl;
import com.atguigu.spring.proxy.MyInterface;
import com.atguigu.spring.proxy.ProxyInterface;
import com.atguigu.spring.proxy.ProxyOne;
import com.atguigu.spring.proxy.ProxyThree;
import com.atguigu.spring.proxy.ProxyTwo;
import net.sf.cglib.proxy.Enhancer;
import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * Date:2022/7/4
 * Author:zsj
 * Description:
 */
public class ProxyTest {

    /**
     * 动态代理有两种：
     * 1、jdk动态代理，要求必须有接口，最终生成的代理类和目标类实现相同的接口
     * 在com.sun.proxy包下，类名为$proxy2
     * 2、cglib动态代理，最终生成的代理类会继承目标类，并且和目标类在相同的包下
     */

    @Test
    public void testProxy() {

        ProxyOne proxyFactory = new ProxyOne(new CalculatorImpl());
        Calculator proxy = (Calculator) proxyFactory.getProxy();
        proxy.div(1, 1);
    }

    @Test
    public void testProxy1() {

        final ProxyTwo proxyTwo = new ProxyTwo(new CalculatorImpl());

        final Calculator o = (Calculator) Proxy.
            newProxyInstance(getClass().getClassLoader(),
                             new Class[]{Calculator.class},
                             proxyTwo);
        //
        //        Calculator target = new CalculatorImpl();
        //        Calculator proxy = (Calculator) Proxy.newProxyInstance(
        //            getClass().getClassLoader(),
        //            new Class[] { Calculator.class },
        //            new ProxyTwo((CalculatorImpl) target)

        o.div(1, 1);
    }

    @Test
    public void testProxy2() {


        final ProxyThree proxyThree = new ProxyThree();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(CalculatorImpl.class);
        enhancer.setCallback(proxyThree);
        final Calculator o = (Calculator)enhancer.create();
        o.div(1,1);

        final ProxyThree proxyThree1 = new ProxyThree();
        final Enhancer enhancer1 = new Enhancer();
        enhancer1.setSuperclass(CalculatorImpl.class);
        enhancer1.setCallback(proxyThree1);
        final Calculator o1 = (Calculator)enhancer.create();
        o1.div(1, 1);
    }

    @Test
    public void testProxy3() {
        final ProxyInterface proxyThree = new ProxyInterface();
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[] {MyInterface.class});
        enhancer.setCallback(proxyThree);
        final MyInterface o = (MyInterface)enhancer.create();
        o.myMethod("1111");
    }

}
