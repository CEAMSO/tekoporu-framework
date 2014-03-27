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

package org.ticpy.tekoporu.template;

import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;

import org.ticpy.tekoporu.DemoiselleException;
import org.ticpy.tekoporu.annotation.Name;
import org.ticpy.tekoporu.util.Beans;
import org.ticpy.tekoporu.util.Faces;
import org.ticpy.tekoporu.util.Parameter;
import org.ticpy.tekoporu.util.Reflections;
import org.ticpy.tekoporu.util.ResourceBundle;

public abstract class AbstractEditPageBean<T, I> extends AbstractPageBean implements EditPageBean<T> {

	private static final long serialVersionUID = 1L;

	@Inject
	private Parameter<String> id;

	private T bean;

	private Class<T> beanClass;

	private Class<I> idClass;
	
	@Inject
	@Name("demoiselle-jsf-bundle")
	private ResourceBundle bundle;

	@Inject
	private FacesContext facesContext;

	protected void clear() {
		this.id = null;
		this.bean = null;
	}

	protected T createBean() {
		return Beans.getReference(getBeanClass());
	}

	@Override
	public T getBean() {
		if (this.bean == null) {
			initBean();
		}

		return this.bean;
	}

	protected Class<T> getBeanClass() {
		if (this.beanClass == null) {
			this.beanClass = Reflections.getGenericTypeArgument(this.getClass(), 0);
		}

		return this.beanClass;
	}

	protected Class<I> getIdClass() {
		if (this.idClass == null) {
			this.idClass = Reflections.getGenericTypeArgument(this.getClass(), 1);
		}

		return this.idClass;
	}

	@Override
	@SuppressWarnings("unchecked")
	public I getId() {
		Converter converter = getIdConverter();
		
		if(converter == null && String.class.equals(getIdClass())) {
			return (I) id.getValue();

		} else if (converter == null) {
			throw new DemoiselleException(bundle.getString("id-converter-not-found", getIdClass().getCanonicalName()));
		
		} else {
			return (I) converter.getAsObject(facesContext, facesContext.getViewRoot(), id.getValue());
		}
	}

	private Converter getIdConverter() {
		return Faces.getConverter(getIdClass());
	}

	protected abstract void handleLoad();

	private void initBean() {
		if (isUpdateMode()) {
			this.bean = this.loadBean();
		} else {
			setBean(createBean());
		}
	}

	@Override
	public boolean isUpdateMode() {
		return getId() != null;
	}

	private T loadBean() {
		this.handleLoad();
		return this.bean;
	}

	protected void setBean(final T bean) {
		this.bean = bean;
	}

	// protected void setId(final I id) {
	// clear();
	// String value = getIdConverter().getAsString(getFacesContext(), getFacesContext().getViewRoot(), id);
	// this.id.setValue(value);
	// }

}
