package com.atguigu.spring.test;

import com.atguigu.spring.aop.annotation.Calculator;
import com.atguigu.spring.aop.annotation.CalculatorImpl;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Date:2022/7/4
 * Author:zsj
 * Description:
 */
public class AOPByAnnotationTest {


    // todo
    // 通过XML配置和通过注解方式定义切面的执行顺序不一致的原因是因为它们的底层实现机制不同。
    //当使用XML配置时，切面的执行顺序由AOP配置元素在XML文件中的顺序决定。
    // 即使没有显式地指定切面的order属性，Spring AOP会按照配置的顺序依次执行通知。
    //而使用基于注解的方式时，切面的执行顺序是由注解本身的声明顺序决定的。
    // 基于注解的切面是通过扫描类路径并查找带有切面注解的组件来实现的。Spring会根据注解扫描的顺序来决定切面的执行顺序.
    //因此，即使没有显式地指定切面的order属性，使用XML配置和使用注解方式定义的切面的执行顺序仍然可能不一致。
    // 如果想要确保切面的执行顺序一致，可以通过在切面类上使用@Order注解或实现Order
    // 接口来显式地指定切面的执行顺序。这样，Spring会根据指定的顺序来执行切面的通知。

    @Test
    public void testAOPByAnnotation(){
        ApplicationContext ioc = new ClassPathXmlApplicationContext("aop-annotation.xml");
        Calculator calculator = ioc.getBean(Calculator.class);
        calculator.div(10, 1);
    }

}
