package dream.study.authorization.server.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;
import dream.study.authorization.server.convert.UserConvert;
import dream.study.authorization.server.entity.ResourceEntity;
import dream.study.authorization.server.entity.RoleMemberEntity;
import dream.study.authorization.server.entity.RolePrivilegeEntity;
import dream.study.authorization.server.entity.UserEntity;
import dream.study.authorization.server.mapper.ResourceMapper;
import dream.study.authorization.server.mapper.RoleMemberMapper;
import dream.study.authorization.server.mapper.RolePrivilegeMapper;
import dream.study.authorization.server.mapper.UserMapper;
import dream.study.authorization.server.query.UserQuery;
import dream.study.authorization.server.service.UserService;
import dream.study.authorization.server.vo.UserVO;
import lombok.AllArgsConstructor;

/**
 * 用户信息
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
@AllArgsConstructor
public class UserServiceImpl extends AbstractServiceImpl<UserEntity, UserVO, UserQuery, UserConvert, UserMapper>
		implements UserService, UserDetailsService {

	private final RoleMemberMapper roleMemberMapper;

	private final RolePrivilegeMapper rolePrivilegeMapper;

	private final ResourceMapper resourceMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 在Security中“username”就代表了用户登录时输入的账号，在重写该方法时它可以代表以下内容：账号、手机号、邮箱、姓名等
		// “username”在数据库中不一定非要是一样的列，它可以是手机号、邮箱，也可以都是，最主要的目的就是根据输入的内容获取到对应的用户信息，如下方所示
		// 通过传入的账号信息查询对应的用户信息
		LambdaQueryWrapper<UserEntity> wrapper = Wrappers.lambdaQuery(UserEntity.class)
				.or(o -> o.eq(UserEntity::getEmail, username))
				.or(o -> o.eq(UserEntity::getMobile, username))
				.or(o -> o.eq(UserEntity::getUsername, username));
		UserEntity userEntity = baseMapper.selectOne(wrapper);
		if (userEntity == null) {
			throw new UsernameNotFoundException("账号不存在");
		}

		// 通过用户角色关联表查询对应的角色
		List<RoleMemberEntity> userRoles = roleMemberMapper.selectList(
				Wrappers.lambdaQuery(RoleMemberEntity.class).eq(RoleMemberEntity::getMemberId, userEntity.getId()));
		List<String> rolesId = Optional.ofNullable(userRoles)
				.orElse(Collections.emptyList())
				.stream()
				.map(RoleMemberEntity::getRoleId)
				.collect(Collectors.toList());
		if (ObjectUtils.isEmpty(rolesId)) {
			return userEntity;
		}
		// 通过角色菜单关联表查出对应的菜单
		List<RolePrivilegeEntity> roleMenus = rolePrivilegeMapper.selectList(
				Wrappers.lambdaQuery(RolePrivilegeEntity.class).in(RolePrivilegeEntity::getRoleId, rolesId));
		List<String> menusId = Optional.ofNullable(roleMenus)
				.orElse(Collections.emptyList())
				.stream()
				.map(RolePrivilegeEntity::getResourceId)
				.collect(Collectors.toList());
		if (ObjectUtils.isEmpty(menusId)) {
			return userEntity;
		}

		// 根据菜单ID查出菜单
		List<ResourceEntity> menus = resourceMapper.selectBatchIds(menusId);
		Set<SimpleGrantedAuthority> authorities = Optional.ofNullable(menus)
				.orElse(Collections.emptyList())
				.stream()
				.map(ResourceEntity::getResourceUrl)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toSet());
		userEntity.setAuthorities(authorities);
		return userEntity;
	}
}