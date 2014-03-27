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


package ${package}.persistence;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.ticpy.tekoporu.template.JPACrud;

import ${package}.domain.Permiso;
import ${package}.domain.Rol;
import ${package}.domain.Usuario;

public class PermisoDAO extends JPACrud<Permiso, Long>{

private static final long serialVersionUID = 1L;
	
	@Inject
	@SuppressWarnings("unused")
	private Logger logger;
	
	@Inject
	private EntityManager em;
	
	@Inject
	private RolDAO rolDAO;

	
	public int count() {
		Query q = em.createQuery("select count(*) from Permiso p");
		return ((Long) q.getSingleResult()).intValue();
		
	}
	
	public List<String> findResources() {
		Query q = em.createQuery("select distinct recurso from Permiso p");
		return q.getResultList();
	}
	
	public List<String> findOperations() {
		Query q = em.createQuery("select distinct operacion from Permiso p");
		return q.getResultList();
	}
	
	public List<String> findAllKeys() {
		Query q = em.createQuery("select distinct clave from Permiso p");
		return q.getResultList();
	}

	public Permiso findById(Long id) {
		Permiso p = new Permiso();
		p.setId(id);
		//r.setActivo(true);

		List<Permiso> result = findByExample(p);
		if (result != null && result.size() == 1) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public List<Permiso> findAllOrderBy(String sorters) {
		return findByJPQL("select this from " + getBeanClass().getSimpleName()
				+ " this " + " order by " + sorters);
	}
	
	
	public List<Permiso> findUnAssignedByRol(Long rolId) {
		setPaginated(false);
		List<Permiso> permisos  = findAll();
		
		List<Permiso> result = new ArrayList<Permiso>();
		Rol rol = rolDAO.findById(rolId);
		
		for (Permiso p : permisos) {
			if (!p.getRoles().contains(rol)) {
				result.add(p);
			}
		}
		
		return result;
	}

}

