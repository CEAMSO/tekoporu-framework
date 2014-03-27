/*
 * TICPY Framework
 * Copyright (C) 2012 Plan Director TICs
 *
----------------------------------------------------------------------------
 * Originally developed by SERPRO
 * Demoiselle Framework
 * Copyright (C) 2010 SERPRO
 *
----------------------------------------------------------------------------
 * This file is part of TICPY Framework.
 *
 * TICPY Framework is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License version 3
 * along with this program; if not,  see <http://www.gnu.org/licenses/>
 * or write to the Free Software Foundation, Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA  02110-1301, USA.
 *
----------------------------------------------------------------------------
 * Este archivo es parte del Framework TICPY.
 *
 * El TICPY Framework es software libre; Usted puede redistribuirlo y/o
 * modificarlo bajo los términos de la GNU Lesser General Public Licence versión 3
 * de la Free Software Foundation.
 *
 * Este programa es distribuido con la esperanza que sea de utilidad,
 * pero sin NINGUNA GARANTÍA; sin una garantía implícita de ADECUACION a cualquier
 * MERCADO o APLICACION EN PARTICULAR. vea la GNU General Public Licence
 * más detalles.
 *
 * Usted deberá haber recibido una copia de la GNU Lesser General Public Licence versión 3
 * "LICENCA.txt", junto con este programa; en caso que no, acceda a <http://www.gnu.org/licenses/>
 * o escriba a la Free Software Foundation (FSF) Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02111-1301, USA.
 */

package org.ticpy.tekoporu.internal.interceptor;

import java.io.Serializable;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;

import org.ticpy.tekoporu.annotation.Name;
import org.ticpy.tekoporu.internal.producer.LoggerProducer;
import org.ticpy.tekoporu.internal.producer.ResourceBundleProducer;
import org.ticpy.tekoporu.security.AuthorizationException;
import org.ticpy.tekoporu.security.RequiredPermission;
import org.ticpy.tekoporu.security.SecurityContext;
import org.ticpy.tekoporu.security.User;
import org.ticpy.tekoporu.util.Beans;
import org.ticpy.tekoporu.util.ResourceBundle;
import org.ticpy.tekoporu.util.Strings;

/**
 * Intercepts calls with {@code @RequiredPermission} annotations.
 * 
 * @author SERPRO
 */
@Interceptor
@RequiredPermission
public class RequiredPermissionInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;

	private SecurityContext securityContext;

	private static ResourceBundle bundle;

	private static Logger logger;

	/**
	 * Gets the values for both resource and operation properties of {@code @RequiredPermission}. Delegates to
	 * {@code SecurityContext} check permissions. If the user has the required permission it executes the mehtod,
	 * otherwise throws an exception. Returns what is returned from the intercepted method. If the method's return type
	 * is {@code void} returns {@code null}.
	 * 
	 * @param ic
	 *            the {@code InvocationContext} in which the method is being called
	 * @return what is returned from the intercepted method. If the method's return type is {@code void} returns
	 *         {@code null}
	 * @throws Exception
	 *             if there is an error during the permission check or during the method's processing
	 */
	@AroundInvoke
	public Object manage(final InvocationContext ic) throws Exception {
		String resource = getResource(ic);
		String operation = getOperation(ic);
		String username = null;

		if (getSecurityContext().isLoggedIn()) {
			username = getUsername();
			getLogger().trace(getBundle().getString("access-checking", username, operation, resource));
		}

		if (!getSecurityContext().hasPermission(resource, operation)) {
			getLogger().error(getBundle().getString("access-denied", username, operation, resource));
			throw new AuthorizationException(getBundle().getString("access-denied-ui", resource, operation));
		}

		getLogger().debug(getBundle().getString("access-allowed", username, operation, resource));
		return ic.proceed();
	}

	/**
	 * Returns the id of the currently logged in user.
	 * 
	 * @return the id of the currently logged in user
	 */
	private String getUsername() {
		String username = "";
		User user = getSecurityContext().getUser();

		if (user != null && user.getId() != null) {
			username = user.getId();
		}

		return username;
	}

	/**
	 * Returns the resource defined in {@code @RequiredPermission} annotation, the name defined in {@code @Name}
	 * annotation or the class name itself
	 * 
	 * @param ic
	 *            the {@code InvocationContext} in which the method is being called
	 * @return the resource defined in {@code @RequiredPermission} annotation, the name defined in {@code @Name}
	 *         annotation or the class name itself
	 */
	private String getResource(InvocationContext ic) {
		RequiredPermission requiredPermission = ic.getMethod().getAnnotation(RequiredPermission.class);

		if (requiredPermission == null || Strings.isEmpty(requiredPermission.resource())) {
			if (ic.getTarget().getClass().getAnnotation(Name.class) == null) {
				return ic.getTarget().getClass().getSimpleName();
			} else {
				return ic.getTarget().getClass().getAnnotation(Name.class).value();
			}
		} else {
			return requiredPermission.resource();
		}
	}

	/**
	 * Returns the operation defined in {@code @RequiredPermission} annotation, the name defined in {@code @Name}
	 * annotation or the method's name itself
	 * 
	 * @param ic
	 *            the {@code InvocationContext} in which the method is being called
	 * @return the operation defined in {@code @RequiredPermission} annotation, the name defined in {@code @Name}
	 *         annotation or the method's name itself
	 */
	private String getOperation(InvocationContext ic) {
		RequiredPermission requiredPermission = ic.getMethod().getAnnotation(RequiredPermission.class);

		if (requiredPermission == null || Strings.isEmpty(requiredPermission.operation())) {
			if (ic.getMethod().getAnnotation(Name.class) == null) {
				return ic.getMethod().getName();
			} else {
				return ic.getMethod().getAnnotation(Name.class).value();
			}
		} else {
			return requiredPermission.operation();
		}
	}

	private SecurityContext getSecurityContext() {
		if (securityContext == null) {
			securityContext = Beans.getReference(SecurityContext.class);
		}

		return securityContext;
	}

	private static ResourceBundle getBundle() {
		if (bundle == null) {
			bundle = ResourceBundleProducer.create("demoiselle-core-bundle");
		}

		return bundle;
	}

	private static Logger getLogger() {
		if (logger == null) {
			logger = LoggerProducer.create(RequiredPermissionInterceptor.class);
		}

		return logger;
	}
}
