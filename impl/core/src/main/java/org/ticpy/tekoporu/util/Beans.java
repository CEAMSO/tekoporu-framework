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

import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.ticpy.tekoporu.DemoiselleException;
import org.ticpy.tekoporu.internal.producer.ResourceBundleProducer;

public final class Beans {

	private static BeanManager manager;

	private Beans() {
	}

	public static void setBeanManager(BeanManager beanManager) {
		manager = beanManager;
	}

	public static BeanManager getBeanManager() {
		return manager;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getReference(final Class<T> beanClass, Annotation... qualifiers) {
		T instance;

		try {
			instance = (T) getReference(manager.getBeans(beanClass, qualifiers));

		} catch (NoSuchElementException cause) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(beanClass.getCanonicalName());

			for (Annotation qualifier : qualifiers) {
				buffer.append(", ");
				buffer.append(qualifier.getClass().getCanonicalName());
			}

			String message = getBundle().getString("bean-not-found", buffer.toString());
			throw new DemoiselleException(message, cause);
		}

		return instance;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getReference(final Class<T> beanClass) {
		T instance;

		try {
			instance = (T) getReference(manager.getBeans(beanClass));

		} catch (NoSuchElementException cause) {
			String message = getBundle().getString("bean-not-found", beanClass.getCanonicalName());
			throw new DemoiselleException(message, cause);
		}

		return instance;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getReference(String beanName) {
		T instance;

		try {
			instance = (T) getReference(manager.getBeans(beanName));

		} catch (NoSuchElementException cause) {
			String message = getBundle().getString("bean-not-found", beanName);
			throw new DemoiselleException(message, cause);
		}

		return instance;
	}

	@SuppressWarnings("unchecked")
	private static <T> T getReference(Set<Bean<?>> beans) {
		Bean<?> bean = beans.iterator().next();
		return (T) manager.getReference(bean, bean.getBeanClass(), manager.createCreationalContext(bean));
	}

	private static ResourceBundle getBundle() {
		return ResourceBundleProducer.create("demoiselle-core-bundle", Locale.getDefault());
	}
}
