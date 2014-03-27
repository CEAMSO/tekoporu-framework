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

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.slf4j.Logger;

import org.ticpy.tekoporu.internal.producer.LoggerProducer;
import org.ticpy.tekoporu.message.MessageContext;
import org.ticpy.tekoporu.util.Beans;
import org.ticpy.tekoporu.util.Faces;

/**
 * This class is a JSF phase listener intended to transfer messages from Demoiselle Context to JSF own context.
 * 
 * @author SERPRO
 */
public class MessagePhaseListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	// FIXME: por que não funciona injeção disso aqui?
	// @Inject
	// private Logger logger;
	private final Logger logger = LoggerProducer.create(this.getClass());

	// TODO: usar o bundle nas mensagens de log
	// @Inject
	// @Name("demoiselle-core-bundle")
	// private ResourceBundle bundle;

	public void beforePhase(PhaseEvent e) {
		transferMessages(e);
	}

	public void afterPhase(PhaseEvent e) {
	}

	/**
	 * Transfers messages from a context to another.
	 * 
	 * @param e
	 *            PhaseEvent
	 */
	private void transferMessages(PhaseEvent e) {

		logger.debug(this.getClass().getSimpleName() + " " + e.getPhaseId());

		MessageContext messageContext = Beans.getReference(MessageContext.class);

		logger.debug("Moving " + messageContext.getMessages().size()
				+ " message(s) from MessageContext to FacesContext.");

		Faces.addMessages(messageContext.getMessages());
		messageContext.clear();
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}
