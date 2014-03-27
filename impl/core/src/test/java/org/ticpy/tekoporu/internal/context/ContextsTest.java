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

package org.ticpy.tekoporu.internal.context;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.AfterBeanDiscovery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.ticpy.tekoporu.annotation.ViewScoped;
import org.ticpy.tekoporu.util.Beans;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Beans.class)
public class ContextsTest {

	private AfterBeanDiscovery event;

	@Before
	public void setUp() throws Exception {
		Contexts.clear();
		mockStatic(Beans.class);

		expect(Beans.getReference(Locale.class)).andReturn(Locale.getDefault());

		replay(Beans.class);
	}

	@Test
	public void testRemovingInexistentContext() {
		Contexts.remove(new ThreadLocalContext(SessionScoped.class));
	}

	@Test
	public void testRemovingLastInactiveContext() {
		ThreadLocalContext context1 = new ThreadLocalContext(RequestScoped.class);
		ThreadLocalContext context2 = new ThreadLocalContext(RequestScoped.class);
		ThreadLocalContext context3 = new ThreadLocalContext(RequestScoped.class);

		Contexts.add(context1, event);
		Contexts.add(context2, event);
		Contexts.add(context3, event);
		Contexts.remove(context3);
		assertFalse(Contexts.getInactiveContexts().contains(context3));
	}

	@Test
	public void testRemovingActiveContextAndActivatingInactiveContext() {
		ThreadLocalContext context1 = new ThreadLocalContext(SessionScoped.class);
		ThreadLocalContext context2 = new ThreadLocalContext(SessionScoped.class);
		ThreadLocalContext context3 = new ThreadLocalContext(SessionScoped.class);

		Contexts.add(context1, event);
		Contexts.add(context2, event);
		Contexts.add(context3, event);
		assertTrue(context1.isActive());
		assertFalse(context2.isActive());
		assertFalse(context3.isActive());

		Contexts.remove(context1);
		assertTrue(context2.isActive());
		assertFalse(context3.isActive());

		Contexts.remove(context2);
		assertTrue(context3.isActive());
	}

	@Test
	public void testRemovingActiveContext() {
		ThreadLocalContext context = new ThreadLocalContext(SessionScoped.class);

		Contexts.add(context, event);
		Contexts.remove(context);
		assertEquals(0, Contexts.getActiveContexts().size());
	}

	@Test
	public void testRemovingInactiveContext() {
		ThreadLocalContext context = new ThreadLocalContext(SessionScoped.class);

		Contexts.add(new ThreadLocalContext(SessionScoped.class), event);
		Contexts.add(context, event);
		Contexts.remove(context);
		assertEquals(0, Contexts.getInactiveContexts().size());
	}

	@Test
	public void testClear() {
		List<ThreadLocalContext> list = new ArrayList<ThreadLocalContext>();

		list.add(new ThreadLocalContext(SessionScoped.class));
		list.add(new ThreadLocalContext(SessionScoped.class));
		list.add(new ThreadLocalContext(ApplicationScoped.class));

		for (ThreadLocalContext context : list) {
			Contexts.add(context, event);
		}

		Contexts.clear();
		assertEquals(0, Contexts.getActiveContexts().size());
		assertEquals(0, Contexts.getInactiveContexts().size());

		for (ThreadLocalContext context : list) {
			assertFalse(context.isActive());
		}
	}

	@Test
	public void testAdd() {
		Contexts.add(new ThreadLocalContext(SessionScoped.class), event);
		assertEquals(1, Contexts.getActiveContexts().size());
	}

	@Test
	public void testAddingRepeatedScopeType() {
		Contexts.add(new ThreadLocalContext(SessionScoped.class), event);
		assertEquals(1, Contexts.getActiveContexts().size());
		assertEquals(0, Contexts.getInactiveContexts().size());

		Contexts.add(new ThreadLocalContext(SessionScoped.class), event);
		assertEquals(1, Contexts.getActiveContexts().size());
		assertEquals(1, Contexts.getInactiveContexts().size());
	}

	@Test
	public void testAddingRepeatedScopeInstance() {
		ThreadLocalContext context1 = new ThreadLocalContext(SessionScoped.class);
		ThreadLocalContext context2 = new ThreadLocalContext(SessionScoped.class);

		Contexts.add(context1, event);
		Contexts.add(context2, event);

		assertTrue(context1.isActive());
		assertFalse(context2.isActive());

		assertEquals(1, Contexts.getActiveContexts().size());
		assertEquals(1, Contexts.getInactiveContexts().size());
	}

	@Test
	public void testIsActive() {
		ThreadLocalContext context = new ThreadLocalContext(SessionScoped.class);

		Contexts.add(context, event);
		assertTrue(context.isActive());
	}

	@Test
	public void testIsInactive() {
		ThreadLocalContext context = new ThreadLocalContext(ViewScoped.class);

		Contexts.add(new ThreadLocalContext(ViewScoped.class), event);
		Contexts.add(context, event);
		assertFalse(context.isActive());
	}

	@Test
	public void testAddWithEventNotNull() {
		event = createMock(AfterBeanDiscovery.class);
		ThreadLocalContext context = new ThreadLocalContext(SessionScoped.class);
		event.addContext(context);
		expectLastCall();
		replay(event);

		Contexts.add(context, event);
		assertEquals(1, Contexts.getActiveContexts().size());
	}
}
