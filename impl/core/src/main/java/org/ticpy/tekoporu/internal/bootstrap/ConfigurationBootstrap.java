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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.ticpy.tekoporu.configuration.Configuration;
import org.ticpy.tekoporu.internal.implementation.ConfigurationImpl;

public class ConfigurationBootstrap implements Extension {

	private final List<Class<Object>> cache = Collections.synchronizedList(new ArrayList<Class<Object>>());

	private static final Map<ClassLoader, Map<String, Class<Object>>> cacheClassLoader = Collections
			.synchronizedMap(new HashMap<ClassLoader, Map<String, Class<Object>>>());

	public void processAnnotatedType(@Observes final ProcessAnnotatedType<Object> event) {
		final AnnotatedType<Object> annotatedType = event.getAnnotatedType();

		if (annotatedType.getJavaClass().isAnnotationPresent(Configuration.class)) {
			cache.add(annotatedType.getJavaClass());
			event.veto();
		}
	}

	public void afterBeanDiscovery(@Observes AfterBeanDiscovery event, BeanManager beanManager) throws Exception {
		Class<Object> proxy;

		for (Class<Object> config : cache) {
			proxy = createProxy(config);
			event.addBean(new CustomBean(proxy, beanManager));
		}
	}

	@SuppressWarnings("unchecked")
	private Class<Object> createProxy(Class<Object> type) throws Exception {
		String superClassName = type.getCanonicalName();
		String chieldClassName = superClassName + "__DemoiselleProxy";

		Map<String, Class<Object>> cacheProxy = Collections.synchronizedMap(new HashMap<String, Class<Object>>());;

		Class<Object> clazzProxy = null;

		ClassLoader classLoader = type.getClassLoader();
		if (cacheClassLoader.containsKey(classLoader)) {
			cacheProxy = cacheClassLoader.get(classLoader);
			if (cacheProxy.containsKey(chieldClassName)) {
				clazzProxy = cacheProxy.get(chieldClassName);
			}
		}

		if (clazzProxy == null) {

			ClassPool pool = new ClassPool();
			CtClass ctChieldClass = pool.getOrNull(chieldClassName);

			pool.appendClassPath(new LoaderClassPath(classLoader));
			CtClass ctSuperClass = pool.get(superClassName);

			ctChieldClass = pool.getAndRename(ConfigurationImpl.class.getCanonicalName(), chieldClassName);
			ctChieldClass.setSuperclass(ctSuperClass);

			CtMethod ctChieldMethod;
			for (CtMethod ctSuperMethod : ctSuperClass.getDeclaredMethods()) {
				ctChieldMethod = CtNewMethod.delegator(ctSuperMethod, ctChieldClass);
				ctChieldMethod.insertBefore("load(this);");

				ctChieldClass.addMethod(ctChieldMethod);
			}

			clazzProxy = ctChieldClass.toClass(classLoader, type.getProtectionDomain());

			cacheProxy.put(chieldClassName, clazzProxy);
			cacheClassLoader.put(classLoader, cacheProxy);
		}

		return clazzProxy;
	}
}
