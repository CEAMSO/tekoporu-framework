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

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import javax.enterprise.inject.spi.AnnotatedMethod;

import org.slf4j.Logger;

import org.ticpy.tekoporu.annotation.Priority;
import org.ticpy.tekoporu.exception.ApplicationException;
import org.ticpy.tekoporu.internal.producer.LoggerProducer;
import org.ticpy.tekoporu.internal.producer.ResourceBundleProducer;
import org.ticpy.tekoporu.message.SeverityType;
import org.ticpy.tekoporu.util.Beans;
import org.ticpy.tekoporu.util.ResourceBundle;

/**
 * Represents an annotated method to be processed;
 * 
 * @param <T>
 *            declaring class owner of the method
 */
public class AnnotatedMethodProcessor<T> implements Comparable<AnnotatedMethodProcessor<T>> {

	private AnnotatedMethod<T> annotatedMethod;

	private ResourceBundle bundle;

	public AnnotatedMethodProcessor(final AnnotatedMethod<T> annotatedMethod) {
		this.annotatedMethod = annotatedMethod;
	}

	public AnnotatedMethod<T> getAnnotatedMethod() {
		return this.annotatedMethod;
	}

	@SuppressWarnings("unchecked")
	protected T getReferencedBean() {
		Class<T> classType = (Class<T>) getAnnotatedMethod().getJavaMember().getDeclaringClass();

		return Beans.getReference(classType);
	}

	public int compareTo(final AnnotatedMethodProcessor<T> other) {
		Integer orderThis = getPriority(getAnnotatedMethod());
		Integer orderOther = getPriority(other.getAnnotatedMethod());

		return orderThis.compareTo(orderOther);
	}

	public boolean process(Object... args) throws Throwable {
		getLogger().info(getBundle().getString("processing", getAnnotatedMethod().getJavaMember().toGenericString()));

		try {
			getAnnotatedMethod().getJavaMember().invoke(getReferencedBean(), args);

		} catch (InvocationTargetException cause) {
			handleException(cause.getCause());
		}

		return true;
	}

	private void handleException(Throwable cause) throws Throwable {
		ApplicationException ann = cause.getClass().getAnnotation(ApplicationException.class);

		if (ann == null || SeverityType.FATAL == ann.severity()) {
			throw cause;

		} else {
			switch (ann.severity()) {
				case INFO:
					getLogger().info(cause.getMessage());
					break;

				case WARN:
					getLogger().warn(cause.getMessage());
					break;

				default:
					getLogger().error(getBundle().getString("processing-fail"), cause);
					break;
			}
		}
	}

	private static <T> Integer getPriority(AnnotatedMethod<T> annotatedMethod) {
		Integer priority = Priority.MIN_PRIORITY;

		Priority annotation = annotatedMethod.getAnnotation(Priority.class);
		if (annotation != null) {
			priority = annotation.value();
		}

		return priority;
	}

	// @Override
	// public String toString() {
	// return getBundle().getString("for", getClass().getSimpleName(),
	// getAnnotatedMethod().getJavaMember().toGenericString());
	// }

	protected ResourceBundle getBundle() {
		if (this.bundle == null) {
			this.bundle = ResourceBundleProducer.create("demoiselle-core-bundle", Locale.getDefault());
		}

		return bundle;
	}

	protected Logger getLogger() {
		return LoggerProducer.create(this.getClass());
	}
}
