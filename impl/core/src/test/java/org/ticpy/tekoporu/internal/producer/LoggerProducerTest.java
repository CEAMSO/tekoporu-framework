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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Member;

import javax.enterprise.inject.spi.InjectionPoint;

import org.junit.Test;
import org.slf4j.Logger;

public class LoggerProducerTest {

	private Logger logger;

	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testCreateInjectionPoint() {

		Member member = createMock(Member.class);
		expect(member.getDeclaringClass()).andReturn((Class) this.getClass());
		replay(member);

		InjectionPoint injectionPoint = createMock(InjectionPoint.class);
		expect(injectionPoint.getMember()).andReturn(member);
		replay(injectionPoint);

		logger = LoggerProducer.create(injectionPoint);
		assertNotNull(logger);
	}

	@Test
	public void testCreateWithNullInjectionPoint() {
		logger = LoggerProducer.create((InjectionPoint) null);
		assertNotNull(logger);
	}

	@Test
	public void testCreateClass() {
		logger = LoggerProducer.create(this.getClass());
		assertNotNull(logger);
	}

	// We don't need to instantiate LoggerProducer class. But if we don't get in this way, we'll not get 100% on
	// cobertura.
	@Test
	public void testLoggerFactoryDiferentNull() {
		@SuppressWarnings("unused")
		LoggerProducer loggerProducer = new LoggerProducer();
	}

}
