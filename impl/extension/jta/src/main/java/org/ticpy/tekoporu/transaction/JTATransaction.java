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

import static org.ticpy.tekoporu.internal.implementation.StrategySelector.EXTENSIONS_L2_PRIORITY;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.ticpy.tekoporu.annotation.Priority;
import org.ticpy.tekoporu.util.Beans;

@Priority(EXTENSIONS_L2_PRIORITY)
public class JTATransaction implements Transaction {

	private static final long serialVersionUID = 1L;

	private UserTransaction delegate;

	public UserTransaction getDelegate() {

		if (delegate == null) {
			delegate = Beans.getReference(UserTransaction.class);
		}

		return delegate;
	}

	@Override
	public boolean isActive() {
		try {
			return getDelegate().getStatus() == Status.STATUS_ACTIVE || isMarkedRollback();

		} catch (SystemException cause) {
			throw new TransactionException(cause);
		}
	}

	@Override
	public boolean isMarkedRollback() {
		try {
			return getDelegate().getStatus() == Status.STATUS_MARKED_ROLLBACK;

		} catch (SystemException cause) {
			throw new TransactionException(cause);
		}
	}

	@Override
	public void begin() {
		try {
			getDelegate().begin();

		} catch (Exception cause) {
			throw new TransactionException(cause);
		}
	}

	@Override
	public void commit() {
		try {
			getDelegate().commit();

		} catch (Exception cause) {
			throw new TransactionException(cause);
		}
	}

	@Override
	public void rollback() {
		try {
			getDelegate().rollback();

		} catch (SystemException cause) {
			throw new TransactionException(cause);
		}
	}

	@Override
	public void setRollbackOnly() {
		try {
			getDelegate().setRollbackOnly();

		} catch (SystemException cause) {
			throw new TransactionException(cause);
		}
	}
}
