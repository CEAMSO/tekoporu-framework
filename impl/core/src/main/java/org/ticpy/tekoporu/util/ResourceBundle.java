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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;

public class ResourceBundle extends java.util.ResourceBundle implements Serializable {

	private static final long serialVersionUID = 1L;

	private String baseName;

	private transient java.util.ResourceBundle delegate;

	private final Locale locale;

	private java.util.ResourceBundle getDelegate() {
		if (delegate == null) {
			try {
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				delegate = ResourceBundle.getBundle(baseName, locale, classLoader);

			} catch (MissingResourceException mre) {
				delegate = ResourceBundle.getBundle(baseName, locale);
			}
		}

		return delegate;
	}

	public ResourceBundle(String baseName, Locale locale) {
		this.baseName = baseName;
		this.locale = locale;
	}

	@Override
	public boolean containsKey(String key) {
		return getDelegate().containsKey(key);
	}

	@Override
	public Enumeration<String> getKeys() {
		return getDelegate().getKeys();
	}

	@Override
	public Locale getLocale() {
		return getDelegate().getLocale();
	}

	@Override
	public Set<String> keySet() {
		return getDelegate().keySet();
	}

	public String getString(String key, Object... params) {
		return Strings.getString(getString(key), params);
	}

	@Override
	protected Object handleGetObject(String key) {
		Object result;

		try {
			Method method = getDelegate().getClass().getMethod("handleGetObject", String.class);

			method.setAccessible(true);
			result = method.invoke(delegate, key);
			method.setAccessible(false);

		} catch (Exception cause) {
			throw new RuntimeException(cause);
		}

		return result;
	}
}
