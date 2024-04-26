package com.wy;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.DatagramChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Java中的输入/输出:以程序为中心,向程序是数据源的起点,那就是输出(写);若程序是接收数据的,那就是输入(读)
 * 
 * NIO:同步非阻塞流,适用于高并发,短连接的架构,如聊天室.主要类,, NIO在read/write的时候是同步的,要等待操作完成,但和操作系统交互是异步的.
 * NIO在while读取客户端的流时不会阻塞,BIO会阻塞,多线程情况下,NIO比BIO性能更出色,消耗资源更小,且更稳定 在读取流中的数据时,将不会一直等待,而是通过轮询来判断其他线程是否也需要进行读写操作.
 * 
 * NIO主要是通过{@link Channels},{@link Buffer}以及Selector(多路复用选择器)对socket,io流等进行操作,
 * channel是线程安全的,读写都是用Buffer,而Buffer分为读和写模式,中间通过flip进行切换
 * 
 * {@link Buffer}:缓冲,{@link ByteBuffer}是其主要实现类,大部分的操作都是通过ByteBuffer完成,还有其他基本类型Buffer
 * 
 * <pre>
 * capacity: 容量,buffer的最大长度
 * limit:buffer能写入数据的最大长度,必须小于等于capacity
 * position:无参方法读取buffer指针下标,当前下标是还没有读的,要小于等于limit.有参方法指定当前buffer索引
 * mark:记号下标,方便重新读取buffer中的数据,要小于等于position
 * flip():设置limit为position值,再将position设为0,以备用于读取,各种读写操作都是在postion和limit之间进行
 * rewind():将limit和position恢复到读取数据之前,重复读取缓冲区数据
 * clear():重置buffer到初始状态,用于下次写入新的数据,但实际上次的数据仍在缓冲区中,重复写入会覆盖以前的数据
 * allocate():在jvm的堆中分配内存创建缓冲区,并给缓冲区分配大小
 * allocateDirect():在os内核中分配内存创建缓冲区,不占用jvm的内存空间,速度更快,默认是64M,但不可回收
 * array():将缓冲区数据转为字节数组
 * </pre>
 * 
 * Channel:通道,主要是为了进行Selector的切换,主要可分为以下四种:
 * 
 * <pre>
 * {@link FileChannel}:从文件中读取数据
 * {@link DatagramChannel}:从UDP中读写网络数据
 * {@link SocketChannel}:从TCP中读写网络数据,用在Socket客户端
 * {@link ServerSocketChannel}:监听新连接的TCP请求,对每一个新的连接新建一个SocketChannel,用在Socket服务端
 * </pre>
 * 
 * 网络七层模型:
 * 
 * <pre>
 * 物理层:中继器,集线器,双绞线等网络传输设备
 * 数据链路层:网桥,以太网交换机,网卡等物理网络连接设备
 * 网络层:路由器,三层交换机等网络设备
 * 传输层:四层交换机,四层路由器等设备
 * 会话层:和表示层,应用层都可以统称为应用层.
 * 表示层:数据展示
 * 应用层:数据展示
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-09-29 10:47:14
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}