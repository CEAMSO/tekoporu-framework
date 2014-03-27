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

package org.ticpy.tekoporu.internal.configuration;

import java.io.Serializable;

import org.ticpy.tekoporu.configuration.Configuration;
import org.ticpy.tekoporu.security.Authenticator;
import org.ticpy.tekoporu.security.Authorizer;

@Configuration(prefix = "frameworkdemoiselle.security")
public class SecurityConfigImpl implements Serializable, SecurityConfig {

	private static final long serialVersionUID = 1L;

	private boolean enabled = true;

	private Class<? extends Authenticator> authenticatorClass;

	private Class<? extends Authorizer> authorizerClass;

	/*
	 * (non-Javadoc)
	 * @see org.ticpy.tekoporu.security.SecurityConfig#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	/*
	 * (non-Javadoc)
	 * @see org.ticpy.tekoporu.security.SecurityConfig#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/*
	 * (non-Javadoc)
	 * @see org.ticpy.tekoporu.security.SecurityConfig#getAuthenticatorClass()
	 */
	@Override
	public Class<? extends Authenticator> getAuthenticatorClass() {
		return this.authenticatorClass;
	}

	/*
	 * (non-Javadoc)
	 * @see org.ticpy.tekoporu.security.SecurityConfig#setAuthenticatorClass(java.lang.Class)
	 */
	@Override
	public void setAuthenticatorClass(Class<? extends Authenticator> authenticatorClass) {
		this.authenticatorClass = authenticatorClass;
	}

	/*
	 * (non-Javadoc)
	 * @see org.ticpy.tekoporu.security.SecurityConfig#getAuthorizerClass()
	 */
	@Override
	public Class<? extends Authorizer> getAuthorizerClass() {
		return this.authorizerClass;
	}

	/*
	 * (non-Javadoc)
	 * @see org.ticpy.tekoporu.security.SecurityConfig#setAuthorizerClass(java.lang.Class)
	 */
	@Override
	public void setAuthorizerClass(Class<? extends Authorizer> authorizerClass) {
		this.authorizerClass = authorizerClass;
	}
}
