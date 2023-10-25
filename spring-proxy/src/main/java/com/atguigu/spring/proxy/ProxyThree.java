package com.atguigu.spring.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author：zsj
 * @Date：2023/6/27
 */
public class ProxyThree implements MethodInterceptor {

    @Override
    public Object intercept(final Object o, final Method method, final Object[] objects, final MethodProxy methodProxy)
        throws Throwable {
        final Object result;
        System.out.println("Before say hello...");
        // todo
        //  cligb代理实际上是为被代理类生成一个子类  也就是代理类
        //  这里如果是调用 method.invoke(o, args) 时，因为这里的o是代理对象实例 method是代理方法实例
        //  当你调用 method.invoke(o, args) 时，实际上是在递归地调用代理方法，这会导致栈溢出或死循环等问题。
        // 当你调用 method.invoke(o, args) 方法时，这个方法实际上会导致代理方法的递归调用。
        // 这是因为，代理方法中通常会再次调用被代理对象的方法，以实现代理的功能。
        //具体来说，当你调用代理方法时，
        // 代理方法内部会使用 method.invoke(o, args) 来调用被代理对象的相应方法。
        // 这样就会进入下一层调用，相当于在代理方法中再次调用了代理方法，形成了递归调用。
        //递归调用的目的是为了在代理方法中能够添加额外的逻辑，
        // 例如在方法调用前后进行一些处理。这种递归调用的方式可以实现方法的拦截、日志记录、性能监控等功能。
        // 通过递归调用可以在不修改原始对象代码的情况下，对方法进行增强或附加额外的操作。
        //因此，虽然你只调用了一次 method.invoke(o, args) 方法，但在代理方法内部会递归地调用代理方法，从而达到了方法增强的效果。
        //  使用 methodProxy.invokeSuper(o, args) 方法调用被代理对象的方法，这样就可以避免递归调用导致的问题了。

        //当代理对象实例 o 调用代理方法实例 method 时，实际上是在间接地调用代理对象实例 o 的此方法本身，
        // 这会导致递归调用代理方法实例 method，即方法自我调用，最终可能导致栈溢出或死循环等问题。
        // 这里需要理解的是代理对象实例 o 和代理方法实例 method 是两个不同的实例。
        // 代理对象实例是代理类的一个实例，代理方法实例是代理类中的一个方法。
        //当代理对象实例调用代理方法实例时，会创建一个当前方法的新栈帧并将其入栈。
        // 当方法调用完成时，栈帧会出栈并返回到调用该方法的栈帧。
        // 如果递归调用代理方法实例，会不断创建新的栈帧并入栈，最终可能导致栈溢出或死循环等问题。
        //因此，为避免递归调用代理方法实例导致的问题，应该使用 MethodProxy 对象来调用被代理类的方法，而不是直接调用当前方法。
        // 正确的实现方式是在 MethodProxy 上调用 invokeSuper 方法，从而调用被代理对象的方法。
        //总之，代理对象实例调用代理方法实例相当于递归调用代理方法实例，会引起栈溢出或死循环等问题，应该避免这种实现方式

       //   result = methodProxy.invoke(target, objects);
        //  invokeSuper 是 cglib 库中的一个方法，用于调用被代理类的方法，
        //  因此在使用 java.lang.reflect.Proxy 类创建代理对象时，不能使用 invokeSuper 方法。
        //  methodProxy.invokeSuper(o, args) 是 cglib 动态代理使用 MethodProxy 对象调用被代理类的方法的方法，
        //  相当于子类(代理类)调用父类(被代理类)的方法   o就是被代理类
        //  而 method.invoke(target, args) 是使用 Java 反射机制调用被代理类的方法的方法，不是一个代理技术。
        result = methodProxy.invokeSuper(o, objects);
        System.out.println("After say hello...");
        return result;
    }

}
