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
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.ticpy.tekoporu.annotation.Name;
import org.ticpy.tekoporu.annotation.ViewScoped;
import org.ticpy.tekoporu.util.Beans;
import org.ticpy.tekoporu.util.Faces;
import org.ticpy.tekoporu.util.Parameter;
import org.ticpy.tekoporu.util.Reflections;

public class ParameterImpl<T extends Serializable> implements Parameter<T>, Serializable {

	private static final long serialVersionUID = 1L;

	private Class<Object> type;

	private transient Converter converter;

	private T value;

	private final String key;

	private boolean viewScoped = false;

	private boolean requestScoped = false;

	private boolean sessionScoped = false;

	public Converter getConverter() {
		if (converter == null) {
			converter = Faces.getConverter(type);
		}

		return converter;
	}

	private HttpServletRequest getRequest() {
		return Beans.getReference(HttpServletRequest.class);
	}

	@Inject
	public ParameterImpl(InjectionPoint ip) {
		if (ip.getAnnotated().isAnnotationPresent(Name.class)) {
			this.key = ip.getAnnotated().getAnnotation(Name.class).value();
		} else {
			this.key = ip.getMember().getName();
		}

		this.type = Reflections.getGenericTypeArgument(ip.getMember(), 0);

		this.viewScoped = ip.getAnnotated().isAnnotationPresent(ViewScoped.class);
		this.requestScoped = ip.getAnnotated().isAnnotationPresent(RequestScoped.class);
		this.sessionScoped = ip.getAnnotated().isAnnotationPresent(SessionScoped.class);
	}

	public String getKey() {
		return key;
	}

	private boolean isSessionScoped() {
		return sessionScoped;
	}

	private boolean isViewScoped() {
		return viewScoped;
	}

	private boolean isRequestScoped() {
		return requestScoped;
	}

	@SuppressWarnings("unchecked")
	public T getValue() {
		T result = null;
		String parameterValue = getRequest().getParameter(key);

		if (isSessionScoped()) {
			if (parameterValue != null) {
				getRequest().getSession().setAttribute(key, Faces.convert(parameterValue, getConverter()));
			}

			result = (T) getRequest().getSession().getAttribute(key);

		} else if (isRequestScoped()) {
			result = (T) Faces.convert(parameterValue, getConverter());

		} else if (isViewScoped()) {
			Map<String, Object> viewMap = Faces.getViewMap();
			if (parameterValue != null) {
				viewMap.put(key, Faces.convert(parameterValue, getConverter()));
			}

			result = (T) viewMap.get(key);

		} else {
			if (value == null) {
				value = (T) Faces.convert(parameterValue, getConverter());
			}

			result = value;
		}

		return result;
	}

	@Override
	public void setValue(T value) {
		if (isSessionScoped()) {
			getRequest().getSession().setAttribute(key, value);

		} else if (isRequestScoped()) {
			// FIXME Lançar exceção informando que não é possível setar parâmetros no request.

		} else if (isViewScoped()) {
			Map<String, Object> viewMap = Faces.getViewMap();
			viewMap.put(key, value);

		} else {
			this.value = value;
		}
	}
}
