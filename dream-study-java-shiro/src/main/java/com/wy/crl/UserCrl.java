package com.wy.crl;

import com.wy.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * User用户API
 *
 * @author 飞花梦影
 * @date 2021-04-17 18:28:24
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Controller
@RequestMapping("user")
public class UserCrl {

    /**
     * 登录,成功就跳到index页面
     *
     * @param username 用户名
     * @param password 密码
     * @param request 请求
     * @return 页面跳转地址
     */
    @GetMapping("login")
    public String login(String username, String password, HttpServletRequest request) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        // 调用AuthRealm中的方法进行登录,并存储相关信息
        subject.login(token);
        User user = (User) subject.getPrincipal();
        request.getSession().setAttribute("user", user);
        return "index";
    }
}