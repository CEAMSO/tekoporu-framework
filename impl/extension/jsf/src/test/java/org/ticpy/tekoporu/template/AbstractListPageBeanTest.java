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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.ListDataModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import org.ticpy.tekoporu.internal.implementation.PaginationImpl;
import org.ticpy.tekoporu.pagination.Pagination;
import org.ticpy.tekoporu.pagination.PaginationContext;
import org.ticpy.tekoporu.util.Reflections;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Reflections.class, PaginationContext.class, Pagination.class })
public class AbstractListPageBeanTest {

	private MySimplePageBean pageBean;

	@Before
	public void before() {
		pageBean = new MySimplePageBean();
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testClear() {
		assertNull(Whitebox.getInternalState(pageBean, "dataModel"));
		assertNull(Whitebox.getInternalState(pageBean, "resultList"));

		Whitebox.setInternalState(pageBean, "resultList", new ArrayList());
		Whitebox.setInternalState(pageBean, "dataModel", new ListDataModel());

		pageBean.clear();

		assertNull(Whitebox.getInternalState(pageBean, "dataModel"));
		assertNull(Whitebox.getInternalState(pageBean, "resultList"));
	}

	@Test
	public void testGetBeanClass() {
		assertNull(Whitebox.getInternalState(pageBean, "beanClass"));

		PowerMock.mockStatic(Reflections.class);
		expect(Reflections.getGenericTypeArgument(pageBean.getClass(), 0)).andReturn(Object.class);

		PowerMock.replayAll();
		assertEquals(Object.class, pageBean.getBeanClass());
		PowerMock.verifyAll();

		Whitebox.setInternalState(pageBean, "beanClass", Contact.class, AbstractListPageBean.class);

		assertEquals(Contact.class, pageBean.getBeanClass());
	}

	@Test
	public void testGetDataModel() {
		assertNull(Whitebox.getInternalState(pageBean, "dataModel"));
		assertEquals(ListDataModel.class, pageBean.getDataModel().getClass());

		ListDataModel<Contact> ldm = new ListDataModel<Contact>();
		Whitebox.setInternalState(pageBean, "dataModel", ldm);

		assertEquals(ldm, pageBean.getDataModel());
	}

	@Test
	public void testGetResultList() {
		assertNull(Whitebox.getInternalState(pageBean, "resultList"));

		List<Contact> list = pageBean.getResultList();
		assertTrue(list.size() == 2);

		list = new ArrayList<Contact>();
		Whitebox.setInternalState(pageBean, "resultList", list);
		assertTrue(list.size() == 0);
		assertEquals(list, pageBean.getResultList());
	}

	@Test
	public void testList() {
		this.testClear();
		assertEquals(pageBean.getCurrentView(), pageBean.list());
	}

	@Test
	public void testSelection() {
		Map<Long, Boolean> map = new HashMap<Long, Boolean>();
		map.put(1L, true);
		Whitebox.setInternalState(pageBean, "selection", map);
		assertEquals(map, pageBean.getSelection());
		assertEquals(true, pageBean.getSelection().get(1L));

		pageBean.setSelection(null);
		assertNull(Whitebox.getInternalState(pageBean, "selection"));
		pageBean.setSelection(map);
		assertEquals(map, pageBean.getSelection());
		assertEquals(true, pageBean.getSelection().get(1L));
	}

	@Test
	public void testPagination() {
		Pagination pagination = new PaginationImpl();
		PaginationContext pc = PowerMock.createMock(PaginationContext.class);
		expect(pc.getPagination(Contact.class, true)).andReturn(pagination);

		replayAll();
		Whitebox.setInternalState(pageBean, "paginationContext", pc);
		assertEquals(pageBean.getPagination(), pagination);
		verifyAll();
	}

	@Test
	public void testClearSelection() {
		pageBean.clearSelection();
		assertEquals(true, pageBean.getSelectedList().isEmpty());
	}

	@Test
	public void testGetSelectedList() {
		Map<Long, Boolean> map = new HashMap<Long, Boolean>();
		map.put(1L, true);
		map.put(2L, true);
		pageBean.setSelection(map);
		assertEquals(2, pageBean.getSelectedList().size());
	}

}

@SuppressWarnings("serial")
class MySimplePageBean extends AbstractListPageBean<Contact, Long> {

	@Override
	protected List<Contact> handleResultList() {
		List<Contact> list = new ArrayList<Contact>();
		list.add(new Contact());
		list.add(new Contact());
		return list;
	}

	@Override
	public String getCurrentView() {
		return "currentView";
	}

}
