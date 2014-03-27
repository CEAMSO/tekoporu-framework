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

package org.ticpy.tekoporu.pagination;

/**
 * Structure used to handle pagination of data results on both <i>backend</i> (i.e., persistence) and <i>frontend</i>
 * (i.e., presentation) layers in the application.
 * 
 * @author SERPRO
 */
public interface Pagination {

	/**
	 * Returns the current page.
	 */
	int getCurrentPage();

	/**
	 * Sets the current page.
	 */
	void setCurrentPage(int currentPage);

	/**
	 * Returns the page size.
	 */
	int getPageSize();

	/**
	 * Sets the page size.
	 */
	void setPageSize(int pageSize);

	/**
	 * Returns the total number of results.
	 */
	int getTotalResults();

	/**
	 * Sets the total number of results and calculates the number of pages.
	 */
	void setTotalResults(int totalResults);

	/**
	 * Returns the total number of pages.
	 */
	int getTotalPages();

	/**
	 * Returns the position for the first record according to current page and page size.
	 */
	int getFirstResult();

	/**
	 * Sets the position for the first record and hence calculates current page according to page size.
	 */
	void setFirstResult(int firstResult);
	
	
	void setPage(int currentPage, int pageSize);
	
}
