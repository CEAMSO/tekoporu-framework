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

package org.ticpy.tekoporu.transaction;

import java.io.Serializable;

/**
 * This interface improves UserTransaction by defining additional methods to it, allowing an application to explicitly
 * manage transaction boundaries.
 * 
 * @author SERPRO
 * @see UserTransaction
 */
public interface Transaction extends Serializable {

	/**
	 * Indicates whether the given transaction is still active.
	 * 
	 * @return a boolean
	 */
	boolean isActive();

	/**
	 * Indicates whether the given transaction is already marked to be rolled back.
	 * 
	 * @return a boolean
	 */
	boolean isMarkedRollback();

	/**
	 * Create a new transaction and associate it with the current thread.
	 */
	void begin();

	/**
	 * Complete the transaction associated with the current thread. When this method completes, the thread is no longer
	 * associated with a transaction.
	 */
	void commit();

	/**
	 * Roll back the transaction associated with the current thread. When this method completes, the thread is no longer
	 * associated with a transaction.
	 */
	void rollback();

	/**
	 * Modify the transaction associated with the current thread such that the only possible outcome of the transaction
	 * is to roll back the transaction.
	 */
	void setRollbackOnly();
}
