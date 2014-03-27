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

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Defines the default behavior of the objects responsible for rendering files to the user.
 * 
 * @author SERPRO
 */
public interface FileRenderer extends Serializable {

	/**
	 * Renders a byteArray for display to the user.
	 * 
	 * @param byteArray Byte Array to be rendered.
	 * @param contentType 
	 * @param fileName
	 * @param forceDownload If true, will force a download from the browser, otherwise the browser is free to determine what will happen with the rendered content. 
	 */
	void render(final byte[] byteArray, final ContentType contentType, final String fileName, boolean forceDownload);
	
	/**
	 * Renders a byteArray for display to the user.
	 * 
	 * @param byteArray Byte Array to be rendered.
	 * @param contentType 
	 * @param fileName
	 *  
	 */
	void render(final byte[] byteArray, final ContentType contentType, final String fileName);

	/**
	 * Renders an inputStream for display to the user.
	 * 
	 * @param stream
	 * @param contentType
	 * @param fileName
	 */
	void render(final InputStream stream, final ContentType contentType, final String fileName);
	
	/**
	 * Renders an inputStream for display to the user.
	 * 
	 * @param stream
	 * @param contentType
	 * @param fileName
	 * @param forceDownload If true, will force a download from the browser, otherwise the browser is free to determine what will happen with the rendered content.
	 */
	void render(final InputStream stream, final ContentType contentType, final String fileName, boolean forceDownload);

	/**
	 * Renders a file for display to the user.
	 * 
	 * @param file
	 * @param contentType
	 * @param fileName
	 */
	void render(final File file, final ContentType contentType, final String fileName);
	
	/**
	 * Renders a file for display to the user.
	 * 
	 * @param file
	 * @param contentType
	 * @param fileName
	 * @param forceDownload If true, will force a download from the browser, otherwise the browser is free to determine what will happen with the rendered content.
	 */
	void render(final File file, final ContentType contentType, final String fileName, boolean forceDownload);

	/**
	 * File content type.
	 * 
	 * @author SERPRO
	 */
	public enum ContentType {
		CSV("text/plain"),
		HTML("text/html"),
		ODT("application/vnd.oasis.opendocument.text"),
		PDF("application/pdf"),
		RTF("application/rtf"),
		TXT("text/plain"),
		XLS("application/vnd.ms-excel");	
		
		private String contentType;	
		
		/**
		 * Constructor receiving the fields alias and content type.
		 * @param alias Alias of content type
		 * @param contentType Value of content type
		 */
		private ContentType(String contentType){
			this.contentType = contentType;
		}
		
		/**
		 * Return the content type of Type
		 * @return Content Type
		 */
		public String getContentType() {
			return contentType;
		}	
		
	}

}
