package com.mocha.framework.process.common.mybatis;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mocha.mc.core.enums.IEnum;

import java.io.IOException;

/**
 * 实现接口 类的序列化.
 */
public class EnumIntKeySerializer<T extends IEnum<E>, E extends Enum<E>> extends JsonSerializer<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(final T value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.getKey());
    }

}
