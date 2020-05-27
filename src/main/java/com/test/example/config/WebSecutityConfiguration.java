package com.test.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.test.example.filters.JWTFilter;
import com.test.example.securityUtils.CustomAuthenticationProvider;
import com.test.example.service.TestDataService;
import com.test.example.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) 
public class WebSecutityConfiguration extends WebSecurityConfigurerAdapter {
	
	/*@Autowired
    private UserDetailsServiceImpl userDetailsService;*/
	
	@Autowired
	private CustomAuthenticationProvider authenticationProvider;
	
	@Autowired
	private JWTFilter jwtFilter;

	/*@Autowired
	@Lazy
    private TestDataService testDataService;*/

    /*@Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }*/

	@Autowired
	public void registerGlobalAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.authenticationProvider(authenticationProvider);
	}
	
    /*@Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        
    	authenticationManagerBuilder.authenticationProvider(authenticationProvider);
    	
    	//authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder()); // loadbyusername
    }*/

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().
                authorizeRequests()
                //.antMatchers("/**").permitAll()
               // .antMatchers("/generateToken").permitAll()
              //  .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/test/signin").permitAll()
                .anyRequest().authenticated()
                .and()
                //.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        /*String urls = testDataService.getallurls();
	    //String temp = "DM:DM_MANAGER,RM:RM_MANAGER,RM:GUESTS_MANAGER";
		 String[] roles = urls.split(",");
		 for(String a : roles){
			 String url = a.split(":")[0];
			 String role = a.split(":")[1];
	    	 http.authorizeRequests().antMatchers("/"+url.toLowerCase()+"/**").hasRole(role.toUpperCase()).anyRequest().authenticated();
	    	// http.authorizeRequests().antMatchers("/dm/**").hasRole("DM_MANAGER").anyRequest().authenticated();
	     }*/
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
      // Allow swagger to be accessed without authentication
      web.ignoring().antMatchers("/v2/api-docs")//
          .antMatchers("/swagger-resources/**")//
          .antMatchers("/swagger-ui.html")//
          .antMatchers("/configuration/**")//
          .antMatchers("/webjars/**")//
          .antMatchers("/public")
          
          // Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in production)
          .and()
          .ignoring()
          .antMatchers("/h2-console/**/**");
    }

}
