package com.cris.security.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @ClassName MySecurityConfig
 * @Description TODO
 * @Author zc-cris
 * @Version 1.0
 **/
@EnableWebSecurity  // 开启security注解
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    // 自定义静态资源的权限规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    //    super.configure(http);
        // 定制请求的授权规则
        http.authorizeRequests().antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("VIP1")
                .antMatchers("/level2/**").hasRole("VIP2")
                .antMatchers("/level3/**").hasRole("VIP3");

        // 开启自动配置的登录功能，即如果没有登录，没有权限就会来到默认的用户登录页面
        http.formLogin().usernameParameter("user").passwordParameter("pwd").loginPage("/userlogin");   // 发送指定请求到指定的登录页面
        // 1. 发送 /login请求来到默认的登录页面
        // 2. 登录失败发送 /login?error 表示登录失败
        // 3. 更多详情参考文档
        // 4. 默认发送post形式的/login请求会交给spring security来处理登录验证；但是一旦修改了loginPage方法，
        // 即定制了loginPage，那么loginPage的post请求就是处理登录验证逻辑的请求（但是可以通过loginProcessingUrl(String url)来修改请求路径）

        // 开启自动配置的注销功能
        http.logout().logoutSuccessUrl("/");    // 修改注销成功后返回首页
        // 1. 默认访问/logout 表示注销请求，同时清空session
        // 2. 默认注销成功后返回到 /login?logout 页面（就是默认的登录页面）

        // 开启默认的记住我功能；登录成功后，服务器将名为remember-me的cookie 发送给浏览器保存，以后再访问服务器带上这个cookie即可，只要再
        // 服务器找到对应的这条cookie记录就可以免登录；如果注销成功也会删除这个cookie
        http.rememberMe().rememberMeParameter("remember");  // 自定义记住我的参数名字
    }

    // 用户登录以及授权功能
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
        /*实际开发中用户名和密码，角色信息都应该从内存中读取*/
        auth.inMemoryAuthentication().withUser("cris").password("123456").roles("VIP1", "VIP2")
                .and()
                .withUser("张三").password("123456").roles("VIP2", "VIP3");
    }
}
