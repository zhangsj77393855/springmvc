package com.mocha.framework.process.common.mybatis;

import com.mocha.mc.core.enums.IEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import static com.mocha.mc.core.enums.IEnum.DEFAULT_NONE;

/**
 * 根据枚举值转成枚举.
 *
 * <pre>
 *     1. 支持根据 定义名称
 *     2. 支持根据 数字
 *     3. 支持根据 字符
 * </pre>
 *
 * @param <E> 枚举类型
 * @param <T> 枚举类型
 */
public final class EnumConvert<T extends IEnum<E>, E extends Enum<E>> {

    /**
     * 枚举集合.
     */
    private final List<T> enums;

    /**
     * 找到的枚举值.
     */
    @SuppressWarnings("all")
    private Optional<T> value = Optional.empty();

    /**
     * 初始化.
     *
     * @param enums 枚举集合
     */
    private EnumConvert(final List<T> enums) {
        this.enums = enums;
    }

    /**
     * 初始化.
     *
     * @param  classes 枚举classes 必须是 IEnum 的子类，子类必须是enum类型
     * @param  <T>     待处理类型
     * @param  <E>     待处理枚举类的类型
     * @return         枚举列表
     */
    public static <E extends Enum<E>, T extends IEnum<E>> EnumConvert<T, E> of(final Class<T> classes) {
        if (Objects.isNull(classes)) {
            throw new NullPointerException("Method parameter class is null");
        }
        return new EnumConvert<>(Optional.of(classes)
            .map(Class::getEnumConstants)
            .map(Arrays::asList)
            .orElseGet(Collections::emptyList));
    }

    /**
     * 初始化.
     *
     * @param  enums 枚举集合
     * @param  <T>     待处理类型
     * @param  <E>     待处理枚举类的类型
     * @return         枚举列表
     */
    public static <E extends Enum<E>, T extends IEnum<E>> EnumConvert<T, E> of(final List<T> enums) {
        if (Objects.isNull(enums)) {
            throw new NullPointerException("Method parameter enums is null");
        }
        return new EnumConvert<>(enums);
    }

    /**
     * 根据定义的名称查找枚举.
     *
     * @param  name 定义的枚举
     * @return      当前实例
     */
    public EnumConvert<T, E> byName(final String name) {
        this.value = this.enums.stream().filter(e -> e.getName().equals(name)).findFirst();
        return this;
    }

    /**
     * 根据定义的数字查找枚举.
     *
     * @param  key 数字
     * @return     当前实例
     */
    public EnumConvert<T, E> byKey(final int key) {
        this.value = enums.stream().filter(e -> e.getKey() == key).findFirst();
        return this;
    }

    /**
     * 根据字符key查找枚举.
     *
     * @param  strKey 字符key
     * @return        当前实例
     */
    public EnumConvert<T, E> byStrKey(final String strKey) {
        this.value = enums.stream().filter(e -> e.getStrKey().equals(strKey)).findFirst();
        return this;
    }

    /**
     * 返回找到枚举，未找到时返回null.
     *
     * @return 枚举
     */
    public T get() {
        return this.value.orElse(null);
    }

    /**
     * 返回找到枚举，未找到时抛出定义的异常.
     *
     * @param  exceptionSupplier 异常
     * @param  <X>               异常类型
     * @return                   枚举
     */
    public <X extends RuntimeException> T getOrThrow(final Supplier<? extends X> exceptionSupplier) {
        return this.value.orElseThrow(exceptionSupplier);
    }

    /**
     * 返回找到枚举，未找到时返回定义的默认枚举（NONE）.
     *
     * @return 枚举
     */
    public T getOrDefault() {
        return this.value.orElseGet(() -> byName(DEFAULT_NONE).get());
    }

}
