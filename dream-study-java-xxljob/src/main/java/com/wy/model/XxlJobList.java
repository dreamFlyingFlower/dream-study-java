package com.wy.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * xxljob分页结果
 * 
 * @auther 飞花梦影
 * @date 2022-01-08 01:33:55
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "xxljob分页结果")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XxlJobList<T> {

	private Integer recordsFiltered;

	private List<T> data;

	private Integer recordsTotal;
}