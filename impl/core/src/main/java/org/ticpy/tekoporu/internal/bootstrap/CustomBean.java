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

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.NormalScope;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Stereotype;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;
import javax.inject.Qualifier;
import javax.inject.Scope;

import org.ticpy.tekoporu.util.Beans;

/**
 * @see http://docs.jboss.org/weld/reference/latest/en-US/html_single/#d0e5035
 */
public class CustomBean implements Bean<Object>, Serializable {

	private static final long serialVersionUID = 1L;

	private Class<Object> beanClass;

	private transient InjectionTarget<Object> injectionTarget;

	private transient BeanManager beanManager;

	private InjectionTarget<Object> getInjectionTarget() {
		if (this.injectionTarget == null) {
			AnnotatedType<Object> annotatedType = getBeanManager().createAnnotatedType(beanClass);
			this.injectionTarget = getBeanManager().createInjectionTarget(annotatedType);
		}

		return this.injectionTarget;
	}

	private BeanManager getBeanManager() {
		if (this.beanManager == null) {
			this.beanManager = Beans.getBeanManager();
		}

		return this.beanManager;
	}

	public CustomBean(Class<Object> beanClass, BeanManager beanManager) {
		this.beanClass = beanClass;
		this.beanManager = beanManager;
	}

	public Object create(CreationalContext<Object> creationalContext) {
		Object instance = getInjectionTarget().produce(creationalContext);
		getInjectionTarget().inject(instance, creationalContext);
		getInjectionTarget().postConstruct(instance);

		return instance;
	}

	public void destroy(Object instance, CreationalContext<Object> creationalContext) {
		getInjectionTarget().preDestroy(instance);
		getInjectionTarget().dispose(instance);
		creationalContext.release();
	}

	public Set<Type> getTypes() {
		Set<Type> types = new HashSet<Type>();
		types.add(beanClass.getSuperclass());
		types.add(Object.class);

		return types;
	}

	@SuppressWarnings("serial")
	public Set<Annotation> getQualifiers() {
		Set<Annotation> result = new HashSet<Annotation>();

		result.add(new AnnotationLiteral<Default>() {
		});
		result.add(new AnnotationLiteral<Any>() {
		});

		for (Annotation annotation : beanClass.getAnnotations()) {
			if (annotation.getClass().isAnnotationPresent(Qualifier.class)) {
				result.add(annotation);
			}
		}

		return result;
	}

	public Class<? extends Annotation> getScope() {
		Class<? extends Annotation> result = Dependent.class;

		Class<? extends Annotation> annotationClass;
		for (Annotation annotation : beanClass.getAnnotations()) {
			annotationClass = annotation.getClass();

			if (annotationClass.isAnnotationPresent(Scope.class)
					|| annotationClass.isAnnotationPresent(NormalScope.class)) {
				result = annotationClass;
				break;
			}
		}

		return result;
	}

	public String getName() {
		String result = null;

		if (beanClass.isAnnotationPresent(Named.class)) {
			result = beanClass.getAnnotation(Named.class).value();
		}

		return result;
	}

	public Set<Class<? extends Annotation>> getStereotypes() {
		Set<Class<? extends Annotation>> result = new HashSet<Class<? extends Annotation>>();

		for (Annotation annotation : beanClass.getAnnotations()) {
			if (annotation.getClass().isAnnotationPresent(Stereotype.class)) {
				result.add(annotation.getClass());
			}
		}

		return result;
	}

	public Class<Object> getBeanClass() {
		return beanClass;
	}

	public boolean isAlternative() {
		return beanClass.isAnnotationPresent(Alternative.class);
	}

	public boolean isNullable() {
		return false;
	}

	public Set<InjectionPoint> getInjectionPoints() {
		return getInjectionTarget().getInjectionPoints();
	}
}
