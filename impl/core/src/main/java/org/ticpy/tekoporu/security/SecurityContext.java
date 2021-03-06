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

package org.ticpy.tekoporu.security;

import java.io.Serializable;


/**
 * Structure used to handle both authentication and authorizations mechanisms.
 * 
 * @author SERPRO
 */
public interface SecurityContext extends Serializable {

	/**
	 * Executes the login of a user to the application.
	 */
	void login();

	/**
	 * Executes the logout of a user.
	 * 
	 * @throws NotLoggedInException
	 *             if there is no user logged in a specific session
	 */
	void logout() throws NotLoggedInException;

	/**
	 * Checks if a specific user is logged in.
	 * 
	 * @return {@code true} if the user is logged in
	 */
	boolean isLoggedIn();

	/**
	 * Checks if the logged user has permission to execute an specific operation on a specific resource.
	 * 
	 * @param resource
	 *            resource to be checked
	 * @param operation
	 *            operation to be checked
	 * @return {@code true} if the user has the permission
	 * @throws NotLoggedInException
	 *             if there is no user logged in a specific session.
	 */
	boolean hasPermission(String resource, String operation) throws NotLoggedInException;

	/**
	 * Checks if the logged user has an specific role
	 * 
	 * @param role
	 *            role to be checked
	 * @return {@code true} if the user has the role
	 * @throws NotLoggedInException
	 *             if there is no user logged in a specific session.
	 */
	boolean hasRole(String role) throws NotLoggedInException;

	/**
	 * Return the user logged in the session.
	 * 
	 * @return the user logged in a specific session. If there is no active session returns {@code null}
	 */
	User getUser();
}
