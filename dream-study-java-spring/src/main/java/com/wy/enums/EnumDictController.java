package com.wy.enums;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dream.flying.flower.result.Result;
import io.swagger.annotations.Api;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-05-22 17:39:38
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "通用字典接口")
@RestController
@RequestMapping("/enumDict")
public class EnumDictController {

	@Autowired
	private CommonEnumRegistry commonEnumRegistry;

	@GetMapping("all")
	public Result<Map<String, List<CommonEnumVO>>> allEnums() {
		Map<String, List<CommonEnum>> dict = this.commonEnumRegistry.getNameDict();
		Map<String, List<CommonEnumVO>> dictVo = Maps.newHashMapWithExpectedSize(dict.size());
		for (Map.Entry<String, List<CommonEnum>> entry : dict.entrySet()) {
			dictVo.put(entry.getKey(), CommonEnumVO.from(entry.getValue()));
		}
		return Result.ok(dictVo);
	}

	@GetMapping("types")
	public Result<List<String>> enumTypes() {
		Map<String, List<CommonEnum>> dict = this.commonEnumRegistry.getNameDict();
		return Result.ok(Lists.newArrayList(dict.keySet()));
	}

	@GetMapping("/{type}")
	public Result<List<CommonEnumVO>> dictByType(@PathVariable("type") String type) {
		Map<String, List<CommonEnum>> dict = this.commonEnumRegistry.getNameDict();
		List<CommonEnum> commonEnums = dict.get(type);
		return Result.ok(CommonEnumVO.from(commonEnums));
	}
}