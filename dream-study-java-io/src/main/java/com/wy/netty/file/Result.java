package com.wy.netty.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 文件处理结果
 * 
 * @author 飞花梦影
 * @date 2021-09-03 11:30:25
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {

	private boolean code;

	private String msg;

	private String action;

	private String filePath;
}