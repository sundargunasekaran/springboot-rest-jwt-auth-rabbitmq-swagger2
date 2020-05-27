package com.test.example.model;

import java.util.HashSet;
import java.util.Set;



public class UserModel {

	private int id;
	private String username;
	private String password;
	private String role;
	private String dept;
	private String email;
	private String role_id;
	private String roleName;
	private Set<String> roles = new HashSet<>();
	
	public UserModel(){
		id = -1;
		username = "";
		role = "";
		email = "";
		dept = "";
		role_id = "";
		roleName = "";
		roles = null;
	}
	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	public UserModel(String username, String password, String roles) {
	    this.username = username;
	    this.password = password;
	    this.role = roles;
	  }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole_id() {
		return role_id;
	}
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
	
}
