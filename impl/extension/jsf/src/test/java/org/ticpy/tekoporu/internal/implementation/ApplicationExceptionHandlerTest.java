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

package org.ticpy.tekoporu.internal.implementation;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectLastCall;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.util.ArrayList;
import java.util.Collection;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.PhaseId;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.ticpy.tekoporu.exception.ApplicationException;
import org.ticpy.tekoporu.internal.configuration.ExceptionHandlerConfig;
import org.ticpy.tekoporu.util.Beans;
import org.ticpy.tekoporu.util.Faces;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Beans.class, FacesContext.class, Faces.class })
public class ApplicationExceptionHandlerTest {

	private ApplicationExceptionHandler handler;

	private ExceptionQueuedEventContext eventContext;

	private ExceptionHandlerConfig config;

	private FacesContext facesContext;

	private Collection<ExceptionQueuedEvent> events;

	@SuppressWarnings("serial")
	@ApplicationException
	class AnnotatedAppException extends RuntimeException {
	}

	@SuppressWarnings("serial")
	class SomeException extends RuntimeException {
	}

	@Before
	public void setUp() {

		mockStatic(Beans.class);
		mockStatic(FacesContext.class);

		events = new ArrayList<ExceptionQueuedEvent>();
		ExceptionHandler jsfExceptionHandler = createMock(ExceptionHandler.class);
		handler = new ApplicationExceptionHandler(jsfExceptionHandler);
		eventContext = PowerMock.createMock(ExceptionQueuedEventContext.class);
		ExceptionQueuedEvent event = PowerMock.createMock(ExceptionQueuedEvent.class);
		config = PowerMock.createMock(ExceptionHandlerConfig.class);
		facesContext = PowerMock.createMock(FacesContext.class);

		expect(event.getSource()).andReturn(eventContext);
		expect(Beans.getReference(ExceptionHandlerConfig.class)).andReturn(config);
		expect(FacesContext.getCurrentInstance()).andReturn(facesContext).anyTimes();
		expect(handler.getUnhandledExceptionQueuedEvents()).andReturn(events).times(2);

		events.add(event);

	}

	@Test
	public void testHandleAnApplicationExceptionNotOnRenderResponse() {

		mockStatic(Faces.class);

		AnnotatedAppException exception = new AnnotatedAppException();
		PhaseId phaseId = PowerMock.createMock(PhaseId.class);

		expect(eventContext.getException()).andReturn(exception);
		expect(facesContext.getCurrentPhaseId()).andReturn(phaseId);
		expect(config.isHandleApplicationException()).andReturn(true);

		Faces.addMessage(exception);
		expectLastCall();

		replayAll();

		handler.handle();

		assertTrue(events.isEmpty());

		verifyAll();

	}

	@Test
	public void testHandleAnApplicationExceptionOnRenderResponse() {

		AnnotatedAppException exception = new AnnotatedAppException();
		// PhaseId phaseId = PhaseId.RENDER_RESPONSE;

		expect(eventContext.getException()).andReturn(exception);
		// expect(facesContext.getCurrentPhaseId()).andReturn(phaseId);
		expect(config.isHandleApplicationException()).andReturn(false);

		handler.getWrapped().handle();
		expectLastCall();

		replayAll();

		handler.handle();

		assertFalse(events.isEmpty());

		verifyAll();

	}

	@Test
	public void testHandleAnyException() {

		SomeException exception = new SomeException();
		// PhaseId phaseId = PowerMock.createMock(PhaseId.class);

		expect(eventContext.getException()).andReturn(exception);
		// expect(facesContext.getCurrentPhaseId()).andReturn(phaseId);
		expect(config.isHandleApplicationException()).andReturn(true);

		handler.getWrapped().handle();
		expectLastCall();

		replayAll();

		handler.handle();

		assertFalse(events.isEmpty());

		verifyAll();

	}

	@Test
	public void testDoNotHandleApplicationExceptions() {

		AnnotatedAppException exception = new AnnotatedAppException();

		expect(eventContext.getException()).andReturn(exception);
		expect(config.isHandleApplicationException()).andReturn(false);

		handler.getWrapped().handle();
		expectLastCall();

		replayAll();

		handler.handle();

		assertFalse(events.isEmpty());

		verifyAll();

	}
}
