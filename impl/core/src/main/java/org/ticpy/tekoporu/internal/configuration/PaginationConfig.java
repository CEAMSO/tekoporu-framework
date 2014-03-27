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

package org.ticpy.tekoporu.internal.configuration;

import java.io.Serializable;

import org.ticpy.tekoporu.annotation.Name;
import org.ticpy.tekoporu.configuration.Configuration;

/**
 * The <code>PaginationConfig</code> class provides a pagination context to be used where this type of control is
 * needed. For example: in a UI as a data grid or on a search to the database.
 * 
 * @author SERPRO
 */
@Configuration(prefix = "frameworkdemoiselle.pagination")
public class PaginationConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	@Name("page.size")
	private int pageSize = 10;

	@Name("max.page.links")
	private int maxPageLinks = 5;

	/**
	 * Returns the number of rows that will be shown in a data grid.
	 * 
	 * @return the value defined for the key <i>frameworkdemoiselle.pagination.page.size</i> in the
	 *         <b>demoiselle.properties</b> file. If there is no value defined, returns the default value 10
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * Returns the maximum number of pages that will be shown in a data grid footer
	 * 
	 * @return the value defined for the key <i>frameworkdemoiselle.pagination.max.page.links</i> in the
	 *         <b>demoiselle.properties</b> file. If there is no value defined, returns the default value 5
	 */
	public int getMaxPageLinks() {
		return maxPageLinks;
	}
}
