package com.wy.mybatis;

/**
 * 多数据源配置:<br>
 * 1.多数据源模式下,在application配置文件中的大部分全局配置无效
 * 2.多数据源需要单独的配置文件,每一个sqlsessionfactorybean都需要一个单独数据源
 * 3.该模式下,map-underscore-to-camel-case:true无效,mybatis不会自动将map中的下划线转驼峰
 * 若仍然需要该特性,查询{@link MybatisUnderscore2CamelCaseConfig}
 * 
 * @auther 飞花梦影
 * @date 2021-07-24 17:52:32
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MybatisMultiConfig {

}
