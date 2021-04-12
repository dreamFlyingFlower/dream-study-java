package com.wy.qq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 帧单元,一帧就是一屏画面,包含多个frameUnit
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FrameUnit {

	private long timestamp;

	private int count;

	private int index;

	private int length;

	private byte[] unitData;

}