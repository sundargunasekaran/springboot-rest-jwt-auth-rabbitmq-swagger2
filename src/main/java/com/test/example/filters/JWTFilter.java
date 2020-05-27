package com.test.example.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.GrantedAuthority;
import com.test.example.model.UserModel;
import com.test.example.service.TestDataService;
import com.test.example.service.UserDetailsServiceImpl;
import com.test.example.utils.JWTUtils;

@Component
public class JWTFilter extends OncePerRequestFilter {
	
	/*@Autowired
    private UserDetailsServiceImpl userDetailsService;*/
	
	/*@Autowired
    private TestDataService testDataService;*/


    @Autowired
    private JWTUtils jwtUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtils.extractUsername(jwt);
        }
		
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

           // UserDetails userDetails = this.userDetailsService.loadUserByUsername(username); // 1
        	/*UserModel userModel = null;                           //2
			try {
				userModel = testDataService.findByUsername(username,null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
            grantedAuthorities.add(new SimpleGrantedAuthority(userModel.getRoleName()));*/
            if (jwtUtils.validateToken(jwt, username)) {
            	
            	UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = jwtUtils.getAuthentication(username);   //3
            	usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
           	 	SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            	
            	 /*Authentication auth = new
                         UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
            	 SecurityContextHolder.getContext().setAuthentication(auth);*/
            	
            	 //2
            	 /*UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken  = new UsernamePasswordAuthenticationToken(username, null,grantedAuthorities);
            	 usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            	 SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);*/
            	
            	//1 
                /*UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);*/
            }
        }
        filterChain.doFilter(request, response);
    }

}
