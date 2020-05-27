package com.test.example.service;

import com.test.example.model.UserModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /*@Autowired
    TestDataService testDataService;*/
    @Autowired
	private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
    	
        UserModel user;
        UserBuilder builder = null;
		try {
			//user = testDataService.findByUsername(username,null);
				//ArrayList<> roleList = Arrays.asList(user.getRoleName());
				//return new User(user.getUsername(),user.getPassword(),new ArrayList<>());
			//if(user != null){
				
				 builder = org.springframework.security.core.userdetails.User.withUsername(username);
			     builder.password(new BCryptPasswordEncoder().encode("Sundar"));
			     builder.roles("admin");
			//}else{
				throw new UsernameNotFoundException("User not found.");
			//}
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
       // return UserPrinciple.build(user);
    }
}