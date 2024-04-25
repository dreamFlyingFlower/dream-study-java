package com.wy.easypoi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSONObject;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-25 14:07:13
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RequestMapping("easypoi")
@RestController
public class EasyPoiController {

	@PostMapping("/exportExcel")
	public void export(HttpServletResponse response) throws Exception {
		// mock datas
		Goods goods1 = new Goods();
		List<GoodType> goodTypeList1 = new ArrayList<>();
		GoodType goodType1 = new GoodType();
		goodType1.setTypeId("apple-1");
		goodType1.setTypeName("apple-red");
		goodTypeList1.add(goodType1);
		GoodType goodType2 = new GoodType();
		goodType2.setTypeId("apple-2");
		goodType2.setTypeName("apple-white");
		goodTypeList1.add(goodType2);
		goods1.setNo(110);
		goods1.setName("apple");
		goods1.setShelfLife(new Date());
		goods1.setGoodTypes(goodTypeList1);

		Goods goods2 = new Goods();
		List<GoodType> goodTypeList2 = new ArrayList<>();
		GoodType goodType21 = new GoodType();
		goodType21.setTypeId("wine-1");
		goodType21.setTypeName("wine-red");
		goodTypeList2.add(goodType21);
		GoodType goodType22 = new GoodType();
		goodType22.setTypeId("wine-2");
		goodType22.setTypeName("wine-white");
		goodTypeList2.add(goodType22);
		goods2.setNo(111);
		goods2.setName("wine");
		goods2.setShelfLife(new Date());
		goods2.setGoodTypes(goodTypeList2);

		List<Goods> goodsList = new ArrayList<Goods>();
		goodsList.add(goods1);
		goodsList.add(goods2);

		for (Goods goods : goodsList) {
			System.out.println(goods);
		}
		// export
		EasyPoiHelpers.exportExcel(goodsList, Goods.class, "product.xls", response);
	}

	@PostMapping("/importExcel")
	public void importExcel() throws Exception {
		// loal file
		String filePath = "C:\\Users\\Dell\\Downloads\\product.xls";
		// anaysis excel
		List<Goods> goodsList = EasyPoiHelpers.importExcel(filePath, 0, 1, Goods.class);
		// also use MultipartFile,invoke FileUtil.importExcel(MultipartFile file, Integer titleRows, Integer headerRows,
		// Class<T> pojoClass)
		System.out.println("load data count【" + goodsList.size() + "】row");

		// TODO save datas
		for (Goods goods : goodsList) {
			JSONObject.toJSONString(goods);
		}
	}
}