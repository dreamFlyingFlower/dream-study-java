package com.wy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MyBatis运行流程:
 * 
 * @apiNote 1.获取SqlSessionFactory对象:<br>
 *          {@link org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration#sqlSessionFactory()}
 *          ->{@link org.mybatis.spring.SqlSessionFactoryBean#buildSqlSessionFactory()}
 *          ->{@link org.apache.ibatis.session.defaults.DefaultSqlSessionFactory},
 *          解析配置文件以及全局配置文件等放到Configuration中,最终返回DefaultSqlSessionFactory
 * @apiNote 2.获取SqlSession对象:<br>
 *          {@link org.apache.ibatis.session.defaults.DefaultSqlSessionFactory#openSession()}
 *          ->{@see org.apache.ibatis.session.defaults.DefaultSqlSession},该对象包含Executor和Configuration
 * @apiNote 3.获取接口的代理对象:{@link org.apache.ibatis.session.SqlSession#getMapper()}
 *          ->{@link org.apache.ibatis.binding.MapperProxyFactory#newInstance(org.apache.ibatis.session.SqlSession)}
 *          ->{@link org.apache.ibatis.binding.MapperProxy#invoke(Object, java.lang.reflect.Method, Object[])},
 *          该对象中包含了各种Mapper接口,DefaultSqlSession和Executor
 * @apiNote 4.执行增删改查:通过MapperProxy#invoke()执行各种代理方法:<br>
 *          调用DefaultSqlSession的增删改查(Executor)->创建StatementHandler,ParameterHandler和ResultSetHandler
 *          ->调用StatementHandler预编译参数以及设置参数值->使用ParameterHandler来给sql设置参数
 *          ->调用StatementHandler的增删改查方法->ResultSetHandler封装结果
 * 
 * @apiNote 以下对象创建前,都有interceptorChain.pluginAll()可以拦截,要实现{@link org.apache.ibatis.plugin.Interceptor}:
 *          Executor(update,query,flushStatements,commit,rollback,getTransaction,close,isClosed)
 *          ParameterHandler(getParameterObject,setParameters)
 *          ResultSetHandler(handleResultSets,handleOutputParameters)
 *          StatementHandler(prepare,paraeterize,batch,update,query)
 * 
 * @apiNote 实现了Interceptor接口的类需要在全局配置文件中进行注册(mybatis或applicationyml),
 *          若需要提供额外的参数,可以在实现类中重写setProperties方法,详见{@see com.wy.mybatis.CustomPlugin}
 * 
 * @apiNote 数据库在批量插入时都会有大小限制,此时需要将SqlSessionFactory中的ExecutorType改为BATCH,
 *          默认情况下,该参数是为SIMPLE,详见{@link org.apache.ibatis.session.Configuration#getDefaultExecutorType()}.
 *          但是当直接使用Mapper来调用方法时,该参数为全局配置,若直接配置为BATCH,则所有方法都是该类型,
 *          只有在使用SqlSessionFactory时进行设置单个提交才合适,故批量新增最好是单独提取出来
 * 
 * @apiNote 各种类型处理器,特别是枚举类型,最上层接口{@link org.apache.ibatis.type.TypeHandler},
 *          枚举类型的处理器,默认使用枚举的name属性{@link org.apache.ibatis.type.EnumTypeHandler},
 *          也可以修改使用枚举的ordinal{@link org.apache.ibatis.type.EnumOrdinalTypeHandler},
 *          或者自定义,需要实现{@link org.apache.ibatis.type.BaseTypeHandler}或{@link org.apache.ibatis.type.TypeHandler},
 *          需要在配置文件中进行配置,若只对某个枚举进行配置,需要在全局配置的xml中设置,此处为mybatis.xml;
 *          若针对所有的枚举,可以在application中进行统一配置
 * 
 * @author ParadiseWY
 * @date 2020年9月25日 下午11:11:18
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}