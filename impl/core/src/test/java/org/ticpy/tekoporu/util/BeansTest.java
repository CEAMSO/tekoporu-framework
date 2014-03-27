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

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ BeanManager.class, Bean.class })
public class BeansTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testGetReferenceByClass() {
		BeanManager beanManager = PowerMock.createMock(BeanManager.class);

		Set<Bean<?>> collection = new HashSet<Bean<?>>();
		Bean<?> bean = PowerMock.createMock(Bean.class);
		collection.add(bean);

		String object = "object";

		expect(beanManager.createCreationalContext(EasyMock.anyObject(Contextual.class))).andReturn(null);
		expect(beanManager.getBeans(EasyMock.anyObject(Class.class))).andReturn(collection);
		expect(beanManager.getReference(EasyMock.anyObject(Bean.class), EasyMock.anyObject(Class.class),
						EasyMock.anyObject(CreationalContext.class))).andReturn(object);
		
		expect(bean.getBeanClass()).andReturn(null);

		replayAll(beanManager, bean);

		Beans.setBeanManager(beanManager);
		String returned = Beans.getReference(String.class);

		assertEquals(returned, object);
		assertEquals(beanManager, Beans.getBeanManager());

		verifyAll();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testGetReferenceByString() {
		BeanManager beanManager = PowerMock.createMock(BeanManager.class);

		Set<Bean<?>> collection = new HashSet<Bean<?>>();
		Bean<?> bean = PowerMock.createMock(Bean.class);
		collection.add(bean);

		String object = "object";

		expect(bean.getBeanClass()).andReturn(null);
		expect(beanManager.createCreationalContext(EasyMock.anyObject(Contextual.class))).andReturn(null);
		expect(beanManager.getBeans("something")).andReturn(collection);
		expect(beanManager.getReference(EasyMock.anyObject(Bean.class), EasyMock.anyObject(Class.class),
						EasyMock.anyObject(CreationalContext.class))).andReturn(object);

		replayAll(beanManager, bean);

		Beans.setBeanManager(beanManager);
		String returned = Beans.getReference("something");

		assertEquals(returned, object);
		assertEquals(beanManager, Beans.getBeanManager());

		verifyAll();
	}
}
