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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import org.ticpy.tekoporu.util.Faces;
import org.ticpy.tekoporu.util.FileRenderer;

/**
 * Responsible for displaying the contents of files in the browser.
 */
public class FileRendererImpl implements FileRenderer {

	private static final long serialVersionUID = 7787266586182058798L;

	@Inject
	private HttpServletResponse response;

	@Inject
	private Logger logger;

	@Inject
	private FacesContext context;

	@Override
	public void render(final byte[] byteArray, final ContentType contentType, final String fileName, boolean forceDownload) {
		logger.debug("Renderizando para o arquivo " + fileName + ".");

		try {
			response.setContentType(contentType.getContentType());
			response.setContentLength(byteArray.length);
			
			String forceDownloadCommand = forceDownload ? "attachment; " : "";
			response.setHeader("Content-Disposition", forceDownloadCommand + "filename=\"" + fileName + "\"");

			logger.debug("Escrevendo o arquivo " + fileName + " no response.");
			response.getOutputStream().write(byteArray, 0, byteArray.length);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IOException e) {
			logger.info("Erro na geração do relatório. Incluíndo a exceção de erro em um FacesMessage", e);
			Faces.addMessage(e);
		}
		context.responseComplete();
	}
	
	@Override
	public void render(final byte[] byteArray, final ContentType contentType, final String fileName) {
		render(byteArray, contentType, fileName, false);
	}

	@Override
	public void render(final InputStream stream, final ContentType contentType, final String fileName, boolean forceDownload) {
		logger.debug("Renderizando o arquivo " + fileName + ".");
		render(getBytes(stream), contentType, fileName, forceDownload);
	}
	
	@Override
	public void render(final InputStream stream, final ContentType contentType, final String fileName) {
		render(stream, contentType, fileName, false);
	}

	@Override
	public void render(File file, ContentType contentType, String fileName, boolean forceDownload) {
		logger.debug("Renderizando para o arquivo " + fileName + ".");
		try {
			render(new FileInputStream(file), contentType, fileName, forceDownload);
		} catch (FileNotFoundException e) {
			logger.info("Erro na geração do relatório. Incluíndo a exceção de erro em um FacesMessage", e);
			Faces.addMessage(e);
		}
	}
	
	@Override
	public void render(File file, ContentType contentType, String fileName) {
		render(file, contentType, fileName, false);
	}

	private byte[] getBytes(InputStream stream) {
		byte[] byteArray = null;
		try {
			int thisLine;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			while ((thisLine = stream.read()) != -1) {
				bos.write(thisLine);
			}
			bos.flush();
			byteArray = bos.toByteArray();

			if (bos != null) {
				bos.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return byteArray;
	}

}
