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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class Slf4jLoggerProxy implements Logger, Serializable {

	private static final long serialVersionUID = 1L;

	private transient Logger delegate;

	private final Class<?> type;
	
	private Logger getDelegate() {
		if(delegate == null) {
			delegate = LoggerFactory.getLogger(type);
		}
		
		return delegate;
	}

	public Slf4jLoggerProxy(final Class<?> type) {
		this.type = type;
	}

	@Override
	public void debug(final Marker marker, final String msg) {
		getDelegate().debug(marker, msg);
	}

	@Override
	public void debug(final Marker marker, final String format, final Object arg) {
		getDelegate().debug(marker, format, arg);
	}

	@Override
	public void debug(final Marker marker, final String format, final Object arg1, final Object arg2) {
		getDelegate().debug(marker, format, arg1, arg2);
	}

	@Override
	public void debug(final Marker marker, final String format, final Object[] argArray) {
		getDelegate().debug(marker, format, argArray);
	}

	@Override
	public void debug(final Marker marker, final String msg, final Throwable t) {
		getDelegate().debug(marker, msg, t);
	}

	@Override
	public void debug(final String msg) {
		getDelegate().debug(msg);
	}

	@Override
	public void debug(final String format, final Object arg) {
		getDelegate().debug(format, arg);
	}

	@Override
	public void debug(final String format, final Object arg1, final Object arg2) {
		getDelegate().debug(format, arg1, arg2);
	}

	@Override
	public void debug(final String format, final Object[] argArray) {
		getDelegate().debug(format, argArray);
	}

	@Override
	public void debug(final String msg, final Throwable t) {
		getDelegate().debug(msg, t);
	}

	@Override
	public void error(final Marker marker, final String msg) {
		getDelegate().error(marker, msg);
	}

	@Override
	public void error(final Marker marker, final String format, final Object arg) {
		getDelegate().error(marker, format, arg);
	}

	@Override
	public void error(final Marker marker, final String format, final Object arg1, final Object arg2) {
		getDelegate().error(marker, format, arg1, arg2);
	}

	@Override
	public void error(final Marker marker, final String format, final Object[] argArray) {
		getDelegate().error(marker, format, argArray);
	}

	@Override
	public void error(final Marker marker, final String msg, final Throwable t) {
		getDelegate().error(marker, msg, t);
	}

	@Override
	public void error(final String msg) {
		getDelegate().error(msg);
	}

	@Override
	public void error(final String format, final Object arg) {
		getDelegate().error(format, arg);
	}

	@Override
	public void error(final String format, final Object arg1, final Object arg2) {
		getDelegate().error(format, arg1, arg2);
	}

	@Override
	public void error(final String format, final Object[] argArray) {
		getDelegate().error(format, argArray);
	}

	@Override
	public void error(final String msg, final Throwable t) {
		getDelegate().error(msg, t);
	}

	@Override
	public String getName() {
		return getDelegate().getName();
	}

	@Override
	public void info(final Marker marker, final String msg) {
		getDelegate().info(marker, msg);
	}

	@Override
	public void info(final Marker marker, final String format, final Object arg) {
		getDelegate().info(marker, format, arg);
	}

	@Override
	public void info(final Marker marker, final String format, final Object arg1, final Object arg2) {
		getDelegate().info(marker, format, arg1, arg2);
	}

	@Override
	public void info(final Marker marker, final String format, final Object[] argArray) {
		getDelegate().info(marker, format, argArray);
	}

	@Override
	public void info(final Marker marker, final String msg, final Throwable t) {
		getDelegate().info(marker, msg, t);
	}

	@Override
	public void info(final String msg) {
		getDelegate().info(msg);
	}

	@Override
	public void info(final String format, final Object arg) {
		getDelegate().info(format, arg);
	}

	@Override
	public void info(final String format, final Object arg1, final Object arg2) {
		getDelegate().info(format, arg1, arg2);
	}

	@Override
	public void info(final String format, final Object[] argArray) {
		getDelegate().info(format, argArray);
	}

	@Override
	public void info(final String msg, final Throwable t) {
		getDelegate().info(msg, t);
	}

	@Override
	public boolean isDebugEnabled() {
		return getDelegate().isDebugEnabled();
	}

	@Override
	public boolean isDebugEnabled(final Marker marker) {
		return getDelegate().isDebugEnabled(marker);
	}

	@Override
	public boolean isErrorEnabled() {
		return getDelegate().isErrorEnabled();
	}

	@Override
	public boolean isErrorEnabled(final Marker marker) {
		return getDelegate().isErrorEnabled(marker);
	}

	@Override
	public boolean isInfoEnabled() {
		return getDelegate().isInfoEnabled();
	}

	@Override
	public boolean isInfoEnabled(final Marker marker) {
		return getDelegate().isInfoEnabled(marker);
	}

	@Override
	public boolean isTraceEnabled() {
		return getDelegate().isTraceEnabled();
	}

	@Override
	public boolean isTraceEnabled(final Marker marker) {
		return getDelegate().isTraceEnabled(marker);
	}

	@Override
	public boolean isWarnEnabled() {
		return getDelegate().isWarnEnabled();
	}

	@Override
	public boolean isWarnEnabled(final Marker marker) {
		return getDelegate().isWarnEnabled(marker);
	}

	@Override
	public void trace(final Marker marker, final String msg) {
		getDelegate().trace(marker, msg);
	}

	@Override
	public void trace(final Marker marker, final String format, final Object arg) {
		getDelegate().trace(marker, format, arg);
	}

	@Override
	public void trace(final Marker marker, final String format, final Object arg1, final Object arg2) {
		getDelegate().trace(marker, format, arg1, arg2);
	}

	@Override
	public void trace(final Marker marker, final String format, final Object[] argArray) {
		getDelegate().trace(marker, format, argArray);
	}

	@Override
	public void trace(final Marker marker, final String msg, final Throwable t) {
		getDelegate().trace(marker, msg, t);
	}

	@Override
	public void trace(final String msg) {
		getDelegate().trace(msg);
	}

	@Override
	public void trace(final String format, final Object arg) {
		getDelegate().trace(format, arg);
	}

	@Override
	public void trace(final String format, final Object arg1, final Object arg2) {
		getDelegate().trace(format, arg1, arg2);
	}

	@Override
	public void trace(final String format, final Object[] argArray) {
		getDelegate().trace(format, argArray);
	}

	@Override
	public void trace(final String msg, final Throwable t) {
		getDelegate().trace(msg, t);
	}

	@Override
	public void warn(final Marker marker, final String msg) {
		getDelegate().warn(marker, msg);
	}

	@Override
	public void warn(final Marker marker, final String format, final Object arg) {
		getDelegate().warn(marker, format, arg);
	}

	@Override
	public void warn(final Marker marker, final String format, final Object arg1, final Object arg2) {
		getDelegate().warn(marker, format, arg1, arg2);
	}

	@Override
	public void warn(final Marker marker, final String format, final Object[] argArray) {
		getDelegate().warn(marker, format, argArray);
	}

	@Override
	public void warn(final Marker marker, final String msg, final Throwable t) {
		getDelegate().warn(marker, msg, t);
	}

	@Override
	public void warn(final String msg) {
		getDelegate().warn(msg);
	}

	@Override
	public void warn(final String format, final Object arg) {
		getDelegate().warn(format, arg);
	}

	@Override
	public void warn(final String format, final Object arg1, final Object arg2) {
		getDelegate().warn(format, arg1, arg2);
	}

	@Override
	public void warn(final String format, final Object[] argArray) {
		getDelegate().warn(format, argArray);
	}

	@Override
	public void warn(final String msg, final Throwable t) {
		getDelegate().warn(msg, t);
	}

}
