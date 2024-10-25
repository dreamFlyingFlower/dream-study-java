package dream.study.spring.valid;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

import dream.flying.flower.framework.web.valid.ValidAdd;
import dream.flying.flower.framework.web.valid.ValidEdit;

/**
 * 实体类参数校验,检验所有组别
 * 
 * @author 飞花梦影
 * @date 2022-05-15 15:53:24
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@GroupSequence({ ValidAdd.class, ValidEdit.class, Default.class })
public interface VliadAll {

}