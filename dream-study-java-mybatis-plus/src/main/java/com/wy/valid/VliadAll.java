package com.wy.valid;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

/**
 * 实体类参数校验,检验所有组别
 * @author ParadiseWY
 * @date 2019年8月13日
 */
@GroupSequence({ ValidInsert.class, ValidEdit.class, Default.class })
public interface VliadAll {

}