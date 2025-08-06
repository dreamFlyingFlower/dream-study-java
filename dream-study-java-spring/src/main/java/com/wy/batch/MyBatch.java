package com.wy.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;

/**
 * SpringBoot批处理
 * 
 * <pre>
 * {@link Job}:批处理作业的顶级容器,存在于整个批处理过程
 * {@link Step}:作业的独立执行单元,只在Job内部阶段有效
 * {@link ItemReader}:数据读取接口,包括文件/DB/JMS.从每个Chunk开始存在
 * {@link ItemProcessor}:业务处理逻辑.读取后,写入前存在
 * {@link ItemWriter}:数据写出接口.Chunk结束时存在
 * {@link JobRepository}:存储执行元数据(状态/参数/异常).整个执行过程存在
 * </pre>
 *
 * @author 飞花梦影
 * @date 2025-08-06 13:38:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyBatch {

}