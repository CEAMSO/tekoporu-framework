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

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.ticpy.tekoporu.internal.producer.HttpServletRequestProducer;
import org.ticpy.tekoporu.internal.producer.HttpServletResponseProducer;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Beans.class)
public class ServletFilterTest {

	private ServletFilter filter;

	@Test
	public void testInit() throws ServletException {
		FilterConfig config = createMock(FilterConfig.class);
		replay(config);

		filter = new ServletFilter();
		filter.init(config);

		verifyAll();
	}

	@Test
	public void testDoFilter() throws IOException, ServletException {
		HttpServletRequest request = createMock(HttpServletRequest.class);
		HttpServletResponse response = createMock(HttpServletResponse.class);

		FilterChain chain = createMock(FilterChain.class);
		HttpServletRequestProducer requestProducer = createMock(HttpServletRequestProducer.class);
		HttpServletResponseProducer responseProducer = createMock(HttpServletResponseProducer.class);

		mockStatic(Beans.class);
		expect(Beans.getReference(HttpServletRequestProducer.class)).andReturn(requestProducer);
		expect(Beans.getReference(HttpServletResponseProducer.class)).andReturn(responseProducer);
		requestProducer.setDelegate(request);
		PowerMock.expectLastCall().times(1);
		responseProducer.setDelegate(response);
		PowerMock.expectLastCall().times(1);
		chain.doFilter(request, response);
		PowerMock.expectLastCall().times(1);

		replay(Beans.class, request, response, chain, requestProducer, responseProducer);

		filter = new ServletFilter();
		filter.doFilter(request, response, chain);

		verifyAll();
	}

	@Test
	public void testDestroy() {
		filter = new ServletFilter();
		filter.destroy();
		verifyAll();
	}
}
