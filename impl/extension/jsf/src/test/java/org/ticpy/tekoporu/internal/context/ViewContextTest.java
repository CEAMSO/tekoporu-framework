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

import static junit.framework.Assert.assertEquals;

import java.util.Map;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.ticpy.tekoporu.annotation.ViewScoped;
import org.ticpy.tekoporu.util.Faces;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Faces.class })
public class ViewContextTest {

	private ViewContext context;

	@Before
	public void before() {
		context = new ViewContext();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetViewMapContainsInstance() {
		String instance = "instance";

		Bean<?> bean = PowerMock.createMock(Bean.class);
		EasyMock.expect(bean.getName()).andReturn(instance).anyTimes();

		Map<String,Object> map = PowerMock.createMock(Map.class);
		EasyMock.expect(map.containsKey(EasyMock.anyObject(String.class))).andReturn(true);
		EasyMock.expect(map.get(EasyMock.anyObject(String.class))).andReturn(instance);

		PowerMock.mockStatic(Faces.class);
		EasyMock.expect(Faces.getViewMap()).andReturn(map);

		PowerMock.replay(Faces.class, bean, map);

		assertEquals(instance, context.get(bean));
		
		PowerMock.verifyAll();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetViewMapDoesNotContainsInstance() {
		String instance = "instance";

		Bean<String> bean = PowerMock.createMock(Bean.class);
		EasyMock.expect(bean.getName()).andReturn(instance).anyTimes();
		EasyMock.expect(bean.create(EasyMock.anyObject(CreationalContext.class))).andReturn(instance);

		Map<String,Object> map = PowerMock.createMock(Map.class);
		EasyMock.expect(map.containsKey(EasyMock.anyObject(String.class))).andReturn(false);
		EasyMock.expect(map.put(EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(null);

		PowerMock.mockStatic(Faces.class);
		EasyMock.expect(Faces.getViewMap()).andReturn(map);

		CreationalContext<String> creationalContext = PowerMock.createMock(CreationalContext.class);

		PowerMock.replay(Faces.class, bean, map, creationalContext);

		assertEquals(instance, context.get(bean, creationalContext));

		PowerMock.verifyAll();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetViewMapInstanceNull() {
		String instance = "instance";

		Bean<String> bean = PowerMock.createMock(Bean.class);
		EasyMock.expect(bean.getName()).andReturn(instance).anyTimes();

		Map<String,Object> map = PowerMock.createMock(Map.class);
		EasyMock.expect(map.containsKey(EasyMock.anyObject(String.class))).andReturn(false);

		PowerMock.mockStatic(Faces.class);
		EasyMock.expect(Faces.getViewMap()).andReturn(map);

		PowerMock.replay(Faces.class, bean, map);

		assertEquals(null, context.get(bean));

		PowerMock.verifyAll();
	}
	
	@Test
	public void testScopeClass() {
		assertEquals(ViewScoped.class, context.getScope());
	}

	@Test
	public void testIsActive() {
		assertEquals(true, context.isActive());
	}
	
	@Test
	public void testSetActive() {
		context.setActive(false);
		assertEquals(false, context.isActive());
	}

}


