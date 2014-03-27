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

import java.util.List;

import javax.inject.Inject;

import org.ticpy.tekoporu.template.PaginatedDelegateCrud;

import ${package}.domain.Permiso;
import ${package}.domain.Rol;
import ${package}.persistence.PermisoDAO;

public class PermisoBC extends PaginatedDelegateCrud<Permiso, Long, PermisoDAO> {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PermisoDAO permisoDAO;
	
	public int count() {
		return permisoDAO.count();
	}
	
	public List<String> findResources() {
		return permisoDAO.findResources();
	}
	
	public List<String> findOperations() {
		return permisoDAO.findOperations();
	}
	
	public List<String> findAllKeys() {
		return permisoDAO.findAllKeys();
	}
	
	public List<Permiso> findAllOrderBy(String sorters) {
		return permisoDAO.findAllOrderBy(sorters);
	}
	
	public Permiso findById(Long id){
		return permisoDAO.findById(id);
	}
	
	public List<Permiso> findUnAssignedByRol(Long rolId) {
		return permisoDAO.findUnAssignedByRol(rolId);
	}
}
