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

import static org.ticpy.tekoporu.internal.implementation.StrategySelector.EXTENSIONS_L1_PRIORITY;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.ticpy.tekoporu.annotation.Priority;
import org.ticpy.tekoporu.internal.producer.EntityManagerProducer;
import org.ticpy.tekoporu.util.Beans;

/**
 * Represents the strategy destinated to manage JPA transactions.
 * 
 * @author SERPRO
 * @see Transaction
 */
@Priority(EXTENSIONS_L1_PRIORITY)
public class JPATransaction implements Transaction {

	private static final long serialVersionUID = 1L;

	private EntityManagerProducer producer;

	private EntityManagerProducer getProducer() {
		if (producer == null) {
			producer = Beans.getReference(EntityManagerProducer.class);
		}

		return producer;
	}

	public Collection<EntityManager> getDelegate() {
		return getProducer().getCache().values();
	}

	@Override
	public void begin() {
		EntityTransaction transaction;
		for (EntityManager entityManager : getDelegate()) {
			transaction = entityManager.getTransaction();

			if (!transaction.isActive()) {
				transaction.begin();
			}
		}
	}

	@Override
	public void commit() {
		EntityTransaction transaction;
		for (EntityManager entityManager : getDelegate()) {
			transaction = entityManager.getTransaction();

			if (transaction.isActive()) {
				transaction.commit();
			}
		}
	}

	@Override
	public void rollback() {
		EntityTransaction transaction;
		for (EntityManager entityManager : getDelegate()) {
			transaction = entityManager.getTransaction();

			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
	}

	@Override
	public void setRollbackOnly() {
		EntityTransaction transaction;
		for (EntityManager entityManager : getDelegate()) {
			transaction = entityManager.getTransaction();

			if (transaction.isActive()) {
				transaction.setRollbackOnly();
			}
		}
	}

	@Override
	public boolean isActive() {
		boolean active = false;

		for (EntityManager entityManager : getDelegate()) {
			if (entityManager.getTransaction().isActive()) {
				active = true;
				break;
			}
		}

		return active;
	}

	@Override
	public boolean isMarkedRollback() {
		boolean rollbackOnly = false;

		EntityTransaction transaction;
		for (EntityManager entityManager : getDelegate()) {
			transaction = entityManager.getTransaction();

			if (transaction.isActive() && transaction.getRollbackOnly()) {
				rollbackOnly = true;
				break;
			}
		}

		return rollbackOnly;
	}
}
