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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.ticpy.tekoporu.exception.ApplicationException;
import org.ticpy.tekoporu.message.DefaultMessage;
import org.ticpy.tekoporu.message.Message;
import org.ticpy.tekoporu.message.SeverityType;

import com.sun.faces.util.Util;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Beans.class, Strings.class, Converter.class, Util.class, ResourceBundle.class })
public class FacesTest {

	private FacesContext facesContext;

	private Faces faces;

	@Before
	public void before() {
		faces = new Faces();

		PowerMock.mockStatic(Beans.class);
		facesContext = PowerMock.createMock(FacesContext.class);
	}

	@SuppressWarnings("static-access")
	@Test
	public void testAddMessage() {
		expect(Beans.getReference(ResourceBundle.class)).andReturn(null).anyTimes();
		expect(Beans.getReference(FacesContext.class)).andReturn(facesContext).anyTimes();

		facesContext.addMessage(EasyMock.anyObject(String.class), EasyMock.anyObject(FacesMessage.class));
		EasyMock.expectLastCall().times(5);

		replayAll();

		DefaultMessage message = new DefaultMessage("text") {

			@Override
			public String toString() {
				return "Message";
			}

		};

		List<Message> messages = new ArrayList<Message>();
		messages.add(message);

		faces.addMessages(null);
		faces.addMessages(messages);
		faces.addMessage(message);
		faces.addMessage("clientId", message);
		faces.addMessage("clientId", new MyException());
		faces.addMessage(new MyException());
		verifyAll();
	}

	@SuppressWarnings("static-access")
	@Test
	public void testParseSeverity() {
		assertEquals(faces.parse(SeverityType.ERROR), FacesMessage.SEVERITY_ERROR);
		assertEquals(faces.parse(SeverityType.FATAL), FacesMessage.SEVERITY_FATAL);
		assertEquals(faces.parse(SeverityType.INFO), FacesMessage.SEVERITY_INFO);
		assertEquals(faces.parse(SeverityType.WARN), FacesMessage.SEVERITY_WARN);
	}

	@SuppressWarnings("static-access")
	@Test
	public void testParseThrowable() {
		FacesMessage facesMessage = faces.parse(new MyException());
		assertEquals(facesMessage.getSeverity(), FacesMessage.SEVERITY_INFO);
		assertEquals(facesMessage.getSummary(), "MESSAGE");

		facesMessage = faces.parse(new Exception());
		assertEquals(facesMessage.getSeverity(), FacesMessage.SEVERITY_ERROR);
		assertEquals(facesMessage.getSummary(), "java.lang.Exception");
	}

	@SuppressWarnings("static-access")
	@Test
	public void testConvertNull() {
		PowerMock.mockStatic(Strings.class);

		Converter converter = PowerMock.createMock(Converter.class);
		expect(Strings.isEmpty(EasyMock.anyObject(String.class))).andReturn(true);

		replayAll();
		String object = (String) faces.convert("value", converter);
		assertEquals(null, object);
		verifyAll();
	}

	@SuppressWarnings("static-access")
	@Test
	public void testConvert() {
		PowerMock.mockStatic(Strings.class);

		expect(Beans.getReference(FacesContext.class)).andReturn(facesContext).times(2);

		Converter converter = PowerMock.createMock(Converter.class);
		expect(Strings.isEmpty(EasyMock.anyObject(String.class))).andReturn(false).times(2);
		expect(
				converter.getAsObject(EasyMock.anyObject(FacesContext.class), EasyMock.anyObject(UIViewRoot.class),
						EasyMock.anyObject(String.class))).andReturn("THAT");
		expect(facesContext.getViewRoot()).andReturn(null);

		replayAll();
		String object = (String) faces.convert("value", converter);
		assertEquals("THAT", object);

		object = (String) faces.convert("value", null);
		assertEquals("value", object);

		verifyAll();
	}

	@SuppressWarnings("static-access")
	@Test
	public void testGetConverter() {
		Application application = PowerMock.createMock(Application.class);
		Converter converter = PowerMock.createMock(Converter.class);
		PowerMock.mockStatic(Util.class);

		expect(Beans.getReference(FacesContext.class)).andReturn(facesContext);
		expect(facesContext.getApplication()).andReturn(application);
		expect(application.createConverter(getClass())).andReturn(converter);

		replayAll();
		assertEquals(converter, faces.getConverter(getClass()));
		verifyAll();
	}

	@SuppressWarnings("static-access")
	@Test
	public void testGetViewMap() {
		UIViewRoot uiViewRoot = PowerMock.createMock(UIViewRoot.class);
		expect(Beans.getReference(FacesContext.class)).andReturn(facesContext);
		expect(facesContext.getViewRoot()).andReturn(uiViewRoot);

		Map<String, Object> map = new HashMap<String, Object>();
		expect(uiViewRoot.getViewMap(true)).andReturn(map);

		replayAll();
		assertEquals(map, faces.getViewMap());
		verifyAll();
	}

	@SuppressWarnings("serial")
	@ApplicationException(severity = SeverityType.INFO)
	class MyException extends Exception {

		@Override
		public String getMessage() {
			return "MESSAGE";
		}

	}

}
