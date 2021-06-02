package com.wy.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.wy.valid.ValidEdit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户表 ts_user
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel(description = "用户表 ts_user")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("ts_user") // 指定表名
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户编号
	 */
	@ApiModelProperty("用户编号")
	@NotNull(groups = ValidEdit.class)
	@TableId(value = "user_id", type = IdType.AUTO) // 指定主键,指定主键生成方式
	private Long userId;

	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
	@NotBlank(message = "用户账号不能为空")
	@Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
	@TableField(value = "username", condition = SqlCondition.LIKE) // 指定字段名,同时可指定查询时的默认(等值)行为,其他行为可参考SqlCondition
	private String username;

	/**
	 * 密码,md5加密
	 */
	@ApiModelProperty("密码,md5加密")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	/**
	 * 真实姓名
	 */
	@ApiModelProperty("真实姓名")
	@Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
	private String realname;

	/**
	 * 部门编号
	 */
	@ApiModelProperty("部门编号")
	private Long departId;

	/**
	 * 身份证号
	 */
	@ApiModelProperty("身份证号")
	private String idcard;

	/**
	 * 性别,见ts_dict表SEX
	 */
	@ApiModelProperty("性别,见ts_dict表SEX")
	private String sex;

	/**
	 * 年龄
	 */
	@ApiModelProperty("年龄")
	private Integer age;

	/**
	 * 住址
	 */
	@ApiModelProperty("住址")
	private String address;

	/**
	 * 出生日期
	 */
	@ApiModelProperty("出生日期")
	private Date birthday;

	/**
	 * 邮箱地址
	 */
	@ApiModelProperty("邮箱地址")
	@Email(message = "邮箱格式不正确")
	@Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
	private String email;

	/**
	 * 手机号
	 */
	@ApiModelProperty("手机号")
	@Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
	private String mobile;

	/**
	 * 用户状态:0黑名单;默认1正常;2休假;3离职中;4离职
	 */
	@ApiModelProperty("用户状态:0黑名单;默认1正常;2锁定;3休假;4离职中;5离职;6逻辑删除")
	private Integer state;

	/**
	 * 用户头像
	 */
	@ApiModelProperty("用户头像")
	private String avatar;

	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
	@TableField(fill = FieldFill.INSERT)
	private Date createtime;

	/**
	 * 修改时间
	 */
	@ApiModelProperty("修改时间")
	@TableField(fill = FieldFill.UPDATE)
	private Date updatetime;

	/**
	 * 逻辑删除
	 * 
	 * {@link TableField#select()}:定义查询时是否查出该值,只对mybatis-plus内置sql有效
	 */
	@ApiModelProperty("逻辑删除")
	@TableField(select = false)
	@TableLogic(value = "1", delval = "2")
	private Integer logicDelete;

	/** 非数据库字段 */
	/**
	 * 用户登录成功后返回的token
	 */
	@ApiModelProperty("用户登录成功后返回的token")
	@TableField(exist = false)
	private String token;

	/**
	 * 用户存入token到redis中的时间
	 */
	@ApiModelProperty("用户存入token到redis中的时间")
	@TableField(exist = false)
	private Date loginTime;

	/**
	 * 部门对象
	 */
	@ApiModelProperty("部门对象")
	@TableField(exist = false)
	private Depart depart;

	/**
	 * 用户扩展信息
	 */
	@ApiModelProperty("用户扩展信息")
	@TableField(exist = false)
	private Userinfo userinfo;

	/**
	 * 用户角色对象
	 */
	@ApiModelProperty("用户角色对象")
	@TableField(exist = false)
	private List<Role> roles;
}