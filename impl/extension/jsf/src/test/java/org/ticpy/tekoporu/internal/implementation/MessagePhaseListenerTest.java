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

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

import org.ticpy.tekoporu.internal.producer.LoggerProducer;
import org.ticpy.tekoporu.message.Message;
import org.ticpy.tekoporu.message.MessageContext;
import org.ticpy.tekoporu.util.Beans;
import org.ticpy.tekoporu.util.Faces;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ LoggerProducer.class, Beans.class, Faces.class })
public class MessagePhaseListenerTest {

	private MessagePhaseListener m;

	private Logger logger;

	@Before
	public void before() {
		PowerMock.mockStatic(LoggerProducer.class);
		logger = PowerMock.createMock(Logger.class);
		expect(LoggerProducer.create(MessagePhaseListener.class)).andReturn(logger);
	}

	@Test
	public void testGetPhaseId() {
		m = new MessagePhaseListener();
		Assert.assertEquals(PhaseId.RENDER_RESPONSE, m.getPhaseId());
	}

	@Test
	public void testBeforePhase() {

		mockStatic(Beans.class);
		mockStatic(Faces.class);

		PhaseEvent event = PowerMock.createMock(PhaseEvent.class);
		MessageContext mc = PowerMock.createMock(MessageContext.class);

		logger.debug(EasyMock.anyObject(String.class));
		logger.debug(EasyMock.anyObject(String.class));

		List<Message> list = new ArrayList<Message>();

		expect(event.getPhaseId()).andReturn(PhaseId.RENDER_RESPONSE);
		expect(Beans.getReference(MessageContext.class)).andReturn(mc);
		expect(mc.getMessages()).andReturn(list).anyTimes();

		mc.clear();
		Faces.addMessages(list);

		replayAll();
		m = new MessagePhaseListener();
		m.beforePhase(event);
		m.afterPhase(event);
		verifyAll();
	}

}
