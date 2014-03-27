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

package org.ticpy.tekoporu.internal.interceptor;

import java.io.Serializable;

import javax.enterprise.context.ContextNotActiveException;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;

import org.ticpy.tekoporu.exception.ApplicationException;
import org.ticpy.tekoporu.internal.implementation.TransactionInfo;
import org.ticpy.tekoporu.internal.producer.LoggerProducer;
import org.ticpy.tekoporu.internal.producer.ResourceBundleProducer;
import org.ticpy.tekoporu.transaction.Transaction;
import org.ticpy.tekoporu.transaction.TransactionContext;
import org.ticpy.tekoporu.transaction.Transactional;
import org.ticpy.tekoporu.util.Beans;
import org.ticpy.tekoporu.util.ResourceBundle;

@Interceptor
@Transactional
public class TransactionalInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;

	private TransactionContext transactionContext;

	private TransactionInfo transactionInfo;

	private static ResourceBundle bundle;

	private static Logger logger;

	private TransactionContext getTransactionContext() {
		if (this.transactionContext == null) {
			this.transactionContext = Beans.getReference(TransactionContext.class);
		}

		return this.transactionContext;
	}

	private TransactionInfo newTransactionInfo() {
		TransactionInfo instance;

		try {
			instance = Beans.getReference(TransactionInfo.class);

		} catch (ContextNotActiveException cause) {
			instance = new TransactionInfo() {

				private static final long serialVersionUID = 1L;

				@Override
				public boolean isOwner() {
					return false;
				}
			};
		}

		return instance;
	}

	private TransactionInfo getTransactionInfo() {
		if (this.transactionInfo == null) {
			this.transactionInfo = newTransactionInfo();
		}

		return this.transactionInfo;
	}

	@AroundInvoke
	public Object manage(final InvocationContext ic) throws Exception {
		initiate(ic);

		Object result = null;
		try {
			getLogger().debug(getBundle().getString("transactional-execution", ic.getMethod().toGenericString()));
			result = ic.proceed();

		} catch (Exception cause) {
			handleException(cause);
			throw cause;

		} finally {
			complete(ic);
		}

		return result;
	}

	private void initiate(final InvocationContext ic) {
		Transaction transaction = getTransactionContext().getCurrentTransaction();
		TransactionInfo transactionInfo = getTransactionInfo();

		if (!transaction.isActive()) {
			transaction.begin();
			transactionInfo.markAsOwner();
			getLogger().info(getBundle().getString("begin-transaction"));
		}

		transactionInfo.incrementCounter();
	}

	private void handleException(final Exception cause) {
		Transaction transaction = getTransactionContext().getCurrentTransaction();

		if (!transaction.isMarkedRollback()) {
			boolean rollback = false;
			ApplicationException annotation = cause.getClass().getAnnotation(ApplicationException.class);

			if (annotation == null || annotation.rollback()) {
				rollback = true;
			}

			if (rollback) {
				transaction.setRollbackOnly();
				getLogger().info(getBundle().getString("transaction-marked-rollback", cause.getMessage()));
			}
		}
	}

	private void complete(final InvocationContext ic) {
		Transaction transaction = getTransactionContext().getCurrentTransaction();
		TransactionInfo transactionInfo = getTransactionInfo();
		transactionInfo.decrementCounter();

		if (transactionInfo.getCounter() == 0 && transaction.isActive()) {

			if (transactionInfo.isOwner()) {
				if (transaction.isMarkedRollback()) {
					transaction.rollback();
					transactionInfo.clear();

					getLogger().info(getBundle().getString("transaction-rolledback"));

				} else {
					transaction.commit();
					transactionInfo.clear();

					getLogger().info(getBundle().getString("transaction-commited"));
				}
			}

		} else if (transactionInfo.getCounter() == 0 && !transaction.isActive()) {
			getLogger().info(getBundle().getString("transaction-already-finalized"));
		}
	}

	private static ResourceBundle getBundle() {
		if (bundle == null) {
			bundle = ResourceBundleProducer.create("demoiselle-core-bundle");
		}

		return bundle;
	}

	private static Logger getLogger() {
		if (logger == null) {
			logger = LoggerProducer.create(TransactionalInterceptor.class);
		}

		return logger;
	}
}
