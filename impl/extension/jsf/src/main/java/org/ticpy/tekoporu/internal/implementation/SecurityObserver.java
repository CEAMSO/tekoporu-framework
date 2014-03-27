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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;

import org.ticpy.tekoporu.configuration.ConfigurationException;
import org.ticpy.tekoporu.internal.configuration.JsfSecurityConfig;
import org.ticpy.tekoporu.security.AfterLoginSuccessful;
import org.ticpy.tekoporu.security.AfterLogoutSuccessful;
import org.ticpy.tekoporu.util.Beans;
import org.ticpy.tekoporu.util.PageNotFoundException;
import org.ticpy.tekoporu.util.Redirector;

@SessionScoped
public class SecurityObserver implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private JsfSecurityConfig config;

	private transient Map<String, Object> savedParams;

	private String savedViewId;

	@Inject
	private Logger logger;

	public SecurityObserver() {
		clear();
	}

	private Map<String, Object> getSavedParams() {
		if (this.savedParams == null) {
			this.savedParams = new HashMap<String, Object>();
		}

		return this.savedParams;
	}

	private void saveCurrentState() {
		clear();
		FacesContext facesContext = Beans.getReference(FacesContext.class);

		if (!config.getLoginPage().equals(facesContext.getViewRoot().getViewId())) {
			getSavedParams().putAll(facesContext.getExternalContext().getRequestParameterMap());
			savedViewId = facesContext.getViewRoot().getViewId();
		}
	}

	public void redirectToLoginPage() {
		saveCurrentState();

		try {
			Redirector.redirect(config.getLoginPage());

		} catch (PageNotFoundException cause) {
			// TODO Colocar a mensagem no bundle
			throw new ConfigurationException(
					"A tela de login \""
							+ cause.getViewId()
							+ "\" não foi encontrada. Caso o seu projeto possua outra, defina no arquivo de configuração a chave \""
							+ "frameworkdemoiselle.security.login.page" + "\"", cause);
		}
	}

	public void onLoginSuccessful(@Observes final AfterLoginSuccessful event) {
		boolean redirectedFromConfig = false;

		try {
			if (savedViewId != null) {
				Redirector.redirect(savedViewId, getSavedParams());

			} else if (config.isRedirectEnabled()) {
				redirectedFromConfig = true;
				Redirector.redirect(config.getRedirectAfterLogin(), getSavedParams());
			}

		} catch (PageNotFoundException cause) {
			if (redirectedFromConfig) {
				// TODO Colocar a mensagem no bundle
				throw new ConfigurationException(
						"A tela \""
								+ cause.getViewId()
								+ "\" que é invocada após o logon não foi encontrada. Caso o seu projeto possua outra, defina no arquivo de configuração a chave \""
								+ "frameworkdemoiselle.security.redirect.after.login" + "\"", cause);
			} else {
				throw cause;
			}

		} finally {
			clear();
		}
	}

	public void onLogoutSuccessful(@Observes final AfterLogoutSuccessful event) {
		try {
			if (config.isRedirectEnabled()) {
				Redirector.redirect(config.getRedirectAfterLogout());
			}

		} catch (PageNotFoundException cause) {
			// TODO Colocar a mensagem no bundle
			throw new ConfigurationException(
					"A tela \""
							+ cause.getViewId()
							+ "\" que é invocada após o logout não foi encontrada. Caso o seu projeto possua outra, defina no arquivo de configuração a chave \""
							+ "frameworkdemoiselle.security.redirect.after.logout" + "\"", cause);

		} finally {
			try {
				Beans.getReference(HttpSession.class).invalidate();
			} catch (IllegalStateException e) {
				logger.debug("Esta sessão já foi invalidada.");
			}
		}
	}

	private void clear() {
		savedViewId = null;
		getSavedParams().clear();
	}
}
