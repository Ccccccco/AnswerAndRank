package com.xyl.game.Service.impl;

import com.xyl.game.Service.AuthenticationSerivce;
import com.xyl.game.po.Admin;
import com.xyl.game.utils.HeapVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 后台登录认证
 * @author dazhi
 *
 */
@Service
public class AuthenticationSerivceImpl implements AuthenticationSerivce{

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationSerivceImpl.class);
	/**
	 * 判断后台是否是第一次登录
	 */
	@Override
	public boolean isFirstAdmin() {
		if(HeapVariable.pwd == null){
			//是第一次登录
			logger.info("第一次登录");
			return true;
		}

		return false;
	}
	@Override
	public Admin admin(String adminId, String pwd) {
		if(adminId == null ){
			HeapVariable.pwd = new Admin();
			HeapVariable.pwd.setId(adminId);
			HeapVariable.pwd.setPwd(pwd);
		}
		Admin pwd2 = HeapVariable.pwd;
		return null;
	}

}
