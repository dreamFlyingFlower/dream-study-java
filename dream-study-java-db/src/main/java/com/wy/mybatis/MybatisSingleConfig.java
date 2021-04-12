package com.wy.mybatis;

import org.mybatis.spring.annotation.MapperScan;

/**
 * 单数据源配置<br>
 * 1.不配置@MapperScan和mybatis.mapper-locations,则myabtis将默认扫描的包同@SpringBootApplication,只扫描接口
 * 而xml文件则默认扫描classpath下的mapper文件夹
 * 2.配置@MapperScan的value或basePackages,两者效果一样,将会扫描指定的包中的接口<br>
 * 若不指定这2个值,则将扫描当前包以及子包中的接口<br>
 * 3.但数据源下,@MapperScan最好直接写在启动类中,且最好配置value,减少启动扫描消耗的资源
 * 
 * @author ParadiseWY
 * @date 2020-8-14 14:59:52
 */
@MapperScan
public class MybatisSingleConfig {

}