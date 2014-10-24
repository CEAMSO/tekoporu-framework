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


package ${package}.security;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.subject.Subject;

public class PermissionManager {

	private List<String> keys;
	
	public PermissionManager(List<String> keys) {
		this.keys = keys;
	}

	public Map<String, Object> getJsonPermissions(Subject subject) {		
		Map<String, Set<String>> permisosMap = new HashMap<String, Set<String>>();
		Map<String, Object> root = new HashMap<String, Object>();
		Map<String, Object> per;
		
			boolean hasAllOperationPermission = false;
			
			for (String key : keys) {
				//TODO Controlar que en la bd no hayan permisos repetidos
				String[] parts = key.split(":");
				String recurso = parts[0];
				String operacion = parts[1];
				
				Set<String> ops =  permisosMap.get(recurso);
				if (ops == null) {
					ops = new HashSet<String>();
				}
				ops.add(operacion);
				permisosMap.put(recurso, ops);
			}

			for (Map.Entry<String, Set<String>> entry : permisosMap.entrySet()) {
				per = new HashMap<String, Object>();
				
				hasAllOperationPermission = false;
				//existe un permiso de all operation
				if (entry.getValue().contains("*")) {
					entry.getValue().remove("*");
					if (subject.isPermitted(entry.getKey() + ":*")) {
						hasAllOperationPermission = true;
					}
				}

				for (String val : entry.getValue()) {
					if (hasAllOperationPermission || subject.isPermitted(entry.getKey() + ":" + val)) {
						per.put(val, true);
					} else {
						per.put(val, false);
					}
				}
				root.put(entry.getKey(), per);
			}

		return root;
	}
}
