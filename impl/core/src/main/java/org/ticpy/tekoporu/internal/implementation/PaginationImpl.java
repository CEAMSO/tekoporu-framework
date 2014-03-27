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

package org.ticpy.tekoporu.internal.implementation;

import java.io.Serializable;

import org.ticpy.tekoporu.pagination.Pagination;
import org.ticpy.tekoporu.util.Strings;

/**
 * Structure used to handle pagination of data results on both <i>backend</i> (i.e., persistence) and <i>frontend</i>
 * (i.e., presentation) layers in the application.
 * <p>
 * Internally, it stores the current page index on {@code currentPage} variable, the amount of records in a single page
 * on {@code pageSize}, and the total number of pages in {@code totalPages}.
 * 
 * @author SERPRO
 * @see Pagination
 */
public class PaginationImpl implements Serializable, Pagination {

	private static final long serialVersionUID = 1L;

	private int currentPage;

	private int pageSize;

	private int totalResults;

	private int totalPages;

	public PaginationImpl() {
		pageSize = 0;
		totalResults = 0;
		reset();
	}

	private void reset() {
		currentPage = 0;
		totalPages = 0;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	private void setTotalPages(int totalPages) {
		validateNegativeValue(totalPages);
		this.totalPages = totalPages;

		if (totalPages == 0) {
			reset();
		} else if (getCurrentPage() >= totalPages) {
			setCurrentPage(totalPages - 1);
		}
	}

	private void validateNegativeValue(int input) throws IndexOutOfBoundsException {
		if (input < 0) {
			throw new IndexOutOfBoundsException("colocar mensagem");
		}
	}

	private void validateCurrentPage(int currentPage) throws IndexOutOfBoundsException {
		if (currentPage >= this.totalPages) {
			if (this.totalPages > 0) {
				throw new IndexOutOfBoundsException("colocar mensagem");
			}
		}
	}

	public void setCurrentPage(int currentPage) {
		validateNegativeValue(currentPage);
		validateCurrentPage(currentPage);
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(int totalResults) {
		validateNegativeValue(totalResults);
		this.totalResults = totalResults;

		if (totalResults > 0) {
			setTotalPages();
		} else {
			reset();
		}
	}

	private void setTotalPages() {
		if (totalResults > 0) {
			setTotalPages((int) Math.ceil(totalResults * 1d / getPageSize()));
		} else {
			setTotalPages(0);
		}
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getFirstResult() {
		return getCurrentPage() * getPageSize();
	}

	public void setPageSize(int pageSize) {
		validateNegativeValue(pageSize);
		this.pageSize = pageSize;

		if (pageSize > 0) {
			setTotalPages();
		} else {
			reset();
		}
	}

	private void validateFirstResult(int firstResult) throws IndexOutOfBoundsException {
		if (firstResult >= this.totalResults) {
			if (this.totalResults > 0) {
				throw new IndexOutOfBoundsException("colocar mensagem");
			}
		}
	}

	public void setFirstResult(int firstResult) {
		validateNegativeValue(firstResult);
		validateFirstResult(firstResult);

		if (firstResult > 0) {
			setCurrentPage(firstResult / pageSize);
		} else {
			setCurrentPage(0);
		}
	}

	@Override
	public String toString() {
		return Strings.toString(this);
	}
	
	public void setPage(int currentPage, int pageSize) {
		this.setPageSize(pageSize);
		this.setCurrentPage(currentPage);
	}
}