package cs.common;


import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import cs.service.framework.UserService;

public class SystemRealm  extends AuthorizingRealm {

	@Autowired
	private UserService userService;
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		System.out.println("authorize process");
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		Set<String > permissions=userService.getCurrentUserPermissions();
		authorizationInfo.addStringPermissions(permissions);
		return authorizationInfo;
	}
	
	 


	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("authentication process");
		// TODO Auto-generated method stub
		String userName = (String) token.getPrincipal(); 
		String password = new String((char[]) token.getCredentials()); 
		
		return new SimpleAuthenticationInfo(userName, password, getName());
	}

}
