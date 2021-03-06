package com.wang.spring_security_framework.config.SpringSecurityConfig.SpringSecurityHandler;

import com.alibaba.fastjson.JSON;
import com.wang.spring_security_framework.config.SpringSecurityConfig.SpringSecurityConfigUtil.JWTUtil;
import com.wang.spring_security_framework.config.SpringSecurityConfig.SpringSecurityConfigUtil.UserRepository;
import com.wang.spring_security_framework.service.CaptchaService;
import com.wang.spring_security_framework.service.serviceImpl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

//登录成功处理
//我们不能在这里获得request了, 因为我们已经在前面自定义了认证过滤器, 做完后SpringSecurity会关闭inputStream流
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    CaptchaService captchaService;
    @Autowired
    JWTUtil jwtUtil;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailServiceImpl userDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        //我们从自定义的认证过滤器中拿到的authInfo, 接下来做验证码校验和跳转, 以及JWT的生成
        Map<String, String> authInfo = (Map<String, String>) request.getAttribute("authInfo");
        System.out.println(authInfo);
        System.out.println("success!");
        String token = authInfo.get("token");
        String inputCode = authInfo.get("inputCode");

        //校验验证码
        Boolean verifyResult = captchaService.versifyCaptcha(token, inputCode);
        System.out.println(verifyResult);

        Map<String, String> result = new HashMap<>();
        //验证码正确, 则生成JWT
        if (verifyResult) {
            //成功的跳转页面
            String VerifySuccessUrl = "/newPage";
            response.setHeader("Content-Type", "application/json;charset=utf-8");
            result.put("code", "200");
            result.put("msg", "认证成功!");
            result.put("url", VerifySuccessUrl);
            //JWT生成
            String jwt = jwtUtil.JWTCreator(authentication);
            result.put("jwt", jwt);
            //用户信息放入缓存 ==> 从userDetailsService的实现类中根据用户名切除User类
            userRepository.insert((User) userDetailsService.loadUserByUsername(authentication.getName()));
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(result));
        } else {
            String VerifyFailedUrl = "/toLoginPage";
            response.setHeader("Content-Type", "application/json;charset=utf-8");
            result.put("code", "201");
            result.put("msg", "验证码输入错误!");
            result.put("url", VerifyFailedUrl);
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(result));
        }
    }
}
