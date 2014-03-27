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

package org.ticpy.tekoporu.internal.bootstrap;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.util.List;
import java.util.Locale;

import javax.enterprise.inject.spi.AfterBeanDiscovery;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import org.ticpy.tekoporu.internal.context.Contexts;
import org.ticpy.tekoporu.internal.context.CustomContext;
import org.ticpy.tekoporu.lifecycle.AfterShutdownProccess;
import org.ticpy.tekoporu.util.Beans;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Beans.class, Contexts.class })
public class SeBootstrapTest {
	
	private SeBootstrap seBootstrap;
	
	private AfterBeanDiscovery event;
	
	@Before
	public void before() {
		event = createMock(AfterBeanDiscovery.class);
		mockStatic(Beans.class);
		expect(Beans.getReference(Locale.class)).andReturn(Locale.getDefault()).anyTimes();
		replay(Beans.class);
		seBootstrap = new SeBootstrap();
	}
	
	@Test
	public void testStoreContext() {
		seBootstrap.storeContexts(event);
		replay(event);
		
		Assert.assertEquals(event, Whitebox.getInternalState(seBootstrap, "afterBeanDiscoveryEvent"));
		List<CustomContext> context = Whitebox.getInternalState(seBootstrap, "tempContexts");
		Assert.assertEquals(4, context.size());
		verifyAll();
	}
	
	@Test
	public void testRemoveContexts() {
		seBootstrap.storeContexts(event);
		
		AfterShutdownProccess afterShutdownProccess = createMock(AfterShutdownProccess.class);
		replay(event, afterShutdownProccess);
		seBootstrap.removeContexts(afterShutdownProccess);

		verifyAll();
	}
}
