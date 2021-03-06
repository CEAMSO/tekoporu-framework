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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;

import org.ticpy.tekoporu.util.Faces;
import org.ticpy.tekoporu.util.FileRenderer;
import org.ticpy.tekoporu.util.FileRenderer.ContentType;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Faces.class })
public class FileRendererImplTest {

	private Logger logger;

	private FileRenderer renderer;

	private FacesContext facesContext;

	private HttpServletResponse response;

	@Before
	public void before() {
		renderer = new FileRendererImpl();

		logger = PowerMock.createMock(Logger.class);
		Whitebox.setInternalState(renderer, "logger", logger);

		facesContext = PowerMock.createMock(FacesContext.class);
		Whitebox.setInternalState(renderer, "context", facesContext);

		response = PowerMock.createMock(HttpServletResponse.class);
		Whitebox.setInternalState(renderer, "response", response);
	}

	@Test
	public void testRenderBytesFail() {
		byte[] bytes = "Test".getBytes();
		String fileName = "fileName.pdf";

		logger.debug(EasyMock.anyObject(String.class));
		EasyMock.expectLastCall().anyTimes();

		IOException exception = new IOException();
		logger.info("Erro na geração do relatório. Incluíndo a exceção de erro em um FacesMessage", exception);
		EasyMock.expectLastCall().anyTimes();

		response.setContentType(ContentType.PDF.getContentType());
		EasyMock.expectLastCall().times(1);

		response.setContentLength(bytes.length);
		EasyMock.expectLastCall().times(1);

		response.setHeader("Content-Disposition", "filename=\"" + fileName + "\"");
		EasyMock.expectLastCall().times(1);

		facesContext.responseComplete();
		EasyMock.expectLastCall().times(1);

		try {
			EasyMock.expect(response.getOutputStream()).andThrow(exception);
		} catch (IOException e) {
			Assert.fail();
		}

		PowerMock.mockStatic(Faces.class);
		Faces.addMessage(exception);

		PowerMock.replayAll();
		renderer.render(bytes, ContentType.PDF, fileName);
		PowerMock.verifyAll();
	}

	@Test
	public void testRenderBytesSuccess() throws IOException {
		byte[] bytes = "Test".getBytes();
		String fileName = "fileName.pdf";

		logger.debug(EasyMock.anyObject(String.class));
		EasyMock.expectLastCall().anyTimes();

		facesContext.responseComplete();
		EasyMock.expectLastCall().times(1);

		response.setContentType(ContentType.PDF.getContentType());
		EasyMock.expectLastCall().times(1);

		response.setContentLength(bytes.length);
		EasyMock.expectLastCall().times(1);

		response.setHeader("Content-Disposition", "filename=\"" + fileName + "\"");
		EasyMock.expectLastCall().times(1);

		ServletOutputStream stream = PowerMock.createMock(ServletOutputStream.class);
		stream.write(bytes, 0, bytes.length);
		EasyMock.expectLastCall().times(1);

		stream.flush();
		EasyMock.expectLastCall().times(1);

		stream.close();
		EasyMock.expectLastCall().times(1);

		EasyMock.expect(response.getOutputStream()).andReturn(stream).times(3);

		PowerMock.replayAll();
		renderer.render(bytes, ContentType.PDF, fileName);
		PowerMock.verifyAll();
	}

	@Test
	public void testRenderStreamFail() {
		byte[] bytes = "Test".getBytes();
		InputStream stream = new ByteArrayInputStream(bytes);
		String fileName = "fileName.pdf";

		logger.debug(EasyMock.anyObject(String.class));
		EasyMock.expectLastCall().anyTimes();

		IOException exception = new IOException();
		logger.info("Erro na geração do relatório. Incluíndo a exceção de erro em um FacesMessage", exception);
		EasyMock.expectLastCall().anyTimes();

		response.setContentType(ContentType.PDF.getContentType());
		EasyMock.expectLastCall().times(1);

		response.setContentLength(bytes.length);
		EasyMock.expectLastCall().times(1);

		response.setHeader("Content-Disposition", "filename=\"" + fileName + "\"");
		EasyMock.expectLastCall().times(1);

		facesContext.responseComplete();
		EasyMock.expectLastCall().times(1);

		try {
			EasyMock.expect(response.getOutputStream()).andThrow(exception);
		} catch (IOException e) {
			Assert.fail();
		}

		PowerMock.mockStatic(Faces.class);
		Faces.addMessage(exception);

		PowerMock.replayAll();
		renderer.render(stream, ContentType.PDF, fileName, false);
		PowerMock.verifyAll();
	}

	@Test
	public void testRenderStreamSuccess() throws IOException {
		byte[] bytes = "Test".getBytes();
		InputStream inputStream = new ByteArrayInputStream(bytes);

		String fileName = "fileName.pdf";

		logger.debug(EasyMock.anyObject(String.class));
		EasyMock.expectLastCall().anyTimes();

		facesContext.responseComplete();
		EasyMock.expectLastCall().times(1);

		response.setContentType(ContentType.PDF.getContentType());
		EasyMock.expectLastCall().times(1);

		response.setContentLength(bytes.length);
		EasyMock.expectLastCall().times(1);

		response.setHeader("Content-Disposition", "filename=\"" + fileName + "\"");
		EasyMock.expectLastCall().times(1);

		ServletOutputStream stream = new ServletOutputStream() {

			@Override
			public void write(int b) throws IOException {
				Assert.assertTrue(true);
			}
		};

		EasyMock.expect(response.getOutputStream()).andReturn(stream).times(3);

		PowerMock.replayAll();
		renderer.render(inputStream, ContentType.PDF, fileName);
		PowerMock.verifyAll();
	}

	@Test
	public void testRenderFileFail() throws IOException {

		File file = new File("fileName");
		file.createNewFile();

		String fileName = "fileName.pdf";

		logger.debug(EasyMock.anyObject(String.class));
		EasyMock.expectLastCall().anyTimes();

		IOException exception = new IOException();
		logger.info("Erro na geração do relatório. Incluíndo a exceção de erro em um FacesMessage", exception);
		EasyMock.expectLastCall().anyTimes();

		response.setContentType(ContentType.PDF.getContentType());
		EasyMock.expectLastCall().times(1);

		response.setContentLength((int) file.length());
		EasyMock.expectLastCall().times(1);

		response.setHeader("Content-Disposition", "filename=\"" + fileName + "\"");
		EasyMock.expectLastCall().times(1);

		facesContext.responseComplete();
		EasyMock.expectLastCall().times(1);

		try {
			EasyMock.expect(response.getOutputStream()).andThrow(exception);
		} catch (IOException e) {
			Assert.fail();
		}

		PowerMock.mockStatic(Faces.class);
		Faces.addMessage(exception);

		PowerMock.replayAll();
		renderer.render(file, ContentType.PDF, fileName);
		PowerMock.verifyAll();

		file.delete();
	}

	@Test
	public void testRenderFileNotFoundException() throws IOException {

		File file = new File("fileName");
		file.createNewFile();

		String fileName = "fileName.pdf";

		logger.debug(EasyMock.anyObject(String.class));
		EasyMock.expectLastCall().anyTimes();

		IOException exception = new IOException();
		logger.info("Erro na geração do relatório. Incluíndo a exceção de erro em um FacesMessage", exception);
		EasyMock.expectLastCall().anyTimes();

		response.setContentType(ContentType.PDF.getContentType());
		EasyMock.expectLastCall().times(1);

		response.setContentLength((int) file.length());
		EasyMock.expectLastCall().times(1);

		response.setHeader("Content-Disposition", "filename=\"" + fileName + "\"");
		EasyMock.expectLastCall().times(1);

		facesContext.responseComplete();
		EasyMock.expectLastCall().times(1);

		try {
			EasyMock.expect(response.getOutputStream()).andThrow(exception);
		} catch (IOException e) {
			Assert.fail();
		}
		
		

		PowerMock.mockStatic(Faces.class);
		Faces.addMessage(exception);

		PowerMock.replayAll();
		renderer.render(file, ContentType.PDF, fileName);
		PowerMock.verifyAll();

		file.delete();
	}
}
