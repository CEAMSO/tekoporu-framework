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

package org.ticpy.tekoporu.internal.interceptor;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;

import org.ticpy.tekoporu.DemoiselleException;
import org.ticpy.tekoporu.exception.ExceptionHandler;
import org.ticpy.tekoporu.internal.bootstrap.CoreBootstrap;
import org.ticpy.tekoporu.internal.producer.LoggerProducer;
import org.ticpy.tekoporu.internal.producer.ResourceBundleProducer;
import org.ticpy.tekoporu.stereotype.Controller;
import org.ticpy.tekoporu.util.Beans;
import org.ticpy.tekoporu.util.ResourceBundle;

@Interceptor
@Controller
public class ExceptionHandlerInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;

	private static ResourceBundle bundle;

	private static Logger logger;

	private final Map<Class<?>, Map<Class<?>, Method>> cache = new HashMap<Class<?>, Map<Class<?>, Method>>();

	private final boolean handleException(final Exception cause, final InvocationContext ic) throws Exception {
		getLogger().info(getBundle().getString("handling-exception", cause.getClass().getCanonicalName()));

		boolean handled = false;
		Class<?> type = getType(ic);

		if (!isLoaded(type)) {
			loadHandlers(type);
		}

		Method handler = getMethod(type, cause);
		if (handler != null) {
			invoke(handler, ic.getTarget(), cause);
			handled = true;
		}

		return handled;
	}

	private final Class<?> getType(final InvocationContext ic) {
		Class<?> type = ic.getTarget().getClass();
		CoreBootstrap bootstrap = Beans.getReference(CoreBootstrap.class);

		if (!bootstrap.isAnnotatedType(type)) {
			type = type.getSuperclass();
			getLogger().debug(
					getBundle().getString("proxy-detected", ic.getTarget().getClass(),
							ic.getTarget().getClass().getSuperclass()));
		}

		return type;
	}

	/**
	 * If there is an handler in the current class for the expected exception, then this method will be returned; Else
	 * returns null;
	 * 
	 * @param type
	 * @param cause
	 * @return
	 */
	private final Method getMethod(final Class<?> type, final Exception cause) {
		Method handler = null;

		if (cache.containsKey(type) && cache.get(type).containsKey(cause.getClass())) {
			handler = cache.get(type).get(cause.getClass());
		}

		return handler;
	}

	/**
	 * Create an map of Exception Handler for this class and put it on the cache.
	 * 
	 * @param type
	 */
	private final void loadHandlers(final Class<?> type) {
		Map<Class<?>, Method> mapHandlers = new HashMap<Class<?>, Method>();
		Method[] methods = type.getMethods();

		for (Method method : methods) {
			if (method.isAnnotationPresent(ExceptionHandler.class)) {
				validateHandler(method);
				mapHandlers.put(method.getParameterTypes()[0], method);
			}
		}
		cache.put(type, mapHandlers);
	}

	/**
	 * Verify the method for compliance with an handler. It must be: public, single parameter, parameter type must be
	 * assigned from Exception
	 * 
	 * @param method
	 */
	private final void validateHandler(final Method method) {
		if (method.getParameterTypes().length != 1) {
			throw new DemoiselleException(getBundle().getString("must-declare-one-single-parameter",
					method.toGenericString()));
		}
	}

	/**
	 * Indicates if this class is already loaded in cache control.
	 * 
	 * @param type
	 * @return
	 */
	private final boolean isLoaded(final Class<?> type) {
		return cache.containsKey(type);
	}

	private final void invoke(final Method method, final Object object, final Exception param) throws Exception {
		boolean accessible = method.isAccessible();
		method.setAccessible(true);

		try {
			method.invoke(object, param);

		} catch (InvocationTargetException cause) {
			Throwable targetTrowable = cause.getTargetException();

			if (targetTrowable instanceof Exception) {
				throw (Exception) targetTrowable;
			} else {
				throw new Exception(targetTrowable);
			}
		}

		method.setAccessible(accessible);
	}

	@AroundInvoke
	public Object manage(final InvocationContext ic) throws Exception {
		Object result = null;

		try {
			result = ic.proceed();

		} catch (Exception cause) {
			if (!handleException(cause, ic)) {
				throw cause;
			}
		}

		return result;
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
