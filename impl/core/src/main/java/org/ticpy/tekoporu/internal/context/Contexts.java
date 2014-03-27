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

package org.ticpy.tekoporu.internal.context;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.inject.spi.AfterBeanDiscovery;

import org.slf4j.Logger;

import org.ticpy.tekoporu.internal.producer.LoggerProducer;
import org.ticpy.tekoporu.internal.producer.ResourceBundleProducer;
import org.ticpy.tekoporu.util.ResourceBundle;

public class Contexts {

	private static List<CustomContext> activeContexts = Collections.synchronizedList(new ArrayList<CustomContext>());

	private static List<CustomContext> inactiveContexts = Collections.synchronizedList(new ArrayList<CustomContext>());

	private static Logger logger;

	private static ResourceBundle bundle;

	private static Logger getLogger() {
		if (logger == null) {
			logger = LoggerProducer.create(Contexts.class);
		}

		return logger;
	}

	private static ResourceBundle getBundle() {
		if (bundle == null) {
			bundle = ResourceBundleProducer.create("demoiselle-core-bundle");
		}

		return bundle;
	}

	private Contexts() {
	}

	public static synchronized void add(CustomContext context, AfterBeanDiscovery event) {
		Class<? extends Annotation> scope = context.getScope();

		getLogger()
				.trace(getBundle().getString("custom-context-was-registered", context.getScope().getCanonicalName()));

		if (get(scope, activeContexts) != null) {
			inactiveContexts.add(context);
			context.setActive(false);

		} else {
			activeContexts.add(context);
			context.setActive(true);
		}

		if (event != null) {
			event.addContext(context);
		}
	}

	private static CustomContext get(Class<? extends Annotation> scope, List<CustomContext> contexts) {
		CustomContext result = null;

		for (CustomContext context : contexts) {
			if (scope.equals(context.getScope())) {
				result = context;
				break;
			}
		}

		return result;
	}

	public static synchronized void remove(CustomContext context) {
		getLogger().trace(
				getBundle().getString("custom-context-was-unregistered", context.getScope().getCanonicalName()));

		if (activeContexts.contains(context)) {
			activeContexts.remove(context);
			context.setActive(false);

			CustomContext inactive = get(context.getScope(), inactiveContexts);
			if (inactive != null) {
				activeContexts.add(inactive);
				inactive.setActive(true);
				inactiveContexts.remove(inactive);
			}

		} else if (inactiveContexts.contains(context)) {
			inactiveContexts.remove(context);
		}
	}

	public static synchronized void clear() {
		CustomContext context;
		for (Iterator<CustomContext> iter = activeContexts.iterator(); iter.hasNext();) {
			context = iter.next();
			context.setActive(false);
			iter.remove();
		}

		activeContexts.clear();
		inactiveContexts.clear();
	}

	public static synchronized List<CustomContext> getActiveContexts() {
		return activeContexts;
	}

	public static synchronized List<CustomContext> getInactiveContexts() {
		return inactiveContexts;
	}
}
