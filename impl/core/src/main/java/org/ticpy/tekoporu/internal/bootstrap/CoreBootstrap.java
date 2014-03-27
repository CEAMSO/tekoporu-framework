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

package org.ticpy.tekoporu.internal.bootstrap;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.slf4j.Logger;

import org.ticpy.tekoporu.internal.producer.LoggerProducer;
import org.ticpy.tekoporu.internal.producer.ResourceBundleProducer;
import org.ticpy.tekoporu.util.Beans;
import org.ticpy.tekoporu.util.ResourceBundle;

public class CoreBootstrap implements Extension {

	private final Map<Class<?>, AnnotatedType<?>> beans = new HashMap<Class<?>, AnnotatedType<?>>();

	private Logger logger;

	private ResourceBundle bundle;

	private Logger getLogger() {
		if (this.logger == null) {
			this.logger = LoggerProducer.create(CoreBootstrap.class);
		}

		return this.logger;
	}

	private ResourceBundle getBundle() {
		if (this.bundle == null) {
			this.bundle = ResourceBundleProducer.create("demoiselle-core-bundle", Locale.getDefault());
		}

		return this.bundle;
	}

	public boolean isAnnotatedType(Class<?> type) {
		return beans.containsKey(type);
	}

	public void engineOn(@Observes final BeforeBeanDiscovery event, BeanManager beanManager) {
		String description;
		Logger log = getLogger();

		description = getBundle().getString("engine-on");
		log.info(description);

		Beans.setBeanManager(beanManager);

		description = getBundle().getString("setting-up-bean-manager", Beans.class.getCanonicalName());
		log.info(description);
	}

	protected <T> void detectAnnotation(@Observes final ProcessAnnotatedType<T> event) {
		beans.put(event.getAnnotatedType().getJavaClass(), event.getAnnotatedType());
	}

	public void takeOff(@Observes final AfterDeploymentValidation event) {
		String description = getBundle().getString("taking-off");

		Logger log = getLogger();
		log.info(description);
	}

	public void engineOff(@Observes final BeforeShutdown event) {
		String description = getBundle().getString("engine-off");

		Logger log = getLogger();
		log.info(description);
	}
}
