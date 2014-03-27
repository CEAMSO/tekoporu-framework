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

package org.ticpy.tekoporu.internal.producer;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;

import java.util.Locale;

import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.ticpy.tekoporu.annotation.Name;
import org.ticpy.tekoporu.util.Beans;
import org.ticpy.tekoporu.util.ResourceBundle;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Beans.class)
public class ResourceBundleProducerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		mockStatic(Beans.class);

		expect(Beans.getReference(Locale.class)).andReturn(Locale.getDefault());

		replay(Beans.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testResourceBundleFactory() {
		ResourceBundleProducer factory = new ResourceBundleProducer();
		Assert.assertNotNull(factory);
	}

	 @Test
	 public void testCreateNullInjectionPoint() {
		 ResourceBundleProducer factory = new ResourceBundleProducer();
		 ResourceBundle resourceBundle = factory.create((InjectionPoint) null); 
		 Assert.assertNotNull(resourceBundle);
	 }

	@Test
	public void testCreateInjectionPointNameAnnoted() {
		Name name = EasyMock.createMock(Name.class);
		expect(name.value()).andReturn("demoiselle-core-bundle");
		replay(name);

		Annotated annotated = EasyMock.createMock(Annotated.class);
		expect(annotated.getAnnotation(Name.class)).andReturn(name).anyTimes();
		expect(annotated.isAnnotationPresent(Name.class)).andReturn(true).anyTimes();
		replay(annotated);

		InjectionPoint ip = EasyMock.createMock(InjectionPoint.class);
		expect(ip.getAnnotated()).andReturn(annotated).anyTimes();
		replay(ip);

		ResourceBundleProducer factory = new ResourceBundleProducer();
		Assert.assertNotNull(factory.create(ip));
	}

	// @Test
	// public void testCreateInjectionPointNameUnannoted() {
	// Annotated annotated = EasyMock.createMock(Annotated.class);
	// expect(annotated.isAnnotationPresent(Name.class)).andReturn(false).anyTimes();
	// replay(annotated);
	//
	// InjectionPoint ip = EasyMock.createMock(InjectionPoint.class);
	// expect(ip.getAnnotated()).andReturn(annotated).anyTimes();
	// replay(ip);
	//
	// ResourceBundleProducer factory = new ResourceBundleProducer();
	// Assert.assertNotNull(factory.create(ip, Locale.getDefault()));
	// }
}
