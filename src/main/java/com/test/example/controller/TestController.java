package com.test.example.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.example.model.AuthenticationModel;
import com.test.example.model.EmployeeModel;
import com.test.example.model.UserModel;
import com.test.example.retryconfig.RetryService;
import com.test.example.service.TestDataService;
import com.test.example.service.UserDetailsServiceImpl;
import com.test.example.utils.CookieUtils;
import com.test.example.utils.JWTUtils;

import io.swagger.annotations.*;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "TestServiceController", description = "list of all Services")
@RequestMapping("/test")
public class TestController {

	@Autowired
	private TestDataService testDataService;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private JWTUtils jwtUtils;
	
	/*@Autowired
    private RetryService retryService;*/

	@Autowired
	AuthenticationManager authenticationManager;

	private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

	public TestController() {
		LOGGER.info("Test Controller");
	}

	@ApiOperation(value = "user sign in")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval") })
	@RequestMapping(method = RequestMethod.POST, value = "/signin", headers = "Accept= application/json", produces = "application/json")
	public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationModel model) throws Exception {
		String jwt = "";	
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(model.getUsername(), model.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			//jwt = jwtUtils.generateTokenWithClaims(model.getUsername(),model.getPassword(), authentication.getAuthorities().toString().replaceAll("\\[|\\]", "").replaceAll(", ","\t"));
			jwt = jwtUtils.generateToken(model.getUsername());
			//CookieUtils.create(new HttpServletResponse(), jwtTokenCookieName, token, false, -1, "localhost");
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}

		
		return ResponseEntity.ok(jwt);
	}

/*	@ApiOperation(value = "user generate auth token")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval") })
	@RequestMapping(method = RequestMethod.POST, value = "/generateToken", headers = "Accept= application/json", produces = "application/json")
	public ResponseEntity<?> generateToken(@RequestBody AuthenticationModel model) throws Exception {
		try{
			 authenticationManager.authenticate(
					  new UsernamePasswordAuthenticationToken( model.getUsername(),
					  model.getPassword() ) );
		}catch(UsernameNotFoundException e){
			throw new Exception("user not found");
		}
		//SecurityContextHolder.getContext().setAuthentication(authentication);
		//  UserDetails userDetails =  userDetailsServiceImpl.loadUserByUsername(model.getUsername());
		  final String jwt = jwtUtils.generateToken(model.getUsername());
		  return ResponseEntity.ok(jwt);
	}*/

	/*
	 * @ApiOperation(value = "TEST JSON")
	 * 
	 * @ApiResponses(value = {
	 * 
	 * @ApiResponse(code = 500, message = "Server error"),
	 * 
	 * @ApiResponse(code = 404, message =
	 * "The resource you were trying to reach is not found"),
	 * 
	 * @ApiResponse(code = 200, message = "Successful retrieval", response =
	 * EmployeeModel.class,responseContainer = "List") })
	 * 
	 * @RequestMapping(method=RequestMethod.GET, value="/getSampleJson",
	 * headers="Accept= application/json") public String
	 * getSampleJsonStr(@ApiParam("Enter size") @RequestParam(value = "size",
	 * required = false) String size) throws Exception { String fileName =
	 * "E:\\samplehub\\sample_json_test.txt"; File file = new File(fileName);
	 * FileReader fr = new FileReader(file); BufferedReader br = new
	 * BufferedReader(fr); String line; StringBuilder sb = new StringBuilder();
	 * while((line = br.readLine()) != null){ sb.append(line); }
	 * List<EmployeeModel> bean = testDataService.getEmployeeInfo("3000");
	 * return sb.toString(); }
	 */

	@ApiOperation(value = "Search a Emp with an Id")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = EmployeeModel.class, responseContainer = "List") })
	@RequestMapping(method = RequestMethod.GET, value = "/getUser/{id}", headers = "Accept= application/json", produces = "application/json")
	@PreAuthorize("hasAuthority('USER')")
	public List<EmployeeModel> getUserDetails(
			@ApiParam("Enter Id") @PathVariable(value = "id", required = false) String id) throws Exception {
		List<EmployeeModel> bean = null;
		try {
			bean = testDataService.getEmployeeInfo(id);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return bean;
	}

	@ApiOperation(value = "Get All Emp")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = EmployeeModel.class, responseContainer = "List") })
	@RequestMapping(method = RequestMethod.GET, value = "/getAll", headers = "Accept= application/json", produces = "application/json")
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<EmployeeModel> getAll() throws Exception {
		List<EmployeeModel> bean = null;
		try {
			bean = testDataService.getAllInfo();
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return bean;
	}
	
	@ApiOperation(value = "Say hello")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = EmployeeModel.class, responseContainer = "List") })
	@RequestMapping(method = RequestMethod.GET, value = "/say/{msg}", headers = "Accept= application/json", produces = "application/json")
	 @PreAuthorize("hasAuthority('USER') or hasRole('GUESTS')")
	public ResponseEntity<?> sayHello(@ApiParam("Enter your message") @PathVariable(value = "msg", required = false) String msg) throws Exception {
		return ResponseEntity.ok(msg);
	}
	
/*	@RequestMapping(path ="/getData/{id}", method = RequestMethod.GET)
    public Object getEmployee(@PathVariable("id") String id) throws Exception{
            return retryTemplate.execute(new RetryCallback<Object, Exception>() {
            @Override
            public Object doWithRetry(RetryContext arg0) throws Exception{
                Object o = restTutorialClient.getEmployeesList(id);
                return o;
            }
        });*/

}
