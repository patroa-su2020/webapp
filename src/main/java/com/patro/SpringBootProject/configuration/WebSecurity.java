//package com.patro.SpringBootProject.configuration;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.AuthenticationEntryPoint;
//
//import javax.sql.DataSource;
//
//public class WebSecurity extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private DataSource dataSource;
//
//    @Autowired
//    private AuthenticationEntryPoint authenticationEntryPoint;
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication().dataSource(dataSource)
//                .authoritiesByUsernameQuery("select email as username from user_data where email=?")
//                .usersByUsernameQuery("select email as username, password, true from user_data where email=?");
//
//    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/login").permitAll()
//                .antMatchers(HttpMethod.GET, "/books").permitAll()
//                .antMatchers(HttpMethod.POST, "/v1/signup").permitAll()
////                .antMatchers(HttpMethod.GET, "/v1/recipes").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .httpBasic().authenticationEntryPoint(authenticationEntryPoint);
//
//    }
//}
