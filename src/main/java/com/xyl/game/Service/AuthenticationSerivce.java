package com.xyl.game.Service;

import com.xyl.game.po.Admin;

/**
 * 后台认证接口
 * @author dazhi
 *
 */
public interface AuthenticationSerivce {
	public boolean isFirstAdmin();

	public Admin admin(String adminId, String pwd);
}
