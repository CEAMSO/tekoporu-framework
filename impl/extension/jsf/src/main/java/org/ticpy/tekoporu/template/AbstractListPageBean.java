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

package org.ticpy.tekoporu.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;

import org.ticpy.tekoporu.pagination.Pagination;
import org.ticpy.tekoporu.pagination.PaginationContext;
import org.ticpy.tekoporu.util.Reflections;

public abstract class AbstractListPageBean<T, I> extends AbstractPageBean implements ListPageBean<T, I> {

	private static final long serialVersionUID = 1L;

	private List<T> resultList;

	private transient DataModel<T> dataModel;

	private Map<I, Boolean> selection = new HashMap<I, Boolean>();

	@Inject
	private PaginationContext paginationContext;

	public void clear() {
		this.dataModel = null;
		this.resultList = null;
	}

	private Class<T> beanClass;

	protected Class<T> getBeanClass() {
		if (this.beanClass == null) {
			this.beanClass = Reflections.getGenericTypeArgument(this.getClass(), 0);
		}

		return this.beanClass;
	}

	@Override
	public DataModel<T> getDataModel() {
		if (this.dataModel == null) {
			this.dataModel = new ListDataModel<T>(this.getResultList());
		}

		return this.dataModel;
	}

	@Override
	public List<T> getResultList() {
		if (this.resultList == null) {
			this.resultList = handleResultList();
		}

		return this.resultList;
	}

	protected abstract List<T> handleResultList();

	@Override
	public String list() {
		clear();
		return getCurrentView();
	}

	public Map<I, Boolean> getSelection() {
		return selection;
	}

	public void setSelection(Map<I, Boolean> selection) {
		this.selection = selection;
	}

	public void clearSelection() {
		this.selection = new HashMap<I, Boolean>();
	}

	public List<I> getSelectedList() {
		List<I> selectedList = new ArrayList<I>();
		Iterator<I> iter = getSelection().keySet().iterator();
		while (iter.hasNext()) {
			I id = iter.next();
			if (getSelection().get(id)) {
				selectedList.add(id);
			}
		}
		return selectedList;
	}

	public Pagination getPagination() {
		return paginationContext.getPagination(getBeanClass(), true);
	}
}
