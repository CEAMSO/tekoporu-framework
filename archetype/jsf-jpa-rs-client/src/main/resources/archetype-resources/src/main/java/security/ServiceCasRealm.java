package ${package}.security;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ${package}.clientrest.AuthServiceCR;

public class ServiceCasRealm extends AuthorizingRealm {
	AuthServiceCR authCR;

	// default roles to applied to authenticated user
	private String defaultRoles;

	/*
	 * Parte de JDBC para autorización
	 * 
	 * @see
	 * org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache
	 * .shiro.subject.PrincipalCollection)
	 */

	private static final Logger log = LoggerFactory
			.getLogger(ServiceCasRealm.class);

	/*--------------------------------------------
	|         C O N S T R U C T O R S           |
	============================================*/
	public ServiceCasRealm() {
		authCR = new AuthServiceCR();
		setAuthenticationTokenClass(UsernameToken.class);
	}

	public String getDefaultRoles() {
		return defaultRoles;
	}

	public void setDefaultRoles(String defaultRoles) {
		this.defaultRoles = defaultRoles;
	}

	/**
	 * Split a string into a list of not empty and trimmed strings, delimiter is
	 * a comma.
	 * 
	 * @param s
	 *            the input string
	 * @return the list of not empty and trimmed strings
	 */
	private List<String> split(String s) {
		List<String> list = new ArrayList<String>();
		String[] elements = StringUtils.split(s, ',');
		if (elements != null && elements.length > 0) {
			for (String element : elements) {
				if (StringUtils.hasText(element)) {
					list.add(element.trim());
				}
			}
		}
		return list;
	}

	private String splitPermission(String codigo) {
		String permission = "";
		String[] elements = StringUtils.split(codigo, '_');
		if (elements != null && elements.length >= 2) {
			if (StringUtils.hasText(elements[1])
					&& StringUtils.hasText(elements[2])) {
				permission = permission + elements[1] + ":" + elements[2];
				if (elements.length == 4) {
					permission = permission + ":" + elements[3];
				}
			} else {
				permission = null;
			}
		} else {
			permission = null;
		}
		return permission;
	}

	/**
	 * Add roles to the simple authorization info.
	 * 
	 * @param simpleAuthorizationInfo
	 * @param roles
	 *            the list of roles to add
	 */
	private void addRoles(SimpleAuthorizationInfo simpleAuthorizationInfo,
			List<String> roles) {
		for (String role : roles) {
			simpleAuthorizationInfo.addRole(role);
		}
	}

	/**
	 * Add permissions to the simple authorization info.
	 * 
	 * @param simpleAuthorizationInfo
	 * @param permissions
	 *            the list of permissions to add
	 */
	private void addPermissions(
			SimpleAuthorizationInfo simpleAuthorizationInfo,
			List<String> permissions) {
		for (String permission : permissions) {
			simpleAuthorizationInfo.addStringPermission(permission);
		}
	}

	/**
	 * Authenticates a user and retrieves its information.
	 * 
	 * @param token
	 *            the authentication token
	 * @throws AuthenticationException
	 *             if there is an error during authentication.
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		UsernameToken uToken = (UsernameToken) token;
		if (token == null) {
			return null;
		}

		String name = (String) uToken.getCredentials();
		if (!StringUtils.hasText(name)) {
			return null;
		}

		List<String> principals = CollectionUtils.asList(name);
		PrincipalCollection principalCollection = new SimplePrincipalCollection(
				principals, getName());
		// log
		System.out.println("RealmName: " + getName());

		SimpleAuthenticationInfo sai = new SimpleAuthenticationInfo(
				principalCollection, name);
		return sai;

	}

	/**
	 * This implementation of the interface expects the principals collection to
	 * return a String username keyed off of this realm's {@link #getName()
	 * name}
	 * 
	 * @see #getAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		// null usernames are invalid
		if (principals == null) {
			throw new AuthorizationException(
					"Principal Collection method argument cannot be null.");
		}
		String username = (String) getAvailablePrincipal(principals);
		Set<String> roleNames = null;

		/* llamar al servicio de permisos */
		Set<String> permissions = new LinkedHashSet<String>();
		List<Map<String, String>> list = authCR.getPermisos(username);
		// System.out.println("+++++ Permisos de " + username + " +++++");

		for (Map<String, String> per : list) {
			String perFormated = splitPermission(per.get("codigo"));
			// System.out.println(perFormated);
			if (perFormated != null) {
				permissions.add(perFormated);
			}
		}
		/*
		 * permissions.add(perFormated); permissions.add(perFormated);
		 * permissions.add(perFormated); permissions.add(perFormated);
		 */
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
		addRoles(info, split(defaultRoles));
		info.setStringPermissions(permissions);
		return info;

	}

}
