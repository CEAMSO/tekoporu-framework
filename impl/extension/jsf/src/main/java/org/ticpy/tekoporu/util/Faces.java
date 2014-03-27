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

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_FATAL;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import static javax.faces.application.FacesMessage.SEVERITY_WARN;

import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.ticpy.tekoporu.exception.ApplicationException;
import org.ticpy.tekoporu.message.Message;
import org.ticpy.tekoporu.message.SeverityType;

public class Faces {

	public static void addMessages(final List<Message> messages) {
		if (messages != null) {
			for (Message m : messages) {
				addMessage(m);
			}
		}
	}

	public static void addMessage(final Message message) {
		getFacesContext().addMessage(null, parse(message));
	}

	public static void addMessage(final String clientId, final Message message) {
		getFacesContext().addMessage(clientId, parse(message));
	}

	public static void addMessage(final String clientId, final Throwable throwable) {
		getFacesContext().addMessage(clientId, parse(throwable));
	}

	public static void addMessage(final Throwable throwable) {
		addMessage(null, throwable);
	}

	private static FacesContext getFacesContext() {
		return Beans.getReference(FacesContext.class);
	}

	public static Severity parse(final SeverityType severityType) {
		Severity result = null;

		switch (severityType) {
			case INFO:
				result = SEVERITY_INFO;
				break;
			case WARN:
				result = SEVERITY_WARN;
				break;
			case ERROR:
				result = SEVERITY_ERROR;
				break;
			case FATAL:
				result = SEVERITY_FATAL;
		}

		return result;
	}

	public static FacesMessage parse(final Throwable throwable) {
		FacesMessage facesMessage = new FacesMessage();
		ApplicationException annotation = throwable.getClass().getAnnotation(ApplicationException.class);

		if (annotation != null) {
			facesMessage.setSeverity(parse(annotation.severity()));
		} else {
			facesMessage.setSeverity(SEVERITY_ERROR);
		}

		if (throwable.getMessage() != null) {
			facesMessage.setSummary(throwable.getMessage());
		} else {
			facesMessage.setSummary(throwable.toString());
		}

		return facesMessage;
	}

	public static FacesMessage parse(final Message message) {
		FacesMessage facesMessage = new FacesMessage();
		facesMessage.setSeverity(parse(message.getSeverity()));
		facesMessage.setSummary(message.getText());
		return facesMessage;
	}

	public static Object convert(final String value, final Converter converter) {
		Object result = null;

		if (!Strings.isEmpty(value)) {
			if (converter != null) {
				result = converter.getAsObject(getFacesContext(), getFacesContext().getViewRoot(), value);
			} else {
				result = new String(value);
			}
		}

		return result;
	}

	public static Converter getConverter(Class<?> targetClass) {
		Converter result;

		try {
			Application application = getFacesContext().getApplication();
			result = application.createConverter(targetClass);

		} catch (Exception e) {
			result = null;
		}

		return result;
	}

	public static Map<String, Object> getViewMap() {
		UIViewRoot viewRoot = getFacesContext().getViewRoot();
		return viewRoot.getViewMap(true);
	}

}
