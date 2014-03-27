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

package org.ticpy.tekoporu.util;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import javax.enterprise.inject.spi.BeanManager;
import javax.servlet.ServletContextEvent;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.ticpy.tekoporu.internal.bootstrap.ShutdownBootstrap;
import org.ticpy.tekoporu.lifecycle.AfterStartupProccess;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Beans.class)
public class ServletListenerTest {

	private ServletListener listener;

	@Test
	public void testContextInitialized() {
		ServletContextEvent event = createMock(ServletContextEvent.class);
		BeanManager beanManager = PowerMock.createMock(BeanManager.class);

		mockStatic(Beans.class);
		expect(Beans.getBeanManager()).andReturn(beanManager);
		beanManager.fireEvent(EasyMock.anyObject(AfterStartupProccess.class));
		PowerMock.expectLastCall().times(1);

		replayAll();

		listener = new ServletListener();
		listener.contextInitialized(event);

		verifyAll();
	}

	@Test
	public void testContextDestroyed() {
		ServletContextEvent event = createMock(ServletContextEvent.class);
		BeanManager beanManager = PowerMock.createMock(BeanManager.class);

		mockStatic(Beans.class);
		expect(Beans.getBeanManager()).andReturn(beanManager);
		beanManager.fireEvent(EasyMock.anyObject(ShutdownBootstrap.class));
		PowerMock.expectLastCall().times(1);

		replayAll();

		listener = new ServletListener();
		listener.contextDestroyed(event);

		verifyAll();
	}

}
