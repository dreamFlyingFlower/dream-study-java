package com.wy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

/**
 * MyBatis-plus配置
 * 
 * @author 飞花梦影
 * @date 2021-04-08 16:16:38
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class MyBatisPlusConfig {

	/**
	 * MyBatis-plus分页插件
	 * 
	 * @return 分页拦截器
	 */
	@Bean
	public MybatisPlusInterceptor paginationInnerInterceptor() {
		// 分页插件
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		// 添加租户解析器,所有表的增删改查都会添加特定的租户
		// addTenantId(interceptor);
		// FIXME 动态表名解析器,不同版本不同
		return interceptor;
	}

	public void addTenantId(MybatisPlusInterceptor interceptor) {
		// 租户拦截器
		TenantLineHandler tenantLineHandler = new TenantLineHandler() {

			/**
			 * 字段值处理
			 * 
			 * @return
			 */
			@Override
			public Expression getTenantId() {
				// tenantId段值处理
				return new LongValue(0L);
			}

			/**
			 * 字段处理,默认字段为tenant_id
			 * 
			 * @return
			 */
			@Override
			public String getTenantIdColumn() {
				// TODO Auto-generated method stub
				return TenantLineHandler.super.getTenantIdColumn();
			}

			/**
			 * 是否忽略某些表
			 * 
			 * @param tableName
			 * @return
			 */
			@Override
			public boolean ignoreTable(String tableName) {
				return TenantLineHandler.super.ignoreTable(tableName);
			}
		};
		// 添加租户拦截器
		interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(tenantLineHandler));
	}

	/**
	 * 乐观锁插件,需要配合在指定字段上添加 {@link Version}
	 */
	@Bean
	public OptimisticLockerInnerInterceptor optimisticLockerInterceptor() {
		return new OptimisticLockerInnerInterceptor();
	}
}