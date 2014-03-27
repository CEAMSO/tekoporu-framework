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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Enumeration;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class ResourceBundleTest {

	/**
	 * This is a workaround to mock java.util.ResourceBundle. Since getString(key) method is defined as final, there is
	 * no way to extend and override it. For that reason, setting expectations (i.e. expect(...)) won't work.
	 */
	private ResourceBundle resourceBundle;

	@Before
	public void setUp() throws Exception {
		resourceBundle = new ResourceBundle("resource-bundle", new Locale("pt"));
	}

	@Test
	public void containsKey() {
		assertTrue(resourceBundle.containsKey("msgWithoutParams"));

		assertFalse(resourceBundle.containsKey("inexistentKey"));
	}

	@Test
	public void getKeys() {
		int keyCount = 0;

		Enumeration<String> e = resourceBundle.getKeys();

		while (e.hasMoreElements()) {
			keyCount++;
			e.nextElement();
		}

		assertEquals(resourceBundle.keySet().size(), keyCount);
	}

	@Test
	public void testGetLocale() {
		assertEquals(resourceBundle.getLocale(), new Locale("pt"));
	}

	@Test
	public void testKeySet() {
		assertEquals(2, resourceBundle.keySet().size());
	}

	@Test
	public void getString() {
		assertEquals("no params", resourceBundle.getString("msgWithoutParams"));

		assertEquals("params: a, b", resourceBundle.getString("msgWithParams", "a", "b"));

		assertEquals("params: {0}, {1}", resourceBundle.getString("msgWithParams"));
	}

	/**
	 * For this test, java.util.ResourceBundle is mocked to force an exception. Since the getString method is called
	 * from the actual ResourceBundle, not from the mock, it tries to find a handleGetObject method that doesn't exist.
	 * 
	 * @throws Exception
	 */
	// @Test(expected = RuntimeException.class)
	// public void getStringWhenHandleGetObjectThrowsException() {
	// mockResourceBundle = createMock(java.util.ResourceBundle.class);
	// resourceBundle = new ResourceBundle(mockResourceBundle);
	//
	// replay(mockResourceBundle);
	//
	// resourceBundle.getString("msgWithParams");
	//
	// verify(mockResourceBundle);
	//
	// Assert.fail();
	// }

}
