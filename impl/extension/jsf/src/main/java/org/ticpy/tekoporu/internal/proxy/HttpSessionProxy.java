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

package org.ticpy.tekoporu.internal.proxy;

import java.io.Serializable;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

@SuppressWarnings("deprecation")
public class HttpSessionProxy implements HttpSession, Serializable {

	private static final long serialVersionUID = 1L;

	private transient final HttpSession delegate;

	public HttpSessionProxy(HttpSession httpSession) {
		this.delegate = httpSession;
	}

	@Override
	public long getCreationTime() {
		return this.delegate.getCreationTime();
	}

	@Override
	public String getId() {
		return this.delegate.getId();
	}

	@Override
	public long getLastAccessedTime() {
		return this.delegate.getLastAccessedTime();
	}

	@Override
	public ServletContext getServletContext() {
		return this.delegate.getServletContext();
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		this.delegate.setMaxInactiveInterval(interval);
	}

	@Override
	public int getMaxInactiveInterval() {
		return this.delegate.getMaxInactiveInterval();
	}

	@Override
	@Deprecated
	public HttpSessionContext getSessionContext() {
		return this.delegate.getSessionContext();
	}

	@Override
	public Object getAttribute(String name) {
		return this.delegate.getAttribute(name);
	}

	@Override
	@Deprecated
	public Object getValue(String name) {
		return this.delegate.getValue(name);
	}

	@Override
	public Enumeration<?> getAttributeNames() {
		return this.delegate.getAttributeNames();
	}

	@Override
	@Deprecated
	public String[] getValueNames() {
		return this.delegate.getValueNames();
	}

	@Override
	public void setAttribute(String name, Object value) {
		this.delegate.setAttribute(name, value);
	}

	@Override
	@Deprecated
	public void putValue(String name, Object value) {
		this.delegate.putValue(name, value);
	}

	@Override
	public void removeAttribute(String name) {
		this.delegate.removeAttribute(name);
	}

	@Override
	@Deprecated
	public void removeValue(String name) {
		this.delegate.removeValue(name);
	}

	@Override
	public void invalidate() {
		this.delegate.invalidate();
	}

	@Override
	public boolean isNew() {
		return this.delegate.isNew();
	}

}
