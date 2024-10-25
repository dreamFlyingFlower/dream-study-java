package dream.study.authorization.server.query;

import java.util.Date;

import dream.flying.flower.framework.web.query.AbstractQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 用户信息查询
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户信息查询")
public class UserQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	@Schema(description = "登录名")
	private String username;

	@Schema(description = "昵称")
	private String nickName;

	@Schema(description = "显示名称")
	private String displayName;

	@Schema(description = "密码")
	private String password;

	@Schema(description = "DE密码")
	private String decipherable;

	@Schema(description = "手机号码")
	private String mobile;

	@Schema(description = "手机号验证")
	private String mobileVerified;

	@Schema(description = "邮箱")
	private String email;

	@Schema(description = "邮箱验证")
	private Integer emailVerified;

	@Schema(description = "生日")
	private Date birthDate;

	@Schema(description = "性别")
	private Integer gender;

	@Schema(description = "是否已婚")
	private Integer married;

	@Schema(description = "密码问题")
	private String passwordQuestion;

	@Schema(description = "密码答案")
	private String passwordAnswer;

	@Schema(description = "认证类型")
	private Integer authnType;

	@Schema(description = "应用登录认证类型")
	private Integer appLoginAuthnType;

	@Schema(description = "应用登录密码")
	private String appLoginPassword;

	@Schema(description = "应用登录密码保护应用")
	private String protectedApps;

	@Schema(description = "IM账号")
	private String defineIm;

	@Schema(description = "LDAP信息")
	private String ldapDn;

	@Schema(description = "AD域账号")
	private String windowsAccount;

	@Schema(description = "应用列表类型")
	private Integer gridList;

	@Schema(description = "名")
	private String givenName;

	@Schema(description = "中间名")
	private String middleName;

	@Schema(description = "姓")
	private String familyName;

	@Schema(description = "名字中文拼音")
	private String nameZhSpell;

	@Schema(description = "名字中文拼音简称")
	private String nameZhShortSpell;

	@Schema(description = "用户全名")
	private String formattedName;

	@Schema(description = "前缀")
	private String honorificPrefix;

	@Schema(description = "后缀")
	private String honorificSuffix;

	@Schema(description = "是否锁定")
	private Integer isLocked;

	@Schema(description = "锁定时间")
	private Date unlockTime;

	@Schema(description = "登录次数统计")
	private Integer loginCount;

	@Schema(description = "最近登录IP地址")
	private String lastLoginIp;

	@Schema(description = "最近登录时间")
	private Date lastLoginTime;

	@Schema(description = "最近注销时间")
	private Date lastLogoffTime;

	@Schema(description = "最近密码错误时间")
	private Date badPasswordTime;

	@Schema(description = "密码错误次数")
	private Integer badPasswordCount;

	@Schema(description = "最近密码修改时间")
	private Date passwordLastSetTime;

	@Schema(description = "密码重置类型")
	private Integer passwordSetType;

	@Schema(description = "历史密码")
	private String passwordHistory;

	@Schema(description = "COUNTER-OPT密钥")
	private String sharedCounter;

	@Schema(description = "TIME-OPT密钥")
	private String sharedSecret;

	@Schema(description = "学历")
	private String education;

	@Schema(description = "毕业院校")
	private String graduateFrom;

	@Schema(description = "毕业日期")
	private Date graduateDate;

	@Schema(description = "证件类型")
	private Integer idType;

	@Schema(description = "证件号码")
	private String idCardNo;

	@Schema(description = "用户类型")
	private String userType;

	@Schema(description = "状态")
	private String userStatus;

	@Schema(description = "地址")
	private String locale;

	@Schema(description = "分支")
	private String division;

	@Schema(description = "成本中心")
	private String costCenter;

	@Schema(description = "时区")
	private String timeZone;

	@Schema(description = "家庭-邮件")
	private String homeEmail;

	@Schema(description = "家庭-电话")
	private String homePhoneNumber;

	@Schema(description = "家庭-省/市")
	private String homeCountry;

	@Schema(description = "家庭-市")
	private String homeRegion;

	@Schema(description = "家庭-区")
	private String homeLocality;

	@Schema(description = "家庭-街道")
	private String homeStreetAddress;

	@Schema(description = "家庭-地址全称")
	private String homeAddressFormatted;

	@Schema(description = "家庭-邮编")
	private String homePostalCode;

	@Schema(description = "家庭-传真")
	private String homeFax;

	@Schema(description = "历史区域")
	private String regionHistory;

	@Schema(description = "机构ID")
	private String instId;
}