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

package org.ticpy.tekoporu.internal.proxy;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.powermock.api.easymock.PowerMock.mockStatic;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LoggerFactory.class)
public class Slf4jLoggerProxyTest {

	private Logger logger;
	private Slf4jLoggerProxy slf4jLoggerProxy;
	
	@Before
	public void setUp() throws Exception {
		this.logger = EasyMock.createMock(Logger.class);
		this.slf4jLoggerProxy = new Slf4jLoggerProxy(Logger.class);
		
		mockStatic(LoggerFactory.class);
		
		expect(LoggerFactory.getLogger(EasyMock.anyObject(Class.class))).andReturn(logger);
	}
	
	@Test
	public void testDebugWithMarkerAndString() {
		Marker marker = null;
		this.logger.debug(marker,"");
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.debug(marker,"");
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testDebugWithMarkerStringAndOneObject() {
		Marker marker = null;
		Object obj = null;
		this.logger.debug(marker,"",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.debug(marker,"",obj);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testDebugWithMarkerStringAndTwoObjects() {
		Marker marker = null;
		Object obj1 = null, obj2 = null;
		this.logger.debug(marker,"",obj1,obj2);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.debug(marker,"",obj1,obj2);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testDebugWithMarkerStringAndObjectArray() {
		Marker marker = null;
		Object[] obj = null;
		this.logger.debug(marker,"",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.debug(marker,"",obj);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testDebugWithMarkerStringAndThrowable() {
		Marker marker = null;
		Throwable t = null;
		this.logger.debug(marker,"",t);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.debug(marker,"",t);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testDebugWithString() {
		this.logger.debug("");
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.debug("");
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testDebugWithStringAndOneObject() {
		Object obj = null;
		this.logger.debug("",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.debug("",obj);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testDebugWithStringAndTwoObjects() {
		Object obj1 = null, obj2 = null;
		this.logger.debug("",obj1,obj2);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.debug("",obj1,obj2);
		PowerMock.verify(this.logger);
	}

	@Test
	public void testDebugWithStringAndObjectArray() {
		Object[] obj = null;
		this.logger.debug("",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.debug("",obj);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testDebugWithStringAndThrowable() {
		Throwable t = null;
		this.logger.debug("",t);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.debug("",t);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testErrorWithMarkerAndString() {
		Marker marker = null;
		this.logger.error(marker,"");
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.error(marker,"");
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testErrorWithMarkerStringAndOneObject() {
		Marker marker = null;
		Object obj = null;
		this.logger.error(marker,"",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.error(marker,"",obj);
		PowerMock.verify(this.logger);
	}
		
	@Test
	public void testErrorWithMarkerStringAndTwoObjects() {
		Marker marker = null;
		Object obj1 = null, obj2 = null;
		this.logger.error(marker,"",obj1,obj2);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.error(marker,"",obj1,obj2);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testErrorWithMarkerStringAndObjectArray() {
		Marker marker = null;
		Object[] obj1 = null;
		this.logger.error(marker,"",obj1);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.error(marker,"",obj1);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testErrorWithMarkerStringAndThrowable() {
		Marker marker = null;
		Throwable t = null;
		this.logger.error(marker,"",t);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.error(marker,"",t);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testErrorWithString() {
		this.logger.error("");
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.error("");
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testErrorWithStringAndOneObject() {
		Object obj = null;
		this.logger.error("",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.error("",obj);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testErrorWithStringAndTwoObjects() {
		Object obj1 = null,obj2 = null;
		this.logger.error("",obj1,obj2);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.error("",obj1,obj2);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testErrorWithStringAndObjectArray() {
		Object[] obj = null;
		this.logger.error("",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.error("",obj);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testErrorWithStringAndThrowable() {
		Throwable t = null;
		this.logger.error("",t);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.error("",t);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testGetName() {
		expect(this.logger.getName()).andReturn("xxx");
		PowerMock.replay(LoggerFactory.class, this.logger);
		assertEquals("xxx", this.slf4jLoggerProxy.getName());
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testInfoWithMarkerAndString() {
		Marker marker = null;
		this.logger.info(marker,"");
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.info(marker,"");
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testInfoWithMarkerStringAndOneObject() {
		Marker marker = null;
		Object obj = null;
		this.logger.info(marker,"",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.info(marker,"",obj);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testInfoWithMarkerStringAndTwoObjects() {
		Marker marker = null;
		Object obj1 = null, obj2 = null;
		this.logger.info(marker,"",obj1, obj2);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.info(marker,"",obj1,obj2);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testInfoWithMarkerStringAndObjectArray() {
		Marker marker = null;
		Object[] obj = null;
		this.logger.info(marker,"",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.info(marker,"",obj);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testInfoWithMarkerStringAndThrowable() {
		Marker marker = null;
		Throwable t = null;
		this.logger.info(marker,"",t);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.info(marker,"",t);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testInfoWithString() {
		this.logger.info("");
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.info("");
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testInfoWithStringAndOneObject() {
		Object obj = null;
		this.logger.info("",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.info("",obj);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testInfoWithStringAndTwoObjects() {
		Object obj1 = null, obj2 = null;
		this.logger.info("",obj1,obj2);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.info("",obj1,obj2);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testInfoWithStringAndObjectArray() {
		Object[] obj = null;
		this.logger.info("",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.info("",obj);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testInfoWithStringAndThrowable() {
		Throwable t = null;
		this.logger.info("",t);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.info("",t);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testIsDebugEnabled() {
		expect(this.logger.isDebugEnabled()).andReturn(true);
		PowerMock.replay(LoggerFactory.class, this.logger);
		assertEquals(true, this.slf4jLoggerProxy.isDebugEnabled());
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testIsDebugEnabledWithMarker() {
		Marker marker = null;
		expect(this.logger.isDebugEnabled(marker)).andReturn(true);
		PowerMock.replay(LoggerFactory.class, this.logger);
		assertEquals(true, this.slf4jLoggerProxy.isDebugEnabled(marker));
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testIsErrorEnabled() {
		expect(this.logger.isErrorEnabled()).andReturn(true);
		PowerMock.replay(LoggerFactory.class, this.logger);
		assertEquals(true, this.slf4jLoggerProxy.isErrorEnabled());
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testIsErrorEnabledWithMarker() {
		Marker marker = null;
		expect(this.logger.isErrorEnabled(marker)).andReturn(true);
		PowerMock.replay(LoggerFactory.class, this.logger);
		assertEquals(true, this.slf4jLoggerProxy.isErrorEnabled(marker));
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testIsInfoEnabled() {
		expect(this.logger.isInfoEnabled()).andReturn(true);
		PowerMock.replay(LoggerFactory.class, this.logger);
		assertEquals(true, this.slf4jLoggerProxy.isInfoEnabled());
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testIsInfoEnabledWithMarker() {
		Marker marker = null;
		expect(this.logger.isInfoEnabled(marker)).andReturn(true);
		PowerMock.replay(LoggerFactory.class, this.logger);
		assertEquals(true, this.slf4jLoggerProxy.isInfoEnabled(marker));
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testIsTRaceEnabled() {
		expect(this.logger.isTraceEnabled()).andReturn(true);
		PowerMock.replay(LoggerFactory.class, this.logger);
		assertEquals(true, this.slf4jLoggerProxy.isTraceEnabled());
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testIsTraceEnabledWithMarker() {
		Marker marker = null;
		expect(this.logger.isTraceEnabled(marker)).andReturn(true);
		PowerMock.replay(LoggerFactory.class, this.logger);
		assertEquals(true, this.slf4jLoggerProxy.isTraceEnabled(marker));
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testIsWarnEnabled() {
		expect(this.logger.isWarnEnabled()).andReturn(true);
		PowerMock.replay(LoggerFactory.class, this.logger);
		assertEquals(true, this.slf4jLoggerProxy.isWarnEnabled());
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testIsWarnEnabledWithMarker() {
		Marker marker = null;
		expect(this.logger.isWarnEnabled(marker)).andReturn(true);
		PowerMock.replay(LoggerFactory.class, this.logger);
		assertEquals(true, this.slf4jLoggerProxy.isWarnEnabled(marker));
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testTraceWithMarkerAndString() {
		Marker marker = null;
		this.logger.trace(marker,"");
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.trace(marker,"");
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testTraceWithMarkerStringAndOneObject() {
		Marker marker = null;
		Object obj = null;
		this.logger.trace(marker,"",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.trace(marker,"",obj);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testTraceWithMarkerStringAndTwoObjects() {
		Marker marker = null;
		Object obj1 = null, obj2 = null;
		this.logger.trace(marker,"",obj1, obj2);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.trace(marker,"",obj1,obj2);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testTraceWithMarkerStringAndObjectArray() {
		Marker marker = null;
		Object[] obj = null;
		this.logger.trace(marker,"",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.trace(marker,"",obj);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testTraceWithMarkerStringAndThrowable() {
		Marker marker = null;
		Throwable t = null;
		this.logger.trace(marker,"",t);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.trace(marker,"",t);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testTraceWithString() {
		this.logger.trace("");
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.trace("");
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testTraceWithStringAndOneObject() {
		Object obj = null;
		this.logger.trace("",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.trace("",obj);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testTraceWithStringAndTwoObjects() {
		Object obj1 = null, obj2 = null;
		this.logger.trace("",obj1,obj2);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.trace("",obj1,obj2);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testTraceWithStringAndObjectArray() {
		Object[] obj = null;
		this.logger.trace("",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.trace("",obj);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testTraceWithStringAndThrowable() {
		Throwable t = null;
		this.logger.trace("",t);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.trace("",t);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testWarnWithMarkerAndString() {
		Marker marker = null;
		this.logger.warn(marker,"");
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.warn(marker,"");
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testWarnWithMarkerStringAndOneObject() {
		Marker marker = null;
		Object obj = null;
		this.logger.warn(marker,"",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.warn(marker,"",obj);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testWarnWithMarkerStringAndTwoObjects() {
		Marker marker = null;
		Object obj1 = null, obj2 = null;
		this.logger.warn(marker,"",obj1, obj2);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.warn(marker,"",obj1,obj2);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testWarnWithMarkerStringAndObjectArray() {
		Marker marker = null;
		Object[] obj = null;
		this.logger.warn(marker,"",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.warn(marker,"",obj);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testWarnWithMarkerStringAndThrowable() {
		Marker marker = null;
		Throwable t = null;
		this.logger.warn(marker,"",t);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.warn(marker,"",t);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testWarnWithString() {
		this.logger.warn("");
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.warn("");
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testWarnWithStringAndOneObject() {
		Object obj = null;
		this.logger.warn("",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.warn("",obj);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testWarnWithStringAndTwoObjects() {
		Object obj1 = null, obj2 = null;
		this.logger.warn("",obj1,obj2);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.warn("",obj1,obj2);
		PowerMock.verify(this.logger);
	}
		
	@Test
	public void testWarnWithStringAndObjectArray() {
		Object[] obj = null;
		this.logger.warn("",obj);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.warn("",obj);
		PowerMock.verify(this.logger);
	}
	
	@Test
	public void testWarnWithStringAndThrowable() {
		Throwable t = null;
		this.logger.warn("",t);
		PowerMock.replay(LoggerFactory.class, this.logger);
		this.slf4jLoggerProxy.warn("",t);
		PowerMock.verify(this.logger);
	}
}
