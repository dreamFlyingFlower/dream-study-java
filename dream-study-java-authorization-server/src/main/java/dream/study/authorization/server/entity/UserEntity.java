package dream.study.authorization.server.entity;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import dream.flying.flower.framework.mybatis.plus.entity.AbstractStringEntity;
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
@TableName("auth_user")
public class UserEntity extends AbstractStringEntity implements UserDetails {

	private static final long serialVersionUID = -3601222345418102072L;

	/**
	 * 登录名
	 */
	private String username;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 显示名称
	 */
	private String displayName;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * DE密码
	 */
	private String decipherable;

	/**
	 * 手机号码
	 */
	private String mobile;

	/**
	 * 手机号验证
	 */
	private Integer mobileVerified;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 邮箱验证
	 */
	private Integer emailVerified;

	/**
	 * 性别
	 */
	private Integer gender;

	/**
	 * 是否已婚
	 */
	private Integer married;

	/**
	 * 生日
	 */
	private Date birthDate;

	/**
	 * 图片
	 */
	@JsonIgnore
	private byte[] picture;

	/**
	 * 证件类型
	 */
	private Integer idType;

	/**
	 * 证件号码
	 */
	private String idCardNo;

	/**
	 * 学历
	 */
	private String education;

	/**
	 * 毕业院校
	 */
	private String graduateFrom;

	/**
	 * 毕业日期
	 */
	private Date graduateDate;

	/**
	 * 个人主页
	 */
	private String webSite;

	/**
	 * 密码问题
	 */
	private String passwordQuestion;

	/**
	 * 密码答案
	 */
	private String passwordAnswer;

	/**
	 * 认证类型
	 */
	private Integer authnType;

	/**
	 * 应用登录认证类型
	 */
	private Integer appLoginAuthnType;

	/**
	 * 应用登录密码
	 */
	private String appLoginPassword;

	/**
	 * 应用登录密码保护应用
	 */
	private String protectedApps;

	/**
	 * 最近密码修改时间
	 */
	private Date passwordLastSetTime;

	/**
	 * 密码错误次数
	 */
	private Integer badPasswordCount;

	/**
	 * 最近密码错误时间
	 */
	private Date badPasswordTime;

	/**
	 * 是否锁定
	 */
	private Integer isLocked;

	/**
	 * 锁定时间
	 */
	private Date unlockTime;

	/**
	 * 最近登录时间
	 */
	private Date lastLoginTime;

	/**
	 * 最近登录IP地址
	 */
	private String lastLoginIp;

	/**
	 * 最近注销时间
	 */
	private Date lastLogoffTime;

	/**
	 * 密码设置类型
	 */
	private Integer passwordSetType;

	/**
	 * 登录次数统计
	 */
	private Integer loginCount;

	/**
	 * 历史区域
	 */
	private String regionHistory;

	/**
	 * 历史密码
	 */
	private String passwordHistory;

	/**
	 * 地址
	 */
	private String locale;

	/**
	 * 时区
	 */
	private String timeZone;

	/**
	 * 语言偏好
	 */
	private String preferredLanguage;

	/**
	 * 家庭-省/市
	 */
	private String homeCountry;

	/**
	 * 家庭-市
	 */
	private String homeRegion;

	/**
	 * 家庭-区
	 */
	private String homeLocality;

	/**
	 * 家庭-街道
	 */
	private String homeStreetAddress;

	/**
	 * 家庭-地址全称
	 */
	private String homeAddressFormatted;

	/**
	 * 家庭-邮件
	 */
	private String homeEmail;

	/**
	 * 家庭-电话
	 */
	private String homePhoneNumber;

	/**
	 * 家庭-邮编
	 */
	private String homePostalCode;

	/**
	 * 家庭-传真
	 */
	private String homeFax;

	/**
	 * 成本中心
	 */
	private String costCenter;

	/**
	 * 分支
	 */
	private String division;

	/**
	 * IM账号
	 */
	private String defineIm;

	/**
	 * TIME-OPT密钥
	 */
	private String sharedSecret;

	/**
	 * COUNTER-OPT密钥
	 */
	private String sharedCounter;

	/**
	 * 用户类型:"Employee", "Supplier","Dealer","Contractor",Partner,Customer "Intern",
	 * "Temp", "External", and "Unknown" .
	 */
	private String userType;

	/**
	 * 状态
	 */
	private String userStatus;

	/**
	 * AD域账号
	 */
	private String windowsAccount;

	/**
	 * 名字中文拼音
	 */
	private String nameZhSpell;

	/**
	 * 名字中文拼音简称
	 */
	private String nameZhShortSpell;

	/**
	 * 名
	 */
	private String givenName;

	/**
	 * 中间名
	 */
	private String middleName;

	/**
	 * 姓
	 */
	private String familyName;

	/**
	 * 前缀
	 */
	private String honorificPrefix;

	/**
	 * 后缀
	 */
	private String honorificSuffix;

	/**
	 * 用户全名
	 */
	private String formattedName;

	/**
	 * 微信粉丝
	 */
	private Integer weixinFollow;

	/**
	 * 主题
	 */
	private String theme;

	/**
	 * for extended Attribute from userType extraAttribute for database
	 * extraAttributeName & extraAttributeValue for page submit
	 */
	private String extraAttribute;

	/**
	 * 是否在线
	 */
	private Integer online;

	/**
	 * LDAP信息
	 */
	private String ldapDn;

	/**
	 * 应用列表类型
	 */
	private Integer gridList;

	/**
	 * 状态
	 */
	private Integer status;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 机构ID
	 */
	private String instId;

	/**
	 * 工号
	 */
	private String employeeNumber;

	/**
	 * 开始工作时间
	 */
	private Date startWorkDate;

	/**
	 * 工作-国家
	 */
	private String workCountry;

	/**
	 * 工作-省/市
	 */
	private String workRegion;

	/**
	 * 工作-城市
	 */
	private String workLocality;

	/**
	 * 工作-街道
	 */
	private String workStreetAddress;

	/**
	 * 工作-地址全称
	 */
	private String workAddressFormatted;

	/**
	 * 工作-邮件
	 */
	private String workEmail;

	/**
	 * 工作-电话
	 */
	private String workPhoneNumber;

	/**
	 * 工作-邮编
	 */
	private String workPostalCode;

	/**
	 * 工作-传真
	 */
	private String workFax;

	/**
	 * 工作-公司名称
	 */
	private String workOfficeName;

	/**
	 * 机构
	 */
	private String orgId;

	/**
	 * 部门编号
	 */
	private String departmentId;

	/**
	 * 部门
	 */
	private String department;

	/**
	 * 职务
	 */
	private String jobTitle;

	/**
	 * 工作职级
	 */
	private String jobLevel;

	/**
	 * 经理编号
	 */
	private String managerId;

	/**
	 * 经理名字
	 */
	private String manager;

	/**
	 * 助理编号
	 */
	private String assistantId;

	/**
	 * 助理名字
	 */
	private String assistant;

	/**
	 * 入职日期
	 */
	private Date entryDate;

	/**
	 * 离职日期
	 */
	private Date quitDate;

	public UserEntity(String username) {
		super();
		this.username = username;
	}

	public UserEntity(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	/**
	 * 权限信息 非数据库字段
	 */
	@TableField(exist = false)
	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.deleted == 0;
	}
}