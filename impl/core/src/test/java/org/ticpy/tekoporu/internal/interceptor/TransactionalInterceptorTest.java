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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.replayAll;

import java.util.Locale;

import javax.enterprise.inject.Instance;
import javax.interceptor.InvocationContext;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.ticpy.tekoporu.DemoiselleException;
import org.ticpy.tekoporu.internal.implementation.TransactionInfo;
import org.ticpy.tekoporu.transaction.Transaction;
import org.ticpy.tekoporu.transaction.TransactionContext;
import org.ticpy.tekoporu.util.Beans;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Beans.class)
public class TransactionalInterceptorTest {

	private TransactionalInterceptor interceptor;

	private InvocationContext ic;

	private Transaction transaction;

	private TransactionContext transactionContext;

	class ClassWithMethod {

		public void method() {
			System.out.println("test method");
		}
	}

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() throws Exception {
		Instance<Transaction> transactionInstance = EasyMock.createMock(Instance.class);
		Instance<TransactionInfo> transactionInfoInstance = EasyMock.createMock(Instance.class);
		Instance<TransactionContext> transactionContextInstance = EasyMock.createMock(Instance.class);

		TransactionInfo transactionInfo = new TransactionInfo();
		transaction = EasyMock.createMock(Transaction.class);
		this.transactionContext = EasyMock.createMock(TransactionContext.class);
		this.ic = EasyMock.createMock(InvocationContext.class);

		mockStatic(Beans.class);
		expect(Beans.getReference(Locale.class)).andReturn(Locale.getDefault());
		expect(Beans.getReference(TransactionContext.class)).andReturn(transactionContext);
		expect(Beans.getReference(TransactionInfo.class)).andReturn(transactionInfo);

		expect(transactionInstance.get()).andReturn(transaction).anyTimes();
		expect(transactionContextInstance.get()).andReturn(transactionContext).anyTimes();
		expect(transactionInfoInstance.get()).andReturn(transactionInfo).anyTimes();
		expect(transactionContext.getCurrentTransaction()).andReturn(transaction).anyTimes();
		expect(this.ic.proceed()).andReturn(null);
		expect(this.ic.getMethod()).andReturn(ClassWithMethod.class.getMethod("method"));
		replayAll(Beans.class, this.ic, transactionContextInstance, transactionInfoInstance, transactionInstance,
				transactionContext);

		this.interceptor = new TransactionalInterceptor();

	}

	@Test
	public void testManageWithInativeTransactionAndTransactionInterceptorBeginAndDoNotIsMarkedRollback()
			throws Exception {
		expect(this.transaction.isActive()).andReturn(false).times(1);
		expect(this.transaction.isActive()).andReturn(true).times(2);
		expect(this.transaction.isMarkedRollback()).andReturn(false).anyTimes();
		this.transaction.begin();
		this.transaction.commit();
		replay(this.transaction);

		assertEquals(null, this.interceptor.manage(this.ic));
		verify();
	}

	@Test
	public void testManageWithInativeTransactionAndTransactionInterceptorBeginAndIsMarkedRollback() throws Exception {
		expect(this.transaction.isActive()).andReturn(false).times(1);
		expect(this.transaction.isActive()).andReturn(true).times(2);
		expect(this.transaction.isMarkedRollback()).andReturn(true).anyTimes();
		this.transaction.begin();
		this.transaction.rollback();
		replay(this.transaction);

		assertEquals(null, this.interceptor.manage(this.ic));
		verify();
	}

	@Test
	public void testManageWithAtiveTransaction() throws Exception {
		expect(this.transaction.isActive()).andReturn(true).anyTimes();
		replay(this.transaction);

		assertEquals(null, this.interceptor.manage(this.ic));
		verify();
	}

	@Test
	public void testManageWithAtiveTransactionButTheTransactionWasInative() throws Exception {
		expect(this.transaction.isActive()).andReturn(true).times(1);
		expect(this.transaction.isActive()).andReturn(false).times(2);
		replay(this.transaction);

		assertEquals(null, this.interceptor.manage(this.ic));
		verify();
	}

	@Test
	public void testManageWithAtiveTransactionAndMethodThrowExceptionAndDoNotIsMarkedRollback() throws Exception {
		expect(this.transaction.isActive()).andReturn(true).anyTimes();
		expect(this.transaction.isMarkedRollback()).andReturn(false).anyTimes();
		this.transaction.setRollbackOnly();
		replay(this.transaction);

		this.ic = EasyMock.createMock(InvocationContext.class);
		expect(this.ic.proceed()).andThrow(new DemoiselleException(""));
		expect(this.ic.getMethod()).andReturn(ClassWithMethod.class.getMethod("method"));
		replay(this.ic);

		try {
			this.interceptor.manage(this.ic);
			fail();
		} catch (DemoiselleException cause) {
			assertTrue(true);
		}
		verify();
	}

	@Test
	public void testManageWithAtiveTransactionAndMethodThrowExceptionAndIsMarkedRollback() throws Exception {
		expect(this.transaction.isActive()).andReturn(true).anyTimes();
		expect(this.transaction.isMarkedRollback()).andReturn(true).anyTimes();
		this.transaction.setRollbackOnly();
		replay(this.transaction);

		this.ic = EasyMock.createMock(InvocationContext.class);
		expect(this.ic.proceed()).andThrow(new DemoiselleException(""));
		expect(this.ic.getMethod()).andReturn(ClassWithMethod.class.getMethod("method"));
		replay(this.ic);

		try {
			this.interceptor.manage(this.ic);
			fail();
		} catch (DemoiselleException cause) {
			assertTrue(true);
		}
		verify();
	}
}
