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

import ${package}.domain.Rol;
import ${package}.domain.Usuario;

import org.ticpy.tekoporu.template.JPACrud;

public class RolDAO extends JPACrud<Rol, Long>{

private static final long serialVersionUID = 1L;
	
	@Inject
	@SuppressWarnings("unused")
	private Logger logger;
	
	@Inject
	private EntityManager em;
	
	@Inject	
	private UsuarioDAO usuarioDAO;
	
	@SuppressWarnings("unchecked")
	public List<Rol> findPage(int pageSize, int first, String sortField, boolean sortOrderAsc) {
		
		Session session = (Session) em.getDelegate();
		Criteria criteria = session.createCriteria(Rol.class);
		
		//add order by
		Order order = Order.asc(sortField);
		if(!sortOrderAsc) 
			order = Order.desc(sortField);
		criteria.addOrder(order);

		//add limit, offset
		criteria.setFirstResult(first);
		criteria.setMaxResults(pageSize);
		
		return criteria.list();

	}
	
	public int count() {
		Query q = em.createQuery("select count(*) from Rol r");
		return ((Long) q.getSingleResult()).intValue();
		
	}
	
	public List<Rol> findAllOrderBy(String sorters) {
		return findByJPQL("select this from " + getBeanClass().getSimpleName()
				+ " this " + " order by " + sorters);
	}
	
	public Rol findById(Long id) {
		Rol r = new Rol();
		r.setId(id);
		//r.setActivo(true);

		List<Rol> result = findByExample(r);
		if (result != null && result.size() == 1) {
			return result.get(0);
		} else {
			return null;
		}
	}
	
	public List<Rol> findUnAssignedByUser(Long userId) {
		setPaginated(false);
		List<Rol> roles  = findAll();
		
		List<Rol> result = new ArrayList<Rol>();
		Usuario user = usuarioDAO.findById(userId);
		
		for (Rol r : roles) {
			if (!r.getUsuarios().contains(user)) {
				result.add(r);
			}
		}
		
		return result;
	}
	

}

