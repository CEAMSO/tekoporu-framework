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

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Scope;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Bean.class })
public class ThreadLocalContextTest {

	private ThreadLocalContext context;

	@Before
	public void before() {
		context = new ThreadLocalContext(Scope.class);
	}

	@Test
	public void testContextNotActive() {
		try {
			context.setActive(false);
			context.get(null);
			fail();
		} catch (ContextNotActiveException exception) {
		}
	}

	@SuppressWarnings({ "unchecked" })
	@Test
	public void testStoreContainsInstance() {
		String instance = "instance";

		ContextStore store = PowerMock.createMock(ContextStore.class);
		expect(store.get(EasyMock.anyObject(String.class))).andReturn(instance);
		expect(store.contains(EasyMock.anyObject(String.class))).andReturn(true);

		ThreadLocal<ContextStore> threadLocal = PowerMock.createMock(ThreadLocal.class);
		expect(threadLocal.get()).andReturn(store).times(4);

		Whitebox.setInternalState(context, "threadLocal", threadLocal);

		Bean<String> contextual = new MyBean();
		PowerMock.replayAll(threadLocal, store);

		context.setActive(true);
		Assert.assertEquals(instance, context.get(contextual));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testStoreDoesNotContainsInstance() {
		String instance = "instance";

		ContextStore store = PowerMock.createMock(ContextStore.class);
		expect(store.get(EasyMock.anyObject(String.class))).andReturn(instance);
		expect(store.contains(EasyMock.anyObject(String.class))).andReturn(false).times(1);

		ThreadLocal<ContextStore> threadLocal = PowerMock.createMock(ThreadLocal.class);
		expect(threadLocal.get()).andReturn(store).times(8);

		Whitebox.setInternalState(context, "threadLocal", threadLocal);

		Bean<String> contextual = new MyBean();

		CreationalContext<String> creationalContext = PowerMock.createMock(CreationalContext.class);
		store.put("java.lang.String", instance);

		PowerMock.replayAll(threadLocal, store);

		context.setActive(true);
		Assert.assertEquals(instance, context.get(contextual, creationalContext));
	}

	@Test
	public void testStoreDoesNotContainsInstanceAndCreationalContextIsNull() {
		String instance = "instance";

		ContextStore store = PowerMock.createMock(ContextStore.class);
		expect(store.get(EasyMock.anyObject(String.class))).andReturn(instance);
		expect(store.contains(EasyMock.anyObject(String.class))).andReturn(false);

		@SuppressWarnings("unchecked")
		ThreadLocal<ContextStore> threadLocal = PowerMock.createMock(ThreadLocal.class);
		expect(threadLocal.get()).andReturn(store).times(4);

		Whitebox.setInternalState(context, "threadLocal", threadLocal);

		Bean<String> contextual = new MyBean();
		PowerMock.replayAll(threadLocal, store);

		context.setActive(true);
		Assert.assertNull(context.get(contextual));
	}

	@Test
	public void testContextStoreIsNull() {
		String instance = "instance";

		@SuppressWarnings("unchecked")
		ThreadLocal<ContextStore> threadLocal = PowerMock.createMock(ThreadLocal.class);
		expect(threadLocal.get()).andReturn(null);
		threadLocal.set(EasyMock.anyObject(ContextStore.class));
		PowerMock.expectLastCall();

		ContextStore store = PowerMock.createMock(ContextStore.class);
		expect(store.get(EasyMock.anyObject(String.class))).andReturn(instance);
		expect(threadLocal.get()).andReturn(store).times(4);
		expect(store.contains(EasyMock.anyObject(String.class))).andReturn(true);

		Whitebox.setInternalState(context, "threadLocal", threadLocal);

		Bean<String> contextual = new MyBean();
		PowerMock.replayAll(threadLocal, store);

		context.setActive(true);
		Assert.assertEquals(instance, context.get(contextual));
	}

	class MyBean implements Bean<String> {

		@Override
		public String create(CreationalContext<String> creationalContext) {
			return "instance";
		}

		@Override
		public void destroy(String instance, CreationalContext<String> creationalContext) {
		}

		@Override
		public Set<Type> getTypes() {
			return null;
		}

		@Override
		public Set<Annotation> getQualifiers() {
			return null;
		}

		@Override
		public Class<? extends Annotation> getScope() {
			return null;
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public Set<Class<? extends Annotation>> getStereotypes() {
			return null;
		}

		@Override
		public Class<?> getBeanClass() {
			return String.class;
		}

		@Override
		public boolean isAlternative() {
			return false;
		}

		@Override
		public boolean isNullable() {
			return false;
		}

		@Override
		public Set<InjectionPoint> getInjectionPoints() {
			return null;
		}

	}
}
