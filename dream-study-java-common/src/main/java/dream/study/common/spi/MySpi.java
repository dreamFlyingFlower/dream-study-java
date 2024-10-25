package dream.study.common.spi;

/**
 * SPI,Service Provider Interface,SpringBoot的装配就和该功能类似
 * 
 * 需要在resources目录下新建MATE-INF/services目录,并将需要执行的接口做为文件名新建文件,文件中填写该接口的实现类
 *
 * @author 飞花梦影
 * @date 2024-04-26 16:19:15
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface MySpi {

	String getName();

	void handle();
}