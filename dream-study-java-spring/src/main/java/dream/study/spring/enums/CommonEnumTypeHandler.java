package dream.study.spring.enums;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 整合MyBatis增删改查枚举
 *
 * @author 飞花梦影
 * @date 2024-05-22 17:43:33
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public abstract class CommonEnumTypeHandler<T extends Enum<T> & CommonEnum> extends BaseTypeHandler<T> {

	private final List<T> commonEnums;

	protected CommonEnumTypeHandler(T[] commonEnums) {
		this(Arrays.asList(commonEnums));
	}

	protected CommonEnumTypeHandler(List<T> commonEnums) {
		this.commonEnums = commonEnums;
	}

	@Override
	public void setNonNullParameter(PreparedStatement preparedStatement, int i, T t, JdbcType jdbcType)
			throws SQLException {
		preparedStatement.setInt(i, t.getValue());
	}

	@Override
	public T getNullableResult(ResultSet resultSet, String columnName) throws SQLException {

		int code = resultSet.getInt(columnName);
		return commonEnums.stream()
				.filter(commonEnum -> commonEnum.match(String.valueOf(code)))
				.findFirst()
				.orElse(null);
	}

	@Override
	public T getNullableResult(ResultSet resultSet, int i) throws SQLException {
		int code = resultSet.getInt(i);
		return commonEnums.stream()
				.filter(commonEnum -> commonEnum.match(String.valueOf(code)))
				.findFirst()
				.orElse(null);
	}

	@Override
	public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
		int code = callableStatement.getInt(i);
		return commonEnums.stream()
				.filter(commonEnum -> commonEnum.match(String.valueOf(code)))
				.findFirst()
				.orElse(null);
	}
}