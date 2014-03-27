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

package org.ticpy.tekoporu.internal.implementation;

import javax.inject.Named;

import org.ticpy.tekoporu.internal.bootstrap.AuthenticatorBootstrap;
import org.ticpy.tekoporu.internal.bootstrap.AuthorizerBootstrap;
import org.ticpy.tekoporu.internal.configuration.SecurityConfig;
import org.ticpy.tekoporu.internal.configuration.SecurityConfigImpl;
import org.ticpy.tekoporu.internal.producer.ResourceBundleProducer;
import org.ticpy.tekoporu.security.AfterLoginSuccessful;
import org.ticpy.tekoporu.security.AfterLogoutSuccessful;
import org.ticpy.tekoporu.security.Authenticator;
import org.ticpy.tekoporu.security.Authorizer;
import org.ticpy.tekoporu.security.NotLoggedInException;
import org.ticpy.tekoporu.security.SecurityContext;
import org.ticpy.tekoporu.security.User;
import org.ticpy.tekoporu.util.Beans;
import org.ticpy.tekoporu.util.ResourceBundle;

/**
 * This is the default implementation of {@link SecurityContext} interface.
 * 
 * @author SERPRO
 */
@Named("securityContext")
public class SecurityContextImpl implements SecurityContext {

	private static final long serialVersionUID = 1L;

	private Authenticator authenticator;

	private Authorizer authorizer;

	private Authenticator getAuthenticator() {
		if (this.authenticator == null) {
			AuthenticatorBootstrap bootstrap = Beans.getReference(AuthenticatorBootstrap.class);
			Class<? extends Authenticator> clazz = getConfig().getAuthenticatorClass();

			if (clazz == null) {
				clazz = StrategySelector.getClass(Authenticator.class, bootstrap.getCache());
			}

			this.authenticator = Beans.getReference(clazz);
		}

		return this.authenticator;
	}

	private Authorizer getAuthorizer() {
		if (this.authorizer == null) {
			AuthorizerBootstrap bootstrap = Beans.getReference(AuthorizerBootstrap.class);
			Class<? extends Authorizer> clazz = getConfig().getAuthorizerClass();

			if (clazz == null) {
				clazz = StrategySelector.getClass(Authorizer.class, bootstrap.getCache());
			}

			this.authorizer = Beans.getReference(clazz);
		}

		return this.authorizer;
	}

	/**
	 * @see org.ticpy.tekoporu.security.SecurityContext#hasPermission(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean hasPermission(String resource, String operation) throws NotLoggedInException {
		if (getConfig().isEnabled()) {
			checkLoggedIn();
			return getAuthorizer().hasPermission(resource, operation);

		} else {
			return true;
		}
	}

	/**
	 * @see org.ticpy.tekoporu.security.SecurityContext#hasRole(java.lang.String)
	 */
	@Override
	public boolean hasRole(String role) throws NotLoggedInException {
		if (getConfig().isEnabled()) {
			checkLoggedIn();
			return getAuthorizer().hasRole(role);

		} else {
			return true;
		}
	}

	/**
	 * @see org.ticpy.tekoporu.security.SecurityContext#isLoggedIn()
	 */
	@Override
	public boolean isLoggedIn() {
		if (getConfig().isEnabled()) {
			return getUser() != null;
		} else {
			return true;
		}
	}

	/**
	 * @see org.ticpy.tekoporu.security.SecurityContext#login()
	 */
	@Override
	public void login() {
		if (getConfig().isEnabled() && getAuthenticator().authenticate()) {
			Beans.getBeanManager().fireEvent(new AfterLoginSuccessful() {

				private static final long serialVersionUID = 1L;

			});
		}
	}

	/**
	 * @see org.ticpy.tekoporu.security.SecurityContext#logout()
	 */
	@Override
	public void logout() throws NotLoggedInException {
		if (getConfig().isEnabled()) {
			checkLoggedIn();
			getAuthenticator().unAuthenticate();

			Beans.getBeanManager().fireEvent(new AfterLogoutSuccessful() {

				private static final long serialVersionUID = 1L;
			});
		}
	}

	/**
	 * @see org.ticpy.tekoporu.security.SecurityContext#getUser()
	 */
	@Override
	public User getUser() {
		User user = getAuthenticator().getUser();

		if (!getConfig().isEnabled() && user == null) {
			user = new User() {

				private static final long serialVersionUID = 1L;

				@Override
				public void setAttribute(Object key, Object value) {
				}

				@Override
				public String getId() {
					return "demoiselle";
				}

				@Override
				public Object getAttribute(Object key) {
					return null;
				}
			};
		}

		return user;
	}

	private SecurityConfig getConfig() {
		return Beans.getReference(SecurityConfigImpl.class);
	}

	private void checkLoggedIn() throws NotLoggedInException {
		if (!isLoggedIn()) {
			ResourceBundle bundle = ResourceBundleProducer.create("demoiselle-core-bundle");
			throw new NotLoggedInException(bundle.getString("user-not-authenticated"));
		}
	}
}
