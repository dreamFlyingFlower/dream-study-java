package dream.study.authorization.server.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fhs.core.trans.vo.TransPojo;

import dream.flying.flower.ConstDate;
import dream.flying.flower.framework.web.valid.ValidAdd;
import dream.flying.flower.framework.web.valid.ValidEdit;
import dream.flying.flower.helper.ImageHelper;
import dream.flying.flower.lang.StrHelper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 用户信息
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
@Schema(description = "用户信息")
public class UserVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "编号")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "登录名")
	@NotBlank(message = "登录名不能为空", groups = { ValidAdd.class })
	@Size(max = 32, message = "登录名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String username;

	@Schema(description = "昵称")
	@Size(max = 32, message = "昵称最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String nickName;

	@Schema(description = "显示名称")
	@Size(max = 32, message = "显示名称最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String displayName;

	@Schema(description = "密码")
	@NotBlank(message = "密码不能为空", groups = { ValidAdd.class })
	@Size(max = 128, message = "密码最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String password;

	@Schema(description = "DE密码")
	@NotBlank(message = "DE密码不能为空", groups = { ValidAdd.class })
	@Size(max = 500, message = "DE密码最大长度不能超过500", groups = { ValidAdd.class, ValidEdit.class })
	private String decipherable;

	@Schema(description = "手机号码")
	@Size(max = 32, message = "手机号码最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String mobile;

	@Schema(description = "手机号验证")
	private Integer mobileVerified;

	@Schema(description = "邮箱")
	@Size(max = 32, message = "邮箱最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String email;

	@Schema(description = "邮箱验证")
	private Integer emailVerified;

	@Schema(description = "生日")
	@JsonFormat(pattern = ConstDate.DATETIME)
	private Date birthDate;

	@Schema(description = "性别")
	private Integer gender;

	@Schema(description = "是否已婚")
	private Integer married;

	@Schema(description = "密码问题")
	@Size(max = 64, message = "密码问题最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String passwordQuestion;

	@Schema(description = "密码答案")
	@Size(max = 64, message = "密码答案最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String passwordAnswer;

	@Schema(description = "认证类型")
	private Integer authnType;

	@Schema(description = "应用登录认证类型")
	private Integer appLoginAuthnType;

	@Schema(description = "应用登录密码")
	@Size(max = 64, message = "应用登录密码最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String appLoginPassword;

	@Schema(description = "应用登录密码保护应用")
	@Size(max = 450, message = "应用登录密码保护应用最大长度不能超过450", groups = { ValidAdd.class, ValidEdit.class })
	private String protectedApps;

	@Schema(description = "IM账号")
	@Size(max = 32, message = "IM账号最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String defineIm;

	@Schema(description = "LDAP信息")
	@Size(max = 1000, message = "LDAP信息最大长度不能超过1,000", groups = { ValidAdd.class, ValidEdit.class })
	private String ldapDn;

	@Schema(description = "AD域账号")
	@Size(max = 32, message = "AD域账号最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String windowsAccount;

	@Schema(description = "应用列表类型")
	private Integer gridList;

	@Schema(description = "名")
	@Size(max = 32, message = "名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String givenName;

	@Schema(description = "中间名")
	@Size(max = 32, message = "中间名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String middleName;

	@Schema(description = "姓")
	@Size(max = 32, message = "姓最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String familyName;

	@Schema(description = "名字中文拼音")
	@Size(max = 100, message = "名字中文拼音最大长度不能超过100", groups = { ValidAdd.class, ValidEdit.class })
	private String nameZhSpell;

	@Schema(description = "名字中文拼音简称")
	@Size(max = 64, message = "名字中文拼音简称最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String nameZhShortSpell;

	@Schema(description = "用户全名")
	@Size(max = 64, message = "用户全名最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String formattedName;

	@Schema(description = "前缀")
	@Size(max = 32, message = "前缀最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String honorificPrefix;

	@Schema(description = "后缀")
	@Size(max = 32, message = "后缀最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String honorificSuffix;

	@Schema(description = "登录次数统计")
	private Integer loginCount;

	@Schema(description = "最近登录IP地址")
	@Size(max = 32, message = "最近登录IP地址最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String lastLoginIp;

	@Schema(description = "最近登录时间")
	@JsonFormat(pattern = ConstDate.DATETIME)
	private Date lastLoginTime;

	@Schema(description = "最近注销时间")
	@JsonFormat(pattern = ConstDate.DATETIME)
	private Date lastLogoffTime;

	@Schema(description = "最近密码错误时间")
	@JsonFormat(pattern = ConstDate.DATETIME)
	private Date badPasswordTime;

	@Schema(description = "密码错误次数")
	private Integer badPasswordCount;

	@Schema(description = "最近密码修改时间")
	@JsonFormat(pattern = ConstDate.DATETIME)
	private Date passwordLastSetTime;

	@Schema(description = "密码重置类型")
	private Integer passwordSetType;

	@Schema(description = "历史密码")
	@Size(max = 65535, message = "历史密码最大长度不能超过65,535", groups = { ValidAdd.class, ValidEdit.class })
	private String passwordHistory;

	@Schema(description = "COUNTER-OPT密钥")
	@Size(max = 32, message = "COUNTER-OPT密钥最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String sharedCounter;

	@Schema(description = "TIME-OPT密钥")
	@Size(max = 500, message = "TIME-OPT密钥最大长度不能超过500", groups = { ValidAdd.class, ValidEdit.class })
	private String sharedSecret;

	@Schema(description = "学历")
	@Size(max = 64, message = "学历最大长度不能超过64", groups = { ValidAdd.class, ValidEdit.class })
	private String education;

	@Schema(description = "毕业院校")
	@Size(max = 256, message = "毕业院校最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String graduateFrom;

	@Schema(description = "毕业日期")
	@JsonFormat(pattern = ConstDate.DATETIME)
	private Date graduateDate;

	@Schema(description = "证件类型")
	private Integer idType;

	@Schema(description = "证件号码")
	@Size(max = 45, message = "证件号码最大长度不能超过45", groups = { ValidAdd.class, ValidEdit.class })
	private String idCardNo;

	@Schema(description = "用户类型")
	@Size(max = 32, message = "用户类型最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String userType;

	@Schema(description = "状态")
	@Size(max = 32, message = "状态最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String userStatus;

	@Schema(description = "地址")
	@Size(max = 32, message = "地址最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String locale;

	@Schema(description = "分支")
	@Size(max = 32, message = "分支最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String division;

	@Schema(description = "成本中心")
	@Size(max = 32, message = "成本中心最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String costCenter;

	@Schema(description = "时区")
	@Size(max = 32, message = "时区最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String timeZone;

	@Schema(description = "家庭-邮件")
	@Size(max = 32, message = "家庭-邮件最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String homeEmail;

	@Schema(description = "家庭-电话")
	@Size(max = 32, message = "家庭-电话最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String homePhoneNumber;

	@Schema(description = "家庭-省/市")
	@Size(max = 32, message = "家庭-省/市最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String homeCountry;

	@Schema(description = "家庭-市")
	@Size(max = 32, message = "家庭-市最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String homeRegion;

	@Schema(description = "家庭-区")
	@Size(max = 32, message = "家庭-区最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String homeLocality;

	@Schema(description = "家庭-街道")
	@Size(max = 32, message = "家庭-街道最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String homeStreetAddress;

	@Schema(description = "家庭-地址全称")
	@Size(max = 256, message = "家庭-地址全称最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String homeAddressFormatted;

	@Schema(description = "家庭-邮编")
	@Size(max = 32, message = "家庭-邮编最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String homePostalCode;

	@Schema(description = "家庭-传真")
	@Size(max = 32, message = "家庭-传真最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String homeFax;

	@Schema(description = "历史区域")
	@Size(max = 65535, message = "历史区域最大长度不能超过65,535", groups = { ValidAdd.class, ValidEdit.class })
	private String regionHistory;

	/**
	 * 图片
	 */
	@JsonIgnore
	private byte[] picture;

	/**
	 * 语言偏好
	 */
	private String preferredLanguage;

	/**
	 * 主题
	 */
	private String theme;

	/**
	 * 个人主页
	 */
	private String webSite;

	/**
	 * 微信粉丝
	 */
	private Integer weixinFollow;

	@Schema(description = "工号")
	@Size(max = 32, message = "工号最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String employeeNumber;

	@Schema(description = "机构")
	@Size(max = 32, message = "机构最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String organization;

	@Schema(description = "部门编号")
	@Size(max = 32, message = "部门编号最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String departmentId;

	@Schema(description = "部门")
	@Size(max = 32, message = "部门最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String department;

	@Schema(description = "职务")
	@Size(max = 32, message = "职务最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String jobTitle;

	@Schema(description = "工作职级")
	@Size(max = 32, message = "工作职级最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String jobLevel;

	@Schema(description = "经理编号")
	@Size(max = 32, message = "经理编号最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String managerId;

	@Schema(description = "经理名字")
	@Size(max = 32, message = "经理名字最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String manager;

	@Schema(description = "助理编号")
	@Size(max = 32, message = "助理编号最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String assistantId;

	@Schema(description = "助理名字")
	@Size(max = 32, message = "助理名字最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String assistant;

	@Schema(description = "入司时间")
	@JsonFormat(pattern = ConstDate.DATETIME)
	private Date entryDate;

	@Schema(description = "开始工作时间")
	@JsonFormat(pattern = ConstDate.DATETIME)
	private Date startWorkDate;

	@Schema(description = "离职日期")
	@JsonFormat(pattern = ConstDate.DATETIME)
	private Date quitDate;

	@Schema(description = "工作-邮件")
	@Size(max = 32, message = "工作-邮件最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workEmail;

	@Schema(description = "工作-电话")
	@Size(max = 32, message = "工作-电话最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workPhoneNumber;

	@Schema(description = "工作-国家")
	@Size(max = 32, message = "工作-国家最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workCountry;

	@Schema(description = "工作-省/市")
	@Size(max = 32, message = "工作-省/市最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workRegion;

	@Schema(description = "工作-城市")
	@Size(max = 32, message = "工作-城市最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workLocality;

	@Schema(description = "工作-街道")
	@Size(max = 32, message = "工作-街道最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workStreetAddress;

	@Schema(description = "工作-地址全称")
	@Size(max = 32, message = "工作-地址全称最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workAddressFormatted;

	@Schema(description = "工作-邮编")
	@Size(max = 32, message = "工作-邮编最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workPostalCode;

	@Schema(description = "工作-传真")
	@Size(max = 32, message = "工作-传真最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String workFax;

	@Schema(description = "工作-公司名称")
	@Size(max = 128, message = "工作-公司名称最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String workOfficeName;

	private Date createTime;

	/**
	 * for extended Attribute from userType extraAttribute for database
	 * extraAttributeName & extraAttributeValue for page submit
	 */
	private String extraAttribute;

	private Integer online;

	@Schema(description = "锁定状态")
	private Integer isLocked;

	@Schema(description = "锁定时间")
	private String unlockTime;

	@Schema(description = "备注")
	@Size(max = 256, message = "备注最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String remark;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "会话ID")
	private String sessionId;

	@Schema(description = "BASE64解析的图片")
	private String pictureBase64;

	@Schema(description = "文件上传ID")
	private String pictureId;

	@Schema(description = "机构ID")
	private String instId;

	protected String extraAttributeName;

	protected String extraAttributeValue;

	@JsonIgnore
	protected HashMap<String, String> extraAttributeMap;

	@JsonIgnore
	protected HashMap<String, String> protectedAppsMap;

	private String instName;

	String syncId;

	String syncName;

	String originId;

	String originId2;

	private String roleId;

	private String roleName;

	private String category;

	private String memberId;

	public UserVO(String username) {
		super();
		this.username = username;
	}

	public UserVO(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public void transPictureBase64() {
		if (picture != null) {
			this.pictureBase64 = ImageHelper.encodeImage(picture);
		}
	}

	public HashMap<String, String> getProtectedAppsMap() {
		if (protectedAppsMap == null) {
			protectedAppsMap = new HashMap<String, String>();
		}
		if (StrHelper.isNotBlank(protectedApps)) {
			String[] apps = protectedApps.split(",");

			for (String appid : apps) {
				protectedAppsMap.put(appid, appid);
			}
		}
		return protectedAppsMap;
	}

	public void trans() {
		this.setPassword("");
		this.setDecipherable("");
		this.transPictureBase64();
	}
}