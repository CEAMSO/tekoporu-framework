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

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.ticpy.tekoporu.internal.bootstrap.TransactionBootstrap;
import org.ticpy.tekoporu.internal.configuration.TransactionConfig;
import org.ticpy.tekoporu.transaction.Transaction;
import org.ticpy.tekoporu.transaction.TransactionContext;
import org.ticpy.tekoporu.util.Beans;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Beans.class,StrategySelector.class})
public class TransactionContextImplTest {

	private TransactionContext context;	
	private Transaction transaction;
	
	@Test
	public void testGetTransactionNull() {
		context = new TransactionContextImpl();
		
		Class<? extends Transaction> cache = TransactionImpl.class;					
		
		List<Class<? extends Transaction>> cacheList = new ArrayList<Class<? extends Transaction>>();
		cacheList.add(cache);
		
		TransactionBootstrap bootstrap = PowerMock.createMock(TransactionBootstrap.class);
		TransactionConfig config = PowerMock.createMock(TransactionConfig.class);
		
		mockStatic(Beans.class); 
		expect(Beans.getReference(TransactionBootstrap.class)).andReturn(bootstrap).anyTimes();
		expect(Beans.getReference(TransactionConfig.class)).andReturn(config);
		expect(config.getTransactionClass()).andReturn(null).anyTimes();	
		expect(bootstrap.getCache()).andReturn(cacheList);
		expect(Beans.getReference(TransactionImpl.class)).andReturn(new TransactionImpl());
		
		replayAll(Beans.class);
		
		transaction = context.getCurrentTransaction();
		Assert.assertEquals(transaction.getClass(),TransactionImpl.class);
	}
	
	class TransactionImpl implements Transaction{
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public boolean isActive() {
			return false;
		}
		
		@Override
		public boolean isMarkedRollback() {
			return false;
		}
		
		@Override
		public void begin() {
		}
		
		@Override
		public void commit() {
		}
		
		@Override
		public void rollback() {
		}
		
		@Override
		public void setRollbackOnly() {
		}
	}
	
}
