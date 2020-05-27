package com.test.example.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.test.example.controller.TestController;
import com.test.example.model.EmployeeModel;
import com.test.example.model.UserModel;

@Repository
public class TestDataService extends JdbcDaoSupport{

	private static final Logger LOGGER = LoggerFactory.getLogger(TestDataService.class);
	
	@Autowired
    private DataSource dataSource;
	
	@PostConstruct
	private void initialize(){
		setDataSource(dataSource);
	}
	
	public String getScheduledCornExpression() {
		// 0 23 * * * ? 11pm --- "*/30 * * * * ? 30s"
		return "0 0/24 13 * * *";
	}
	
	//@Retryable(value = { SQLException.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
	public List<EmployeeModel> getEmployeeInfo(String empId) throws Exception {
        String sql = "select user_id,user_name  from loc_users ";
		try{
			  String whereSql = " where 1=1 ";
		        ArrayList<String> paramList = new ArrayList<String>();
		        if(empId != null && !empId.trim().equals("")){
		        	whereSql +=" and user_id = ? ";
		        	paramList.add(empId.toLowerCase());
		        }
		        sql += whereSql;
		        //System.out.println("--> "+sql);
		        if(paramList.size() > 0){
		        	String[] param = paramList.toArray(new String[paramList.size()]);
		        	List<EmployeeModel> beans = getJdbcTemplate().query(
		        			sql,
		        			param,
		                    new RowMapper<EmployeeModel>() {
		                        public EmployeeModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		                        	EmployeeModel empinfoBean = new EmployeeModel();
		                        	empinfoBean.setEmpName(rs.getString("user_name"));
		                        	empinfoBean.setEmpId(rs.getString("user_id"));
		                        	
		                            return empinfoBean;
		                        }
		                    });
		            return beans;
		        }else{
		        	 return null;
		        }
		}catch(Exception e){
			LOGGER.error("", e);
		}
		return null;
      
        
    }
	
	public List<EmployeeModel> getAllInfo() throws Exception {
        String sql = "select user_id,user_name  from loc_users ";
		try{
			  String whereSql = " where 1=1 ";
		        ArrayList<String> paramList = new ArrayList<String>();
		        
		        //System.out.println("--> "+sql);
		       // if(paramList.size() > 0){
		        	String[] param = paramList.toArray(new String[paramList.size()]);
		        	List<EmployeeModel> beans = getJdbcTemplate().query(
		        			sql,
		        			param,
		                    new RowMapper<EmployeeModel>() {
		                        public EmployeeModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		                        	EmployeeModel empinfoBean = new EmployeeModel();
		                        	empinfoBean.setEmpName(rs.getString("user_name"));
		                        	empinfoBean.setEmpId(rs.getString("user_id"));
		                        	
		                            return empinfoBean;
		                        }
		                    });
		            return beans;
		        //}else{
		        //	 return null;
		        //}
		}catch(Exception e){
			LOGGER.error("", e);
		}
		return null;
      
        
    }
	
	
	private Map<String,ArrayList<String>> getWhereClause(Map<String, String> queryMap) {
		ArrayList<String> param = new ArrayList<String>();
		Map<String,ArrayList<String>> plm = new LinkedHashMap<String,ArrayList<String>>();
		int i = 1;
		StringBuffer whereClause = new StringBuffer ("");
		for (Map.Entry<String, String> entry : queryMap.entrySet()) {
		   // System.out.println("--> "+entry.getKey()+"----"+entry.getValue());
		    if (queryMap.size() <= 0)
				return null;
		    if(i == 1 && queryMap.size() >= 1){
		    	whereClause.append (" WHERE ").append (entry.getKey());
				param.add(queryMap.get(entry.getKey()));
		    }else{
		    	whereClause.append (" AND ").append(entry.getKey());
				param.add(queryMap.get(entry.getKey()));
		    }
			
			i++;
		}
		plm.put(whereClause.toString(), param);
		return plm;
	}
	
	
	public UserModel findByUsername(String userName,String password) throws Exception {
        String sql = "SELECT lu.user_id,lu.user_name,lu.password,lu.email_id,lr.role_id,group_concat(lr.role_name) as role_name FROM  loc_users lu "+
						"left join loc_user_roles lur on lur.user_id = lu.user_id "+
						"left join loc_roles lr on lr.role_id = lur.role_id ";
		try{
			    String whereSql = "  ";
			    Map<String,String> qryMap = new LinkedHashMap<String,String>();
		        ArrayList<String> paramList = new ArrayList<String>();
		        if(userName != null && !userName.trim().equals("")){
		        	//whereSql +=" lu.user_name = ? ";
		        	//paramList.add(userName);
		        	qryMap.put(" lu.user_name = ? ", userName);
		        }
		        if(password != null && !password.trim().equals("")){
		        	//whereSql +=" and lu.password = ? ";
		        	//paramList.add(password);
		        	qryMap.put(" lu.password = ? ", password);
		        }
		        
		        Map<String,ArrayList<String>> retmap = this.getWhereClause(qryMap);
		        for (Map.Entry<String, ArrayList<String>> entry : retmap.entrySet()) {
				    whereSql = entry.getKey();
				    paramList = entry.getValue();
				}
		        sql += whereSql;
		        String groupBy = " group by lu.user_id ";
		        //if(paramList.size() > 0){
		        	String[] param = paramList.toArray(new String[paramList.size()]);
		        	UserModel userModel = getJdbcTemplate().queryForObject(
		                    sql+groupBy,
		                    param,
		                    new RowMapper<UserModel>() {
		                        public UserModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		                        	UserModel bean = new UserModel();
		                            bean.setId(rs.getInt("user_id")); 
		                            bean.setUsername(rs.getString("user_name"));
		                            bean.setPassword(rs.getString("password"));
		                            bean.setEmail(rs.getString("email_id"));
		                            bean.setRoleName(rs.getString("role_name"));
		                            /*List<String> list = Arrays.asList(rs.getString("role_name"));
		                            String result = String.join(",", list);*/
		                            return bean;
		                        }
		                    });
		        	return userModel;
		        /*}else{
		        	 return null;
		        }*/
		}catch(Exception e){
			LOGGER.error("", e);
		}
		return null;
      
        
    }
	
	public String getallurls() throws Exception{
		//url:rolename
		String temp = "DM:DM_MANAGER,RM:RM_MANAGER,RM:GUESTS_MANAGER";
		return temp;
	}
	
	/*@Recover
    public String recover(SQLException t){
        return "Service recovered from billing service failure.";
    }*/
	
}
