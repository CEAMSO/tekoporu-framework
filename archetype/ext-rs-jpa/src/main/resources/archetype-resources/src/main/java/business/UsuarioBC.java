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


package ${package}.business;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.ticpy.tekoporu.lifecycle.Startup;
import org.ticpy.tekoporu.template.PaginatedDelegateCrud;
import org.ticpy.tekoporu.transaction.Transactional;

import ${package}.domain.Bookmark;
import ${package}.domain.Permiso;
import ${package}.domain.Rol;
import ${package}.domain.Usuario;
import ${package}.persistence.PermisoDAO;
import ${package}.persistence.RolDAO;
import ${package}.persistence.UsuarioDAO;

public class UsuarioBC extends PaginatedDelegateCrud<Usuario, Long, UsuarioDAO> {
	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioDAO usuarioDAO;

	@Inject
	private RolDAO rolDAO;

	@Inject
	private PermisoDAO permisoDAO;

	@Startup
	@Transactional
	public void load() {
		if (findAll().isEmpty()) {

			Permiso pBookmarkRead = new Permiso("bookmark:read");
			permisoDAO.insert(pBookmarkRead);
			Permiso pBookmarkCreate = new Permiso("bookmark:create");
			permisoDAO.insert(pBookmarkCreate);
			Permiso pBookmarkUpdate = new Permiso("bookmark:update");
			permisoDAO.insert(pBookmarkUpdate);
			Permiso pBookmarkDelete = new Permiso("bookmark:destroy");
			permisoDAO.insert(pBookmarkDelete);
			Permiso pBookmarkAll = new Permiso("bookmark:*");
			permisoDAO.insert(pBookmarkAll);
			Permiso pUserAll = new Permiso("user:*");
			permisoDAO.insert(pUserAll);
			Permiso pRolAll = new Permiso("rol:*");
			permisoDAO.insert(pRolAll);
			Permiso pPermisoAll = new Permiso("permiso:*");
			permisoDAO.insert(pPermisoAll);

			// permisos faltantes no asignados
			permisoDAO.insert(new Permiso("user:read"));
			permisoDAO.insert(new Permiso("user:create"));
			permisoDAO.insert(new Permiso("user:update"));
			permisoDAO.insert(new Permiso("user:destroy"));

			permisoDAO.insert(new Permiso("rol:read"));
			permisoDAO.insert(new Permiso("rol:create"));
			permisoDAO.insert(new Permiso("rol:update"));
			permisoDAO.insert(new Permiso("rol:destroy"));
			
			permisoDAO.insert(new Permiso("permiso:read"));
			permisoDAO.insert(new Permiso("permiso:create"));
			permisoDAO.insert(new Permiso("permiso:update"));
			permisoDAO.insert(new Permiso("permiso:destroy"));

			Rol rAdmin = new Rol("Admin");
			Set<Permiso> lAdmin = new HashSet<Permiso>();
			lAdmin.add(pPermisoAll);
			lAdmin.add(pRolAll);
			lAdmin.add(pUserAll);
			lAdmin.add(pBookmarkAll);
			rAdmin.setPermisos(lAdmin);
			rolDAO.insert(rAdmin);

			Rol rUser = new Rol("User");
			Set<Permiso> lUser = new HashSet<Permiso>();
			lUser.add(pBookmarkAll);
			rUser.setPermisos(lUser);
			rolDAO.insert(rUser);

			Rol rUser2 = new Rol("User Moderator");
			Set<Permiso> lUser2 = new HashSet<Permiso>();
			lUser2.add(pBookmarkRead);
			lUser2.add(pBookmarkUpdate);
			rUser2.setPermisos(lUser2);
			rolDAO.insert(rUser2);

			Usuario uAdmin = new Usuario(true, "Administrator",
					"admin@admin.com", "Administrator",
					new Sha256Hash("admin").toHex(), "5441345", "admin");
			Set<Rol> rolesAdmin = new HashSet<Rol>();
			rolesAdmin.add(rAdmin);
			uAdmin.setRoles(rolesAdmin);
			insert(uAdmin);

			Usuario uUser1 = new Usuario(true, "User", "user@user.com", "User",
					new Sha256Hash("user").toHex(), "787454", "user");
			Set<Rol> rolesUser1 = new HashSet<Rol>();
			rolesUser1.add(rUser);
			uUser1.setRoles(rolesUser1);
			insert(uUser1);

			Usuario uUser2 = new Usuario(true, "User Mod", "usermod@user.com",
					"User Mod", new Sha256Hash("user2").toHex(), "787454",
					"user2");
			Set<Rol> rolesUser2 = new HashSet<Rol>();
			rolesUser2.add(rUser2);
			uUser2.setRoles(rolesUser2);
			insert(uUser2);

		}

	}

	public List<Usuario> listarUsuarios() {
		return usuarioDAO.find();
	}

	public List<Usuario> findPage(int pageSize, int first, String sortField,
			boolean sortOrderAsc) {
		return usuarioDAO.findPage(pageSize, first, sortField, sortOrderAsc);
	}

	public int count() {
		return usuarioDAO.count();
	}

	public List<Usuario> findAllOrderBy(String sorters) {
		return usuarioDAO.findAllOrderBy(sorters);
	}
	
	public Usuario findById(Long id){
		return usuarioDAO.findById(id);
	}
}
