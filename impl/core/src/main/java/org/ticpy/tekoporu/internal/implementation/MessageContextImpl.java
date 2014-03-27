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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import org.slf4j.Logger;

import org.ticpy.tekoporu.internal.interceptor.ExceptionHandlerInterceptor;
import org.ticpy.tekoporu.internal.producer.LoggerProducer;
import org.ticpy.tekoporu.internal.producer.ResourceBundleProducer;
import org.ticpy.tekoporu.message.DefaultMessage;
import org.ticpy.tekoporu.message.Message;
import org.ticpy.tekoporu.message.MessageContext;
import org.ticpy.tekoporu.message.SeverityType;
import org.ticpy.tekoporu.util.ResourceBundle;

/**
 * The message store is designed to provide access to messages. It is shared by every application layer.
 * 
 * @see MessageContext
 */
@RequestScoped
public class MessageContextImpl implements Serializable, MessageContext {

	private static final long serialVersionUID = 1L;

	private final List<Message> messages = new ArrayList<Message>();

	private static ResourceBundle bundle;

	private static Logger logger;

	@Override
	public void add(final Message message, Object... params) {
		Message aux;

		if (params != null) {
			aux = new DefaultMessage(message.getText(), message.getSeverity(), params);
		} else {
			aux = message;
		}

		getLogger().debug(getBundle().getString("adding-message-to-context", message.toString()));
		messages.add(aux);
	}

	@Override
	public void add(String text, Object... params) {
		add(text, null, params);
	}

	@Override
	public void add(String text, SeverityType severity, Object... params) {
		add(new DefaultMessage(text, severity, params));
	}

	@Override
	public List<Message> getMessages() {
		return messages;
	}

	@Override
	public void clear() {
		getLogger().debug(getBundle().getString("cleaning-message-context"));
		messages.clear();
	}

	private static ResourceBundle getBundle() {
		if (bundle == null) {
			bundle = ResourceBundleProducer.create("demoiselle-core-bundle");
		}

		return bundle;
	}

	private static Logger getLogger() {
		if (logger == null) {
			logger = LoggerProducer.create(ExceptionHandlerInterceptor.class);
		}

		return logger;
	}
}
