package com.mocha.framework.process.common.mybatis;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.EnumDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.mocha.mc.core.enums.IEnum;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 返序列化实现了接口 IEnum 的类.
 *
 * @param <T> 枚举
 * @param <E> 枚举
 * @author 郭洪利
 */
public class EnumCustomDeserializer<T extends IEnum<E>, E extends Enum<E>> extends JsonDeserializer<T> implements ContextualDeserializer {
    /**
     * byStrKey byName 类型集合 静态常量.
     * <p>
     *     将`STRING_VALUE_TO_ENUM_FUNCTIONS`定义成静态常量的好处在于不需要每次使用时都重复创建这个集合。这样可以节省内存并提高代码的可读性和可维护性
     * </p>
     * <pre>
     *     声明了一个私有的静态常量`STR_KEY_OR_NAME_FUN`，它是一个`Collection` 集合，包含了两个元素。每个元素都是一个`BiFunction`对象，
     *     用来将字符串转换为枚举类型。这两个元素分别使用了两个方法引用：
     *
     * -`EnumCustomDeserializer::byStrKey`：这个静态方法用于通过枚举类型定义的字符串键来获取一个枚举值。
     * -`EnumCustomDeserializer::byName`：这个静态方法用于通过枚举类型定义的名称来获取一个枚举值。
     *
     * 这个`STRING_VALUE_TO_ENUM_FUNCTIONS`集合可以用于在反序列化过程中通过字符串键或名称来获取枚举值。
     *
     * </pre>
     */
    @SuppressWarnings("rawtypes") // 消除类型警告,因为静态属性没有泛型这里曲线救国，在使用的地方强转
    private static final Collection<BiFunction<EnumCustomDeserializer, String, EnumConvert>> STRING_VALUE_TO_ENUM_FUNCTIONS
        = Arrays.asList(EnumCustomDeserializer::byStrKey,EnumCustomDeserializer::byName);

    /**
     * 枚举转换器.
     */
    private EnumConvert<T, E> convert;


    public EnumCustomDeserializer() {
    }

    /**
     * 初始化.
     *
     * @param tClass 枚举类型
     */
    private EnumCustomDeserializer(final Class<T> tClass) {
        this.convert = EnumConvert.of(tClass);
    }

    /**
     * 当前类中 代理 convert.byStrKey.
     *
     * @param  strKey strKey {@link IEnum#getStrKey()}
     * @return        EnumConvert {@see com.mocha.mc.core.enums.EnumConvert}
     */
    private EnumConvert<T, E> byStrKey(final String strKey) {
        return this.convert.byStrKey(strKey);
    }
    /**
     * 当前类中 代理 convert.byName.
     *
     * @param  name strKey {@link IEnum#getName()}
     * @return        EnumConvert {@see com.mocha.mc.core.enums.EnumConvert}
     */
    private EnumConvert<T, E> byName(final String name) {
        return this.convert.byName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T deserialize(final JsonParser p, final DeserializationContext text) throws IOException {
        final JsonToken curr = p.currentToken();
        final ObjectCodec codec = p.getCodec();
        final JsonNode rootNode = codec.readTree(p);
        if (curr == JsonToken.VALUE_NUMBER_INT) { // 数字类型 使用 intKey
            return convert.byKey(rootNode.asInt()).get();
        }
        // 兼容使用 strKey 和 name 优先匹配 strKey
        if (curr == JsonToken.VALUE_STRING || curr == JsonToken.FIELD_NAME) {
            final String strKey = rootNode.asText();
            return STRING_VALUE_TO_ENUM_FUNCTIONS.stream()
                .map(f -> f.apply(EnumCustomDeserializer.this, strKey))
                .map(EnumConvert::get)
                .filter(Objects::nonNull)
                .findFirst()
                .map(t -> {
                    @SuppressWarnings("unchecked") // 消除类型警告,因为静态属性没有泛型这里曲线救国，在使用的地方强转
                    final T r = (T) t;
                    return r;
                })
                .orElse(null);
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext ctxt,
                                                final BeanProperty property) throws JsonMappingException {
        //beanProperty is null when the type to deserialize is the top-level type or a generic type, not a type of a bean property

        final JavaType type = Stream.<Supplier<JavaType>>of(ctxt::getContextualType, property.getMember()::getType)
            .map(Supplier::get)
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(() -> new JsonMappingException(ctxt.getParser(), "Could not final deserialize object type"));

        @SuppressWarnings("unchecked") // 强制类型转换，消除类型警告
        final Class<T> tClass = (Class<T>) type.getRawClass();

        // 实现IEnum接口的反序列化
        if (IEnum.class.isAssignableFrom(tClass)) {
            return new EnumCustomDeserializer<>(tClass);
        }


        // 枚举默认反序列化 copy 之 @see com.fasterxml.jackson.databind.deser.BasicDeserializerFactory
        final DeserializationConfig config = ctxt.getConfig();
        return new EnumDeserializer(constructEnumResolver(
            tClass,
            config,
            Optional.ofNullable(config.introspect(type))
                .map(BeanDescription::findJsonValueAccessor)
                .orElse(null)),
            config.isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS));
    }

    /**
     * copy 之 @see com.fasterxml.jackson.databind.deser.BasicDeserializerFactory
     */
    private EnumResolver constructEnumResolver(final Class<?> enumClass,
                                               final DeserializationConfig config,
                                               final AnnotatedMember jsonValueAccessor) {
        if (jsonValueAccessor != null) {
            if (config.canOverrideAccessModifiers()) {
                ClassUtil.checkAndFixAccess(jsonValueAccessor.getMember(),
                    config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
            }
            return EnumResolver.constructUsingMethod(config, enumClass, jsonValueAccessor);
        }
        // 14-Mar-2016, tatu: We used to check `DeserializationFeature.READ_ENUMS_USING_TO_STRING`
        // here, but that won't do: it must be dynamically changeable...
        return EnumResolver.constructFor(config, enumClass);
    }

}
