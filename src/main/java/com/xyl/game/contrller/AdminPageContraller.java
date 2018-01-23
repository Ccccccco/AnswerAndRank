package com.xyl.game.contrller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 权限访问控制
 * @author dazhi
 *
 */
import org.springframework.web.bind.annotation.ResponseBody;

import com.xyl.game.Service.AuthenticationSerivce;
/**
 * 后台页面访问控制
 * @author dazhi
 *
 */
@Controller
public class AdminPageContraller {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminPageContraller.class);
	
	@Autowired
	private AuthenticationSerivce authenticationSerivce;
	
	@RequestMapping("/")
	public String getIndexPage(HttpServletRequest request,HttpServletResponse response){
		return "main";
	}
	
	@RequestMapping("/loadPage")
	public String getLoadPage(HttpServletRequest request,HttpServletResponse response){
		return "AnnualMeetingLoadExctFile";
	}
	
	@RequestMapping("/setUpPage")
	public String getSetUpTimePage(HttpServletRequest request,HttpServletResponse response){
		return "AnnualTimeSetUp";
	}
	
	@RequestMapping("/adminPage")
	public String getAdminPage(HttpServletRequest request,HttpServletResponse response){
		return "admin";
	}
	
	@RequestMapping("/isFirstAdmin")
	@ResponseBody
	public String IsAdmin(){
		if(authenticationSerivce.isFirstAdmin()){
			return UUID.randomUUID().toString();
		}
		return "no";
	}
	
	
	@RequestMapping("/admin")
	@ResponseBody
	public String admin(String adminId,String pwd){
		//登录操作
		authenticationSerivce.admin(adminId,pwd);
		return "admin";
	}
}
