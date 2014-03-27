/*
 * CEAMSO-USAID
 * Copyright (C) 2014 Governance and Democracy Program
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


package ${package}.services;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import ${package}.business.PermisoBC;
import ${package}.security.Credential;
import ${package}.security.PermissionManager;

@Path("/")
public class AuthRS {

	@Inject
	private PermisoBC permisoBC;
	
	@GET
	@Path("/islogged")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> isLogguedIn() {
		System.out.println("======== Is Logged IN");
		Map<String, Object> root = new HashMap<String, Object>();
		Subject _subject = null;
		
		boolean loggedIn = false;
		try {
			_subject = SecurityUtils.getSubject();
			loggedIn = _subject.isAuthenticated();
	
		} catch (Exception e) {
			loggedIn = false;
		}
		
		if (loggedIn) {
			PermissionManager pm = new PermissionManager(permisoBC.findAllKeys());
			root.put("success", true);
			root.put("username", _subject.getPrincipal());
			root.put("permissions", pm.getJsonPermissions(_subject));
		} else {
			root.put("success", false);
		}
		
		return root;
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> login(Credential credential) {
		System.out.println("========  LOGIN");
		Map<String, Object> root = new HashMap<String, Object>();
		Subject _subject = null;
		
		boolean failure = false;
		try {
			_subject = SecurityUtils.getSubject();
			if (_subject != null) {
				System.out.println("========  User:" + credential.getUsername());
				System.out.println("========  Password:" + credential.getPassword());
				_subject.login(new UsernamePasswordToken(credential.getUsername(), credential.getPassword()));
			}
		} catch (Exception e) {
			failure = true;
		}
		
		if (failure) {
			root.put("success", false);
		} else {
			PermissionManager pm = new PermissionManager(permisoBC.findAllKeys());
			root.put("success", true);
			root.put("username", _subject.getPrincipal());
			root.put("permissions", pm.getJsonPermissions(_subject));
		}
		
		return root;
	}

	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout_dan() {
		System.out.println("============= Logout");

		Subject _subject = SecurityUtils.getSubject();
		if (_subject != null) {
			_subject.logout();
		}
		
		return Response.ok(true).build();
	}

}
