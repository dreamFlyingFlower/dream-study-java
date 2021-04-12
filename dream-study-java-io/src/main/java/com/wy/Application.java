package com.wy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * NIO:同步非阻塞流,适用于高并发,短连接的架构,如聊天室
 * 主要类和接口{@link java.nio.Buffer},{@link java.nio.ByteBuffer},{@link java.nio.channels.Channels}
 * 
 * Java中的输入/输出:以程序为中心,向程序是数据源的起点,那就是输出(写);若程序是接收数据的,那就是输入(读)
 * 
 * @apiNote NIO在read/write的时候是同步的,要等待操作完成,但和操作系统交互是异步的,
 *          在读取流中的数据时,将不会一直等待,而是通过轮询来查看是否需要使用线程
 *          在多线程情况下,nio性能以及消耗的资源都比传统的bio更好小,且比bio更稳定
 *          nio主要是通过Channel(通道),Buffer,以及Selector(多路复用选择器)来对socket进行操作,channel通常是线程安全的
 *          读写都是用Buffer,而buffer分为读和写模式,中间通过flip进行切换
 * @apiNote Buffer,ByteBuffer:<br>
 *          mark:记号下标,方便重新读取buffer中的数据,要小于等于position<br>
 *          position:读取buffer时指针下标,当前下标是还没有读的,要小于等于limit<br>
 *          limit:buffer能写入数据的最大长度,必须小于等于capacity<br>
 *          capacity: 容量,buffer的最大长度<br>
 *          flip():该方法设置limit为position值,之后将position设置为0,以备用于读取,各种操作都是在postion和limit之间进行
 *          rewind():将limit和position恢复到读取数据之前,重复读取缓冲区数据
 *          clear():和flip不同的是将limit设置为capacity,相当于重置buffer,用于下次写入数据,但实际上次的数据仍在缓冲区中
 *          allocate():在jvm的堆中分配内存创建缓冲区,并给缓冲区分配大小
 *          allocateDirect():在os内核中分配内存创建缓冲区,不占用jvm的内存空间,速度更快,默认是64M,但不可回收
 * @apiNote Channel可分为以下四种:<br>
 *          FileChannel:从文件中读取数据<br>
 *          DatagramChannel:从udp中读写网络中的数据<br>
 *          SocketChannel:从tcp中读写网络数据
 *          ServerSocketChannel:监听新连接的tcp请求,对每一个新的连接新建一个SocketChannel
 * @apiNote bio:NIO在while读取客户端的流时会阻塞,BIO会阻塞,同等线程情况下,nio将比bio更出色,消耗性能更小
 * 
 * @author ParadiseWY
 * @date 2020-09-29 10:47:14
 * @git {@link https://github.com/mygodness100}
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}