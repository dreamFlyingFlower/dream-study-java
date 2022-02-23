package com.wy.jdk12.example;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * JMH基准测试
 * 
 * <pre>
 * {@link Mode#Throughput}:吞吐量,一段时间内可执行的次数,每秒可执行次数
 * {@link Mode#AverageTime}:每次调用的平均耗时时间
 * {@link Mode#SampleTime}:随机进行采样执行的时间,最后输出取样结果的分布
 * {@link Mode#SingleShotTime}:在没执行中计算耗时
 * 以上模式都是默认一次,iteration是1s,只有SingleShotTime是只运行一次
 * 
 * {@link Scope#Thread}:默认的State,每个测试线程分配一个实例
 * {@link Scope#Benchmark}:所有测试线程共享一个实例,用于测试有状态实例在多线程共享下的性能
 * {@link Scope#Group}:每个线程组共享一个实例
 * 
 * {@link OutputTimeUnit}:Benchmark结果所使用的时间单位
 * {@link Benchmark}:表示方法需要进行benchmark测试
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2022-02-23 22:53:59
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class Jmh {

	@Benchmark
	public int test() {
		return 1;
	}

	public static void main(String[] args) {
		Options options = new OptionsBuilder()
				// 需要测试的类
				.include(Jmh.class.getSimpleName())
				// 需要测试几轮
				.forks(1)
				// 预热测试次数
				.warmupIterations(5)
				// 正式测试次数
				.measurementIterations(5).build();
		try {
			new Runner(options).run();
		} catch (RunnerException e) {
			e.printStackTrace();
		}
	}
}