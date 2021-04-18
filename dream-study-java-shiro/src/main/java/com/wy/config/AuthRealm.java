package com.wy.config;

import com.wy.collection.ListTool;
import com.wy.model.Permission;
import com.wy.model.Role;
import com.wy.model.User;
import com.wy.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Shiro授权自定义实现类
 *
 * @author 飞花梦影
 * @date 2021-04-17 17:37:38
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 授权
     *
     * @param principals 用户信息
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User)principals.fromRealm(this.getClass().getName()).iterator().next();
        List<Role> roles = user.getRoles();
        List<String> permissionNames = new ArrayList<>();
        List<String> roleNames = new ArrayList<>();
        if (ListTool.isNotEmpty(roles)){
            for (Role role :roles){
                roleNames.add(role.getRoleName());
                Set<Permission> permissions = role.getPermissions();
                if (ListTool.isNotEmpty(permissions)){
                    for (Permission permission : permissions){
                        permissionNames.add(permission.getName());
                    }
                }
            }
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(permissionNames);
        simpleAuthorizationInfo.addRoles(roleNames);
        return simpleAuthorizationInfo;
    }

    /**
     * 认证登录
     *
     * @param token 用户登录的信息,用户名和密码
     * @return 认证信息
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (token instanceof UsernamePasswordToken) {
            String username = ((UsernamePasswordToken) token).getUsername();
            User user = userService.getByUsername(username);
            return new SimpleAuthenticationInfo(user, user.getPassword(), this.getClass().getName());
        }
        return null;
    }
}