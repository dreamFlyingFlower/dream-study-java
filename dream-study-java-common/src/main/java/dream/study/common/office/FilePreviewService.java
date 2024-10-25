package dream.study.common.office;

import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import dream.flying.flower.lang.StrHelper;

/**
 * service层在线预览方法代码
 *
 * @author 飞花梦影
 * @date 2024-04-22 15:27:12
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class FilePreviewService {

	/**
	 * 系统文件在线预览接口
	 */
	public void onlinePreview(String url, HttpServletResponse response) throws Exception {
		// 获取文件类型
		String[] str = StrHelper.split(url, "\\.");

		if (str.length == 0) {
			throw new Exception("文件格式不正确");
		}
		String suffix = str[str.length - 1];
		if (!suffix.equals("txt") && !suffix.equals("doc") && !suffix.equals("docx") && !suffix.equals("xls")
				&& !suffix.equals("xlsx") && !suffix.equals("ppt") && !suffix.equals("pptx")) {
			throw new Exception("文件格式不支持预览");
		}
		InputStream in = FileConvertUtil.convertNetFile(url, suffix);
		OutputStream outputStream = response.getOutputStream();
		// 创建存放文件内容的数组
		byte[] buff = new byte[1024];
		// 所读取的内容使用n来接收
		int n;
		// 当没有读取完时,继续读取,循环
		while ((n = in.read(buff)) != -1) {
			// 将字节数组的数据全部写入到输出流中
			outputStream.write(buff, 0, n);
		}
		// 强制将缓存区的数据进行输出
		outputStream.flush();
		// 关流
		outputStream.close();
		in.close();
	}
}