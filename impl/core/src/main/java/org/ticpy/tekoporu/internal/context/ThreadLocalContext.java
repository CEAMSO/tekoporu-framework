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

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

public class ThreadLocalContext implements CustomContext {

	private final ThreadLocal<ContextStore> threadLocal = new ThreadLocal<ContextStore>();

	private boolean active;

	private final Class<? extends Annotation> scope;

	public ThreadLocalContext(final Class<? extends Annotation> scope) {
		this(scope, true);
	}

	public ThreadLocalContext(final Class<? extends Annotation> scope,
			boolean active) {
		this.scope = scope;
		this.active = active;
	}

	@Override
	public <T> T get(final Contextual<T> contextual) {
		return get(contextual, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(final Contextual<T> contextual,
			final CreationalContext<T> creationalContext) {
		T instance = null;

		if (!isActive()) {
			throw new ContextNotActiveException();
		}

		String id = getId(contextual);
		if (getStore().contains(id)) {
			instance = (T) getStore().get(id);

		} else if (creationalContext != null) {
			instance = contextual.create(creationalContext);
			getStore().put(id, instance);
		}

		return instance;
	}

	private <T> String getId(final Contextual<T> contextual) {
		Bean<T> bean = (Bean<T>) contextual;
		return bean.getBeanClass().getCanonicalName();
	}

	@Override
	public Class<? extends Annotation> getScope() {
		return this.scope;
	}

	private ContextStore getStore() {
		if (this.threadLocal.get() == null) {
			this.threadLocal.set(new ContextStore());
		}

		return this.threadLocal.get();
	}

	@Override
	public boolean isActive() {
		return this.active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

}
