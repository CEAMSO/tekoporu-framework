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

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;

import org.apache.shiro.crypto.hash.Sha256Hash;
import ${package}.domain.Usuario;

import org.ticpy.tekoporu.template.JPACrud;

public class UsuarioDAO extends JPACrud<Usuario, Long> {
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	@Inject
	@SuppressWarnings("unused")
	private Logger logger;

	@SuppressWarnings("unchecked")
	public List<Usuario> find() {

		Query q = em
				.createQuery("select new Usuario(u.usuarioId, u.username,u.nombre, u.apellido, u.telefono, u.email )"
						+ " from Usuario u");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> findPage(int pageSize, int first, String sortField,
			boolean sortOrderAsc) {
		String order = "asc";
		if (!sortOrderAsc)
			order = "desc";

		Query q = em
				.createQuery("select new Usuario(u.usuarioId, u.username,u.nombre, u.apellido, u.telefono, u.email )"
						+ " from Usuario u order by " + sortField + " " + order);
		q.setFirstResult(first);
		q.setMaxResults(pageSize);

		return (List<Usuario>) q.getResultList();

	}

	public int count() {
		Query q = em.createQuery("select count(*) from Usuario u");
		return ((Long) q.getSingleResult()).intValue();

	}

	public void setHashedPassword(Usuario usuario, String password) {
		usuario.setPwd(new Sha256Hash(password).toHex());
		this.update(usuario);
	}

	public List<Usuario> findAllOrderBy(String sorters) {
		return findByJPQL("select this from " + getBeanClass().getSimpleName()
				+ " this " + " order by " + sorters);
	}
	
	public Usuario findById(Long id) {
		Usuario u = new Usuario();
		u.setId(id);
		u.setActivo(true);

		List<Usuario> result = findByExample(u);
		if (result != null && result.size() == 1) {
			return result.get(0);
		} else {
			//usuario no encontrado
			return null;
		}
	}

}
