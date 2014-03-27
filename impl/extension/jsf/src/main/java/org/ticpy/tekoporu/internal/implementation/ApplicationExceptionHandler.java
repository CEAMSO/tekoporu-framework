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

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.ticpy.tekoporu.DemoiselleException;
import org.ticpy.tekoporu.internal.configuration.ExceptionHandlerConfig;
import org.ticpy.tekoporu.util.Beans;
import org.ticpy.tekoporu.util.Exceptions;
import org.ticpy.tekoporu.util.Faces;
import org.ticpy.tekoporu.util.PageNotFoundException;
import org.ticpy.tekoporu.util.Redirector;

public class ApplicationExceptionHandler extends AbstractExceptionHandler {

	public ApplicationExceptionHandler(final ExceptionHandler wrapped) {
		super(wrapped);
	}

	protected boolean handleException(final Throwable cause, FacesContext facesContext) {
		ExceptionHandlerConfig config = Beans.getReference(ExceptionHandlerConfig.class);
		boolean handled = false;

		if (config.isHandleApplicationException() && Exceptions.isApplicationException(cause)) {

			if (isRendering(facesContext)) {
				handled = handlingDuringRenderResponse(cause, config);
			} else {
				Faces.addMessage(cause);
				handled = true;
			}
		}

		return handled;
	}

	private final boolean isRendering(FacesContext context) {
		return PhaseId.RENDER_RESPONSE.equals(context.getCurrentPhaseId());
	}

	/**
	 * In render response phase an exception interrupt the renderization. So this method will redirect the renderingo to
	 * an page configured in demoiselle.properties
	 * 
	 * @see ExceptionHandlerConfig
	 * @param cause
	 * @param config
	 * @return
	 */
	private final boolean handlingDuringRenderResponse(final Throwable cause, final ExceptionHandlerConfig config) {
		boolean handled = false;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("exception", cause.getMessage());
			Redirector.redirect(config.getExceptionPage(), map);
			handled = true;
		} catch (PageNotFoundException ex) {
			// TODO Colocar a mensagem no bundle
			throw new DemoiselleException(
					"A tela de exibição de erros: \""
							+ ex.getViewId()
							+ "\" não foi encontrada. Caso o seu projeto possua outra, defina no arquivo de configuração a chave \""
							+ "frameworkdemoiselle.handle.application.exception.page" + "\"", ex);
		}
		return handled;
	}
}
