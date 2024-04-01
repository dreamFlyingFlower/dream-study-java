package com.wy.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.dream.result.Result;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wy.model.User;

import lombok.extern.slf4j.Slf4j;

/**
 * EasyExcel导入导出
 *
 * @author 飞花梦影
 * @date 2024-04-01 13:56:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@EnableAsync
@Controller
@RequestMapping("excel")
@Slf4j
public class EasyExcelController {

	/**
	 * 自定义线程池
	 */
	@Autowired
	private ThreadPoolExecutor threadPoolExecutor;

	@GetMapping("/export/user")
	public void exportUserExcel(HttpServletResponse response) {
		try {
			this.setExcelResponseProp(response, "用户列表");
			List<UserExcelDTO> userList = this.getUserList();
			EasyExcel.write(response.getOutputStream()).head(UserExcelDTO.class).excelType(ExcelTypeEnum.XLSX)
					.sheet("用户列表").doWrite(userList);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@GetMapping("/export/userAsync")
	@Async
	public <T> void handlerAsync(HttpServletResponse response, List<T> lists) {
		// 测试每1000条数据插入开一个线程
		int maxCount = new Double(Math
				.ceil(new BigDecimal(lists.size()).divide(new BigDecimal(1000), RoundingMode.HALF_UP).doubleValue()))
						.intValue();
		CountDownLatch countDownLatch = new CountDownLatch(maxCount);
		for (int i = 0; i < maxCount; i++) {
			List<T> subList = lists.subList(i, (i + 1) * 1000);
			threadPoolExecutor.execute(() -> {
				exportUserExcelAsync(response, subList);
			});
			countDownLatch.countDown();
		}
		try {
			countDownLatch.await();
		} catch (Exception e) {
			log.error("阻塞异常:" + e.getMessage());
		}
	}

	private <T> void exportUserExcelAsync(HttpServletResponse response, List<T> userList) {
		try {
			EasyExcel.write(response.getOutputStream()).head(UserExcelDTO.class).excelType(ExcelTypeEnum.XLSX)
					.sheet("用户列表").doWrite(userList);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 设置响应结果
	 *
	 * @param response 响应结果对象
	 * @param rawFileName 文件名
	 * @throws UnsupportedEncodingException 不支持编码异常
	 */
	private void setExcelResponseProp(HttpServletResponse response, String rawFileName)
			throws UnsupportedEncodingException {
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setCharacterEncoding("utf-8");
		String fileName = URLEncoder.encode(rawFileName, "UTF-8").replaceAll("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
	}

	/**
	 * 导出合并对象
	 * 
	 * @param response
	 */
	@GetMapping("/export/order")
	public void exportOrderExcel(HttpServletResponse response) {
		try {
			this.setExcelResponseProp(response, "订单列表");
			List<OrderExcelDTO> exportData = this.getOrderList();
			EasyExcel.write(response.getOutputStream()).head(OrderExcelDTO.class)
					.registerWriteHandler(new EasyExcelMergeStrategy(OrderExcelDTO.class)).excelType(ExcelTypeEnum.XLSX)
					.sheet("订单列表").doWrite(exportData);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 读取用户列表数据
	 *
	 * @return 用户列表数据
	 * @throws IOException IO异常
	 */
	private List<UserExcelDTO> getUserList() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ClassPathResource classPathResource = new ClassPathResource("mock/users.json");
		InputStream inputStream = classPathResource.getInputStream();
		return objectMapper.readValue(inputStream, new TypeReference<List<UserExcelDTO>>() {
		});
	}

	/**
	 * 读取用户列表数据
	 *
	 * @return 用户列表数据
	 * @throws IOException IO异常
	 */
	private List<OrderExcelDTO> getOrderList() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ClassPathResource classPathResource = new ClassPathResource("mock/orders.json");
		InputStream inputStream = classPathResource.getInputStream();
		return objectMapper.readValue(inputStream, new TypeReference<List<OrderExcelDTO>>() {
		});
	}

	/**
	 * Excel导入
	 * 
	 * @param file 文件
	 * @return
	 */
	@PostMapping("/import/user")
	public Result<?> importUserExcel(@RequestPart(value = "file") MultipartFile file) {
		try {
			List<User> userList = EasyExcel.read(file.getInputStream()).head(User.class).sheet().doReadSync();
			return Result.ok(userList);
		} catch (IOException e) {
			return Result.error();
		}
	}
}