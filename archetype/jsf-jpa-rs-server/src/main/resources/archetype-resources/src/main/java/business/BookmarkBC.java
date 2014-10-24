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

package ${package}.business;

import java.util.List;

import javax.inject.Inject;

import org.ticpy.tekoporu.pagination.Pagination;
import org.ticpy.tekoporu.stereotype.BusinessController;
import org.ticpy.tekoporu.template.PaginatedDelegateCrud;

import ${package}.persistence.BookmarkDAO;

import app_common.app_common.domain.Bookmark;

@BusinessController
public class BookmarkBC extends
		PaginatedDelegateCrud<Bookmark, Long, BookmarkDAO> {

	private static final long serialVersionUID = 1L;

	@Inject
	private BookmarkDAO bookmarkDAO;

	public List<Bookmark> findAll(String sortField, boolean sortOrder,
			Bookmark filtro, Pagination pag) {
		bookmarkDAO.setPagination(pag);
		if (filtro != null) {
			if (sortField != null) {
				return bookmarkDAO.findAll(sortField, sortOrder, filtro);
			} else {
				return bookmarkDAO.findByExample(filtro);
			}
		} else {
			if (sortField != null) {
				return bookmarkDAO.findAll(sortField, sortOrder);
			} else {
				return bookmarkDAO.findAll();
			}
		}
	}

	public Bookmark updateBookmark(Bookmark b) {
		return bookmarkDAO.updateBookmark(b);
	}
}
