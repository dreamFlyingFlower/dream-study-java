package com.wy.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.dream.common.StatusMsg;
import com.dream.helper.EnumStatusMsgHelper;
import com.dream.reflect.ClassHelper;

/**
 * 自定义通用枚举处理类
 *
 * @author 飞花梦影
 * @param <E>
 * @date 2023-02-12 23:23:46
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class MyEnumTypeHandler<T extends Enum<T> & StatusMsg> implements TypeHandler<T> {

	/**
	 * 定义当前数据如何保存到数据库中
	 */
	@Override
	public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
		ps.setInt(i, parameter.getCode());
	}

	/**
	 * 获取列的值
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T getResult(ResultSet rs, String columnName) throws SQLException {
		int code = rs.getInt(columnName);
		return EnumStatusMsgHelper.getEnum(code, (Class<T>) ClassHelper.getGenricType(MyEnumTypeHandler.class));
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getResult(ResultSet rs, int columnIndex) throws SQLException {
		int code = rs.getInt(columnIndex);
		return EnumStatusMsgHelper.getEnum(code, (Class<T>) ClassHelper.getGenricType(MyEnumTypeHandler.class));
	}

	/**
	 * 从存储过程中获取
	 */
	@Override
	public T getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return null;
	}
}