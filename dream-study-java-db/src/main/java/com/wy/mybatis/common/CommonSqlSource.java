package com.wy.mybatis.common;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.RowConstructor;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;
import net.sf.jsqlparser.util.TablesNamesFinder;

/**
 * 公共参数自动新增,修改,查询
 *
 * @author 飞花梦影
 * @date 2024-01-04 14:30:33
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
@Component
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query",
				args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }),
		@Signature(type = Executor.class, method = "query",
				args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class,
						BoundSql.class }),
		@Signature(type = Executor.class, method = "queryCursor",
				args = { MappedStatement.class, Object.class, RowBounds.class }) })
public class CommonSqlSource implements Interceptor {

	private static final String CREATE_USER_COLUMN = "create_user";

	private static final String CREATE_TIME_COLUMN = "create_time";

	private static final String UPDATE_USER_COLUMN = "update_user";

	private static final String UPDATE_TIME_COLUMN = "update_time";

	private static final String CREATE_USER_PROPERTY = "createUser";

	private static final String UPDATE_USER_PROPERTY = "updateUser";

	private static final String CREATE_TIME_PROPERTY = "createTime";

	private static final String UPDATE_TIME_PROPERTY = "updateTime";

	private static volatile Configuration modifiedConfiguration;

	private static final Object SYNC_LOCK = new Object();

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 被代理对象
		Object target = invocation.getTarget();
		// 代理方法
		Method method = invocation.getMethod();
		// 方法参数
		Object[] args = invocation.getArgs();

		MappedStatement mappedStatement = (MappedStatement) args[0];

		// 初次进入拦截器 构建Configuration
		if (modifiedConfiguration == null) {
			synchronized (SYNC_LOCK) {
				if (modifiedConfiguration == null) {
					modifiedConfiguration = createConfiguration(mappedStatement.getConfiguration());
				}
			}
		}

		// 从中取得修改后的MappedStatement
		MappedStatement newModifiedMappedStatement = modifiedConfiguration.getMappedStatement(mappedStatement.getId());

		Object[] newArgs = args.clone();
		newArgs[0] = newModifiedMappedStatement;
		return method.invoke(target, newArgs);
		// return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Interceptor.super.plugin(target);
	}

	@Override
	public void setProperties(Properties properties) {
		Interceptor.super.setProperties(properties);
	}

	public MappedStatement buildMappedStatement(Configuration newModifiedConfiguration,
			MappedStatement mappedStatement) {
		SqlSource modifiedSqlSource = new CustomizeSqlSource(mappedStatement, newModifiedConfiguration);

		List<ResultMap> modifiedResultMaps = mappedStatement.getResultMaps().stream().map((resultMap) -> {
			List<ResultMapping> resultMappingList = resultMap.getResultMappings();
			// 为每个resultMap中的resultMappingList添加公共参数映射
			List<ResultMapping> modifiedResultMappingList =
					addResultMappingProperty(newModifiedConfiguration, resultMappingList, resultMap.getType());
			return new ResultMap.Builder(newModifiedConfiguration, resultMap.getId(), resultMap.getType(),
					modifiedResultMappingList, resultMap.getAutoMapping()).build();
		}).collect(Collectors.toList());

		// 构造新MappedStatement 替换SqlSource、ResultMap、Configuration
		MappedStatement.Builder newMappedStatementBuilder = new MappedStatement.Builder(newModifiedConfiguration,
				mappedStatement.getId(), modifiedSqlSource, mappedStatement.getSqlCommandType())
						.cache(mappedStatement.getCache()).databaseId(mappedStatement.getDatabaseId())
						// .dirtySelect(mappedStatement.isDirtySelect())
						.fetchSize(mappedStatement.getFetchSize())
						.flushCacheRequired(mappedStatement.isFlushCacheRequired())
						.keyGenerator(mappedStatement.getKeyGenerator()).lang(mappedStatement.getLang())
						.parameterMap(mappedStatement.getParameterMap()).resource(mappedStatement.getResource())
						.resultMaps(modifiedResultMaps).resultOrdered(mappedStatement.isResultOrdered())
						.resultSetType(mappedStatement.getResultSetType())
						.statementType(mappedStatement.getStatementType()).timeout(mappedStatement.getTimeout())
						.useCache(mappedStatement.isUseCache());
		if (mappedStatement.getKeyColumns() != null) {
			newMappedStatementBuilder.keyColumn(
					StringUtils.collectionToDelimitedString(Arrays.asList(mappedStatement.getKeyColumns()), ","));
		}
		if (mappedStatement.getKeyProperties() != null) {
			newMappedStatementBuilder.keyProperty(
					StringUtils.collectionToDelimitedString(Arrays.asList(mappedStatement.getKeyProperties()), ","));
		}
		if (mappedStatement.getResultSets() != null) {
			newMappedStatementBuilder.resultSets(
					StringUtils.collectionToDelimitedString(Arrays.asList(mappedStatement.getResultSets()), ","));
		}
		return newMappedStatementBuilder.build();
	}

	private Configuration createConfiguration(Configuration sourceConfig) {
		Configuration targetConfig = new Configuration();

		targetConfig.setEnvironment(sourceConfig.getEnvironment());
		targetConfig.setAutoMappingUnknownColumnBehavior(sourceConfig.getAutoMappingUnknownColumnBehavior());
		targetConfig.setCacheEnabled(sourceConfig.isCacheEnabled());
		targetConfig.setLazyLoadingEnabled(sourceConfig.isLazyLoadingEnabled());
		targetConfig.setAggressiveLazyLoading(sourceConfig.isAggressiveLazyLoading());
		targetConfig.setArgNameBasedConstructorAutoMapping(sourceConfig.isArgNameBasedConstructorAutoMapping());
		targetConfig.setAutoMappingBehavior(targetConfig.getAutoMappingBehavior());
		targetConfig.setCallSettersOnNulls(sourceConfig.isCallSettersOnNulls());
		targetConfig.setConfigurationFactory(sourceConfig.getConfigurationFactory());
		targetConfig.setDatabaseId(sourceConfig.getDatabaseId());
		// targetConfig.setDefaultEnumTypeHandler();
		targetConfig.setDefaultExecutorType(sourceConfig.getDefaultExecutorType());
		targetConfig.setDefaultFetchSize(sourceConfig.getDefaultFetchSize());
		targetConfig.setDefaultResultSetType(sourceConfig.getDefaultResultSetType());
		targetConfig.setDefaultScriptingLanguage(sourceConfig.getDefaultScriptingLanguageInstance().getClass());
		targetConfig.setDefaultSqlProviderType(sourceConfig.getDefaultSqlProviderType());
		targetConfig.setDefaultStatementTimeout(sourceConfig.getDefaultStatementTimeout());
		targetConfig.setJdbcTypeForNull(sourceConfig.getJdbcTypeForNull());
		targetConfig.setLazyLoadTriggerMethods(sourceConfig.getLazyLoadTriggerMethods());
		targetConfig.setLocalCacheScope(sourceConfig.getLocalCacheScope());
		targetConfig.setLogImpl(sourceConfig.getLogImpl());
		targetConfig.setLogPrefix(sourceConfig.getLogPrefix());
		targetConfig.setMapUnderscoreToCamelCase(sourceConfig.isMapUnderscoreToCamelCase());
		targetConfig.setMultipleResultSetsEnabled(sourceConfig.isMultipleResultSetsEnabled());
		targetConfig.setNullableOnForEach(sourceConfig.isNullableOnForEach());
		targetConfig.setObjectFactory(sourceConfig.getObjectFactory());
		targetConfig.setObjectWrapperFactory(sourceConfig.getObjectWrapperFactory());
		targetConfig.setProxyFactory(sourceConfig.getProxyFactory());
		targetConfig.setReflectorFactory(sourceConfig.getReflectorFactory());
		targetConfig.setReturnInstanceForEmptyRow(sourceConfig.isReturnInstanceForEmptyRow());
		targetConfig.setSafeResultHandlerEnabled(sourceConfig.isSafeResultHandlerEnabled());
		targetConfig.setSafeRowBoundsEnabled(sourceConfig.isSafeRowBoundsEnabled());
		targetConfig.setShrinkWhitespacesInSql(sourceConfig.isShrinkWhitespacesInSql());
		targetConfig.setUseActualParamName(sourceConfig.isUseActualParamName());
		targetConfig.setUseColumnLabel(sourceConfig.isUseColumnLabel());
		targetConfig.setUseGeneratedKeys(sourceConfig.isUseGeneratedKeys());
		targetConfig.setVariables(sourceConfig.getVariables());
		targetConfig.setVfsImpl(sourceConfig.getVfsImpl());

		sourceConfig.getInterceptors().forEach(targetConfig::addInterceptor);
		sourceConfig.getCaches().forEach((cache) -> {
			if (!targetConfig.hasCache(cache.getId())) {
				targetConfig.addCache(cache);
			}
		});

		// 预处理 添加映射
		sourceConfig.getResultMaps().forEach((resultMap) -> {
			List<ResultMapping> resultMappingList = resultMap.getResultMappings();
			// 为每个resultMap中的resultMappingList添加公共参数映射(resultMapList中已包含嵌套对象 故这里无需递归修改)
			List<ResultMapping> modifiedResultMappingList =
					addResultMappingProperty(targetConfig, resultMappingList, resultMap.getType());

			ResultMap newModifiedResultMap = new ResultMap.Builder(targetConfig, resultMap.getId(), resultMap.getType(),
					modifiedResultMappingList, resultMap.getAutoMapping()).build();
			// 长的resultMapName 如com.live.mapper.TestMapper.findByState-String
			// 底层add到map内之前会将短名称做key也加入至map
			if (!targetConfig.hasResultMap(resultMap.getId())) {
				targetConfig.addResultMap(newModifiedResultMap);
			}
		});

		// targetConfig.addCacheRef();
		sourceConfig.getIncompleteCacheRefs().forEach(targetConfig::addIncompleteCacheRef);
		sourceConfig.getIncompleteMethods().forEach(targetConfig::addIncompleteMethod);
		sourceConfig.getIncompleteResultMaps().forEach(targetConfig::addIncompleteResultMap);
		sourceConfig.getIncompleteStatements().forEach(targetConfig::addIncompleteStatement);
		sourceConfig.getKeyGeneratorNames().forEach((keyGeneratorName -> {
			if (!targetConfig.hasKeyGenerator(keyGeneratorName)) {
				targetConfig.addKeyGenerator(keyGeneratorName, sourceConfig.getKeyGenerator(keyGeneratorName));
			}
		}));

		sourceConfig.getMappedStatements().forEach((mappedStatement -> {
			if (!targetConfig.hasStatement(mappedStatement.getId())) {
				// 添加修改后的MappedStatement
				targetConfig.addMappedStatement(buildMappedStatement(targetConfig, mappedStatement));
			}
		}));
		sourceConfig.getParameterMaps().forEach((parameterMap -> {
			if (!targetConfig.hasParameterMap(parameterMap.getId())) {
				targetConfig.addParameterMap(parameterMap);
			}
		}));

		return targetConfig;
	}

	private static List<ResultMapping> addResultMappingProperty(Configuration configuration,
			List<ResultMapping> resultMappingList, Class<?> mappedType) {
		// resultMappingList为不可修改对象
		List<ResultMapping> modifiableResultMappingList = new ArrayList<>(resultMappingList);

		String[] checkList = { CREATE_USER_PROPERTY, CREATE_TIME_PROPERTY, UPDATE_USER_PROPERTY, UPDATE_TIME_PROPERTY };
		boolean hasAnyTargetProperty = Arrays.stream(checkList)
				.anyMatch((property) -> ReflectionUtils.findField(mappedType, property) != null);

		// 用于防止映射目标为基本类型却被添加映射 导致列名规则 表名_列名 无法与映射的列名的添加规则 映射类型名_列名 相照应
		// 从而导致映射类型为基本类型时会生成出类似与string_column1的映射名 而产生找不到映射列名与实际结果列相照应的列名导致mybatis产生错误
		// 规则: 仅映射类型中包含如上四个字段其一时才会添加映射
		if (hasAnyTargetProperty) {
			// 支持类型使用驼峰命名
			String currentTable = upperCamelToLowerUnderscore(mappedType.getSimpleName());

			// 映射方式 表名_公共字段名 在实体中 表名与实体名相同 则可完成映射
			modifiableResultMappingList.add(new ResultMapping.Builder(configuration, CREATE_USER_PROPERTY,
					currentTable + "_" + CREATE_USER_COLUMN, String.class).build());
			modifiableResultMappingList.add(new ResultMapping.Builder(configuration, CREATE_TIME_PROPERTY,
					currentTable + "_" + CREATE_TIME_COLUMN, Timestamp.class).build());
			modifiableResultMappingList.add(new ResultMapping.Builder(configuration, UPDATE_USER_PROPERTY,
					currentTable + "_" + UPDATE_USER_COLUMN, String.class).build());
			modifiableResultMappingList.add(new ResultMapping.Builder(configuration, UPDATE_TIME_PROPERTY,
					currentTable + "_" + UPDATE_TIME_COLUMN, Timestamp.class).build());
		}

		return modifiableResultMappingList;
	}

	private static String upperCamelToLowerUnderscore(String source) {
		StringBuilder convertBuffer = new StringBuilder();
		for (int i = 0; i < source.length(); i++) {
			char currentChar = source.charAt(i);
			if (Character.isUpperCase(currentChar)) {
				if (i != 0) {
					convertBuffer.append('_');
				}
				convertBuffer.append(Character.toLowerCase(currentChar));
			} else {
				convertBuffer.append(currentChar);
			}
		}
		return convertBuffer.toString();
	}

	static class CustomizeSqlSource implements SqlSource {

		private final MappedStatement mappedStatement;

		private final Configuration configuration;

		public CustomizeSqlSource(MappedStatement mappedStatement, Configuration configuration) {
			this.mappedStatement = mappedStatement;
			this.configuration = configuration;
		}

		@Override
		public BoundSql getBoundSql(Object parameterObject) {
			// 获取原始的 BoundSql 对象
			BoundSql originalBoundSql = mappedStatement.getSqlSource().getBoundSql(parameterObject);

			// 获取原始的 SQL 字符串
			String originalSql = originalBoundSql.getSql();
			log.debug("公共参数添加 - 修改前SQL:{}", originalSql);

			// 创建新的 BoundSql 对象
			String modifiedSql;
			try {
				modifiedSql = buildSql(originalSql);
				log.debug("公共参数添加 - 修改后SQL:{}", modifiedSql);
			} catch (JSQLParserException e) {
				log.error("JSQLParser解析修改SQL添加公共参数失败, 继续使用原始SQL执行", e);
				modifiedSql = originalSql;
			}
			BoundSql modifiedBoundSql =
					new BoundSql(configuration, modifiedSql, originalBoundSql.getParameterMappings(), parameterObject);
			// 复制其他属性
			originalBoundSql.getAdditionalParameters().forEach(modifiedBoundSql::setAdditionalParameter);
			modifiedBoundSql.setAdditionalParameter("_parameter", parameterObject);

			return modifiedBoundSql;
		}

		private String buildSql(String originalSql) throws JSQLParserException {
			Statement statement = CCJSqlParserUtil.parse(originalSql);
			if (SqlCommandType.INSERT == mappedStatement.getSqlCommandType() && statement instanceof Insert) {
				Insert insert = (Insert) statement;
				insert.addColumns(new Column(CREATE_USER_COLUMN), new Column(CREATE_TIME_COLUMN));
				ExpressionList expressionList = insert.getItemsList(ExpressionList.class);
				Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());

				if (!expressionList.getExpressions().isEmpty()) {
					// 多行插入 行构造器解析
					if (expressionList.getExpressions().get(0) instanceof RowConstructor) {
						expressionList.getExpressions().forEach((expression -> {
							if (expression instanceof RowConstructor) {
								RowConstructor rowConstructor = (RowConstructor) expression;
								rowConstructor.getExprList().getExpressions().add(new StringValue(getCurrentUser()));
								rowConstructor.getExprList().getExpressions()
										.add(new TimestampValue().withValue(currentTimeStamp));
							}
						}));
					} else {
						// 其余默认单行插入
						expressionList.addExpressions(new StringValue(getCurrentUser()),
								new TimestampValue().withValue(currentTimeStamp));
					}
				}
				return insert.toString();
			}
			if (SqlCommandType.UPDATE == mappedStatement.getSqlCommandType()) {
				if (statement instanceof Update) {
					Update update = (Update) statement;
					List<UpdateSet> updateSetList = update.getUpdateSets();
					UpdateSet updateBy =
							new UpdateSet(new Column(UPDATE_USER_COLUMN), new StringValue(getCurrentUser()));
					Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
					UpdateSet updateTime = new UpdateSet(new Column(UPDATE_TIME_COLUMN),
							new TimestampValue().withValue(currentTimeStamp));
					updateSetList.add(updateBy);
					updateSetList.add(updateTime);
					return update.toString();
				}
			}
			if (SqlCommandType.SELECT == mappedStatement.getSqlCommandType()) {
				if (statement instanceof Select) {
					Select select = (Select) statement;
					SelectBody selectBody = select.getSelectBody();
					if (selectBody instanceof PlainSelect) {
						PlainSelect plainSelect = (PlainSelect) selectBody;
						TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
						List<String> tableNames = tablesNamesFinder.getTableList(select);

						List<SelectItem> selectItems = plainSelect.getSelectItems();
						tableNames.forEach((tableName) -> {
							String lowerCaseTableName = tableName.toLowerCase();
							selectItems.add(new SelectExpressionItem()
									.withExpression(new Column(new Table(tableName), CREATE_USER_COLUMN))
									.withAlias(new Alias(lowerCaseTableName + "_" + CREATE_USER_COLUMN)));
							selectItems.add(new SelectExpressionItem()
									.withExpression(new Column(new Table(tableName), CREATE_TIME_COLUMN))
									.withAlias(new Alias(lowerCaseTableName + "_" + CREATE_TIME_COLUMN)));
							selectItems.add(new SelectExpressionItem()
									.withExpression(new Column(new Table(tableName), UPDATE_USER_COLUMN))
									.withAlias(new Alias(lowerCaseTableName + "_" + UPDATE_USER_COLUMN)));
							selectItems.add(new SelectExpressionItem()
									.withExpression(new Column(new Table(tableName), UPDATE_TIME_COLUMN))
									.withAlias(new Alias(lowerCaseTableName + "_" + UPDATE_TIME_COLUMN)));
						});
						return select.toString();
					}
				}
			}
			return originalSql;
		}
	}

	private static String getCurrentUser() {
		return 0L + "";
	}
}