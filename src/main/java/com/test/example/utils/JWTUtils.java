package com.test.example.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.core.GrantedAuthority;

import com.test.example.config.TestConfigProperty;
import com.test.example.exceptions.CustomException;
import com.test.example.model.UserModel;
import com.test.example.service.TestDataService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
@Service
public class JWTUtils {
	
	@Autowired
	@Lazy
    private TestDataService testDataService;
	
	@Autowired
	private TestConfigProperty testConfigProperty;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        //System.out.println("role "+(String) claims.get("role"));
       // System.out.println("pwd "+(String) claims.get("password"));
        //String role = (String) claims.get("role");
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(testConfigProperty.getSecretKey()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>(); // <role,list<authorities>
        return createToken(claims, username);
    }
    
    public String generateTokenWithClaims(String username,String password,String roles) {
        Map<String, Object> claims = new HashMap<>(); // <role,list<authorities>
        if(claims.size() > 0 && claims.get("role") != null){
        	 claims.put("role", roles);
        }
        if(claims.size() > 0 && claims.get("password") != null){
        	claims.put("password", password);
       }        
        return createToken(claims, username);
    }

    protected Date getExpirationTime()
    {
    	Date now = new Date();
        Long expireInMilis = TimeUnit.MINUTES.toMillis(testConfigProperty.getExpireDuration());
        return new Date(expireInMilis + now.getTime());
    }
    
    private String createToken(Map<String, Object> claims, String username) {

        return Jwts.builder()
        		.setClaims(claims)
        		.setSubject(username)
        		.setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(getExpirationTime())
                .signWith(SignatureAlgorithm.HS256, testConfigProperty.getSecretKey()).compact();
    }

    public Boolean validateToken(String token, String username) {
        final String tkUsername = extractUsername(token);
        return (tkUsername.equals(username) && !isTokenExpired(token));
    }
    
    public static String getSubject(HttpServletRequest httpServletRequest, String jwtTokenCookieName, String signingKey){
        String token = CookieUtils.getValue(httpServletRequest, jwtTokenCookieName);
        if(token == null) return null;
        return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody().getSubject();
    }
    
    public UsernamePasswordAuthenticationToken getAuthentication(String username) {
        //UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
       // return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    	
    	UserModel userModel = null;
		try {
			userModel = testDataService.findByUsername(username,null); //3
		} catch (Exception e) {
			throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
		}
    	List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority(userModel.getRoleName()));
        
        //UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken  = new UsernamePasswordAuthenticationToken(username, null,grantedAuthorities);
   	 	//usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
   	 	//fSecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        
    	return new UsernamePasswordAuthenticationToken(username, null,grantedAuthorities);
      }

}
