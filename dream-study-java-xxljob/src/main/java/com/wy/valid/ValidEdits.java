package com.wy.valid;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

/**
 * @description 在进行通用增删改查的时候,校验修改参数的标识
 * @author ParadiseWY
 * @date 2019年7月31日 上午9:38:35
 */
@GroupSequence({ ValidEdit.class, Default.class })
public interface ValidEdits {

}