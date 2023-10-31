package com.mocha.framework.process.common.mybatis;

import com.mocha.mc.core.enums.EnumConvert;
import com.mocha.mc.core.enums.IEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * mybatis枚举类型转换.
 *
 * @param <E> 枚举类型
 */
public class EnumTypeKeyHandler<E extends Enum<E> & IEnum<E>> extends BaseTypeHandler<IEnum<E>> {

    /**
     * 枚举类型 .
     */
    private Class<IEnum<E>> type;

    public EnumTypeKeyHandler(){

    }

    /**
     * NONE DOC.
     *
     * @param type 枚举类型
     */
    public EnumTypeKeyHandler(final Class<IEnum<E>> type) {
        if (type == null) { throw new IllegalArgumentException("Type argument cannot be null"); }
        this.type = type;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void setNonNullParameter(final PreparedStatement ps, final int i, final IEnum parameter, final JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getKey());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IEnum<E> getNullableResult(final ResultSet rs, final String columnName) throws SQLException {
        return EnumConvert.of(type).byKey(rs.getInt(columnName)).getOrDefault();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IEnum<E> getNullableResult(final ResultSet rs, final int columnIndex) throws SQLException {
        return EnumConvert.of(type).byKey(rs.getInt(columnIndex)).getOrDefault();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IEnum<E> getNullableResult(final CallableStatement cs, final int columnIndex) throws SQLException {
        return EnumConvert.of(type).byKey(cs.getInt(columnIndex)).getOrDefault();
    }

}
