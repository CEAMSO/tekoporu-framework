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

import static org.ticpy.tekoporu.internal.implementation.StrategySelector.CORE_PRIORITY;
import org.ticpy.tekoporu.DemoiselleException;
import org.ticpy.tekoporu.annotation.Priority;
import org.ticpy.tekoporu.internal.producer.ResourceBundleProducer;
import org.ticpy.tekoporu.security.Authenticator;
import org.ticpy.tekoporu.security.SecurityContext;
import org.ticpy.tekoporu.security.User;
import org.ticpy.tekoporu.util.ResourceBundle;

/**
 * Authenticator that actually does nothing but raise exceptions.
 * 
 * @author SERPRO
 * @see Authenticator
 */
@Priority(CORE_PRIORITY)
public class DefaultAuthenticator implements Authenticator {

	private static final long serialVersionUID = 1L;

	private static ResourceBundle bundle;

	/**
	 * @see org.ticpy.tekoporu.security.Authenticator#authenticate()
	 */
	@Override
	public boolean authenticate() {
		throw getException();
	}

	/**
	 * @see org.ticpy.tekoporu.security.Authenticator#unAuthenticate()
	 */
	@Override
	public void unAuthenticate() {
		throw getException();
	}

	/**
	 * @see org.ticpy.tekoporu.security.Authenticator#getUser()
	 */
	@Override
	public User getUser() {
		throw getException();
	}

	private DemoiselleException getException() {
		return new DemoiselleException(getBundle().getString("authenticator-not-defined",
				SecurityContext.class.getSimpleName()));
	}

	private static ResourceBundle getBundle() {
		if (bundle == null) {
			bundle = ResourceBundleProducer.create("demoiselle-core-bundle");
		}

		return bundle;
	}
}
