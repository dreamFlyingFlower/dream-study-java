package dream.study.common.config;

import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * 国际化初始化{@link MessageSourceAutoConfiguration}
 * 
 * 在Spring中使用需要注入{@link MessageSource},默认实现为{@link ResourceBundleMessageSource}
 * 
 * 在application配置文件中注入spring.messages.beanname:i18n/message/messages
 * 值是国际化文件的地址,前2个可自定义,是文件夹路径,最后一个表示国际化文件的前缀,
 * messages可自定义,不带任何下划线后缀的是默认文件,即找不到本地化配置文件时使用,
 * 带后缀的第一个指语言的缩写,第二个是国家的缩写,可查看{@link java.util.Locale}
 * 
 * 国家化简写:
 * 
 * <pre>
 *	中国 : zh_CN
 * 秘鲁 : es_PE
 * 巴拿马 : es_PA
 * 波斯尼亚和黑山共和国 : sr_BA
 * 危地马拉 : es_GT
 * 阿拉伯联合酋长国 : ar_AE
 * 挪威 : no_NO
 * 阿尔巴尼亚 : sq_AL
 * 伊拉克 : ar_IQ
 * 也门 : ar_YE
 * 葡萄牙 : pt_PT
 * 塞浦路斯 : el_CY
 * 卡塔尔 : ar_QA
 * 马其顿王国 : mk_MK
 * 瑞士 : de_CH
 * 美国 : en_US
 * 芬兰 : fi_FI
 * 马耳他 : en_MT
 * 斯洛文尼亚 : sl_SI
 * 斯洛伐克 : sk_SK
 * 土耳其 : tr_TR
 * 沙特阿拉伯 : ar_SA
 * 英国 : en_GB
 * 塞尔维亚及黑山 : sr_CS
 * 新西兰 : en_NZ
 * 挪威 : no_NO
 * 立陶宛 : lt_LT
 * 尼加拉瓜 : es_NI
 * 爱尔兰 : ga_IE
 * 比利时 : fr_BE
 * 西班牙 : es_ES
 * 黎巴嫩 : ar_LB
 * 加拿大 : fr_CA
 * 爱沙尼亚 : et_EE
 * 科威特 : ar_KW
 * 塞尔维亚 : sr_RS
 * 美国 : es_US
 * 墨西哥 : es_MX
 * 苏丹 : ar_SD
 * 印度尼西亚 : in_ID
 * 乌拉圭 : es_UY
 * 拉脱维亚 : lv_LV
 * 巴西 : pt_BR
 * 叙利亚 : ar_SY
 * 多米尼加共和国 : es_DO
 * 瑞士 : fr_CH
 * 印度 : hi_IN
 * 委内瑞拉 : es_VE
 * 巴林 : ar_BH
 * 菲律宾 : en_PH
 * 突尼斯 : ar_TN
 * 奥地利 : de_AT
 * 荷兰 : nl_NL
 * 厄瓜多尔 : es_EC
 * 台湾地区 : zh_TW
 * 约旦 : ar_JO
 * 冰岛 : is_IS
 * 哥伦比亚 : es_CO
 * 哥斯达黎加 : es_CR
 * 智利 : es_CL
 * 埃及 : ar_EG
 * 南非 : en_ZA
 * 泰国 : th_TH
 * 希腊 : el_GR
 * 意大利 : it_IT
 * 匈牙利 : hu_HU
 *	爱尔兰 : en_IE
 *	乌克兰 : uk_UA
 *	波兰 : pl_PL
 *	卢森堡 : fr_LU
 *	比利时 : nl_BE
 *	印度 : en_IN
 *	西班牙 : ca_ES
 *	摩洛哥 : ar_MA
 *	玻利维亚 : es_BO
 *	澳大利亚 : en_AU
 *	新加坡 : zh_SG
 *	萨尔瓦多 : es_SV
 *	俄罗斯 : ru_RU
 *	韩国 : ko_KR
 *	阿尔及利亚 : ar_DZ
 *	越南 : vi_VN
 *	黑山 : sr_ME
 *	利比亚 : ar_LY
 *	台湾：zh_TW
 *	香港 : zh_HK
 *	白俄罗斯 : be_BY
 *	以色列 : iw_IL
 *	保加利亚 : bg_BG
 *	马耳他 : mt_MT
 *	巴拉圭 : es_PY
 *	法国 : fr_FR
 *	捷克共和国 : cs_CZ
 *	瑞士 : it_CH
 *	罗马尼亚 : ro_RO
 *	波多黎哥 : es_PR
 *	加拿大 : en_CA
 *	德国 : de_DE
 *	卢森堡 : de_LU
 *	阿根廷 : es_AR
 *	马来西亚 : ms_MY
 *	克罗地亚 : hr_HR
 *	新加坡 : en_SG
 *	阿曼 : ar_OM
 *	泰国 : th_TH
 *	瑞典 : sv_SE
 *	丹麦 : da_DK
 *	洪都拉斯 : es_HN
 * 日本 : ja_JP
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2022-05-18 10:10:39
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class I18NConfig {

}