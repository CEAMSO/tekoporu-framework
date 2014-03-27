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

package org.ticpy.tekoporu.template;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import org.ticpy.tekoporu.util.Beans;
import org.ticpy.tekoporu.util.Reflections;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Crud.class, Beans.class, Reflections.class })
public class DelegateCrudTest {

	private DelegateCrud<Contact, Long, Delegated> delegateCrud;

	private Crud<Contact, Long> mockCrud;

	@SuppressWarnings("unchecked")
	@Before
	public void before() {
		delegateCrud = new DelegateCrud<Contact, Long, Delegated>();
		mockCrud = PowerMock.createMock(Crud.class);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDelete() {
		mockStatic(Beans.class);
		mockStatic(Reflections.class);

		expect(Reflections.getGenericTypeArgument(EasyMock.anyObject(Class.class), EasyMock.anyInt())).andReturn(null);
		expect(Beans.getReference(EasyMock.anyObject(Class.class))).andReturn(mockCrud).times(2);

		mockCrud.delete(1L);
		PowerMock.expectLastCall();
		
		PowerMock.replay(Reflections.class, Beans.class, mockCrud);

		delegateCrud.delete(1L);

		PowerMock.verify();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdate() {
		Whitebox.setInternalState(delegateCrud, "delegate", mockCrud);
		
		mockStatic(Beans.class);
		
		expect(Beans.getReference(EasyMock.anyObject(Class.class))).andReturn(mockCrud);
		
		Contact update = new Contact();
		mockCrud.update(update);
		replayAll(Beans.class, mockCrud);

		delegateCrud.update(update);

		verifyAll();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testInsert() {
		Whitebox.setInternalState(delegateCrud, "delegate", mockCrud);
		
		mockStatic(Beans.class);
	
		expect(Beans.getReference(EasyMock.anyObject(Class.class))).andReturn(mockCrud);
		
		Contact insert = new Contact();
		mockCrud.insert(insert);
		replayAll(mockCrud);

		delegateCrud.insert(insert);

		verifyAll();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testFindAll() {
		mockStatic(Beans.class);
		mockStatic(Reflections.class);

		expect(Reflections.getGenericTypeArgument(EasyMock.anyObject(Class.class), EasyMock.anyInt())).andReturn(null);
		expect(Beans.getReference(EasyMock.anyObject(Class.class))).andReturn(mockCrud);

		List<Contact> returned = new ArrayList<Contact>();
		expect(mockCrud.findAll()).andReturn(returned);
		replayAll(Reflections.class, Beans.class, mockCrud);

		assertEquals(returned, delegateCrud.findAll());

		verifyAll();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testLoad() {
		mockStatic(Beans.class);

		expect(Beans.getReference(EasyMock.anyObject(Class.class))).andReturn(mockCrud);

		Contact contact = new Contact();
		expect(mockCrud.load(1L)).andReturn(contact);
		replayAll(Beans.class, mockCrud);

		Whitebox.setInternalState(delegateCrud, "delegateClass", delegateCrud.getClass(), delegateCrud.getClass());

		assertEquals(contact, delegateCrud.load(1L));
		verifyAll();
	}

	class Contact {

		private Long id;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
	}

	@SuppressWarnings("serial")
	class Delegated implements Crud<Contact, Long> {

		@Override
		public void delete(Long id) {
		}

		@Override
		public List<Contact> findAll() {
			return null;
		}

		@Override
		public void insert(Contact bean) {
		}

		@Override
		public Contact load(Long id) {
			return null;
		}

		@Override
		public void update(Contact bean) {
		}

	}

}
