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

import java.util.List;
import java.util.ListIterator;

import org.ticpy.tekoporu.internal.implementation.DefaultTransaction;
import org.ticpy.tekoporu.transaction.Transaction;
import org.ticpy.tekoporu.transaction.Transactional;
import org.ticpy.tekoporu.util.Beans;
import org.ticpy.tekoporu.util.Reflections;

public class DelegateCrud<T, I, C extends Crud<T, I>> implements Crud<T, I> {

	private static final long serialVersionUID = 1L;

	private Class<C> delegateClass;

	private transient C delegate;

	/**
	 * Removes a instance from delegate.
	 * 
	 * @param id
	 *            Entity with the given identifier
	 */
	@Override
	public void delete(final I id) {
		if (isRunningTransactionalOperations()) {
			transactionalDelete(id);
		} else {
			nonTransactionalDelete(id);
		}
	}

	@Transactional
	private void transactionalDelete(final I id) {
		nonTransactionalDelete(id);
	}

	private void nonTransactionalDelete(final I id) {
		getDelegate().delete(id);
	}

	/**
	 * Removes a list of instances from delegate.
	 * 
	 * @param ids
	 *            List of entities identifiers
	 */
	public void delete(final List<I> ids) {
		if (isRunningTransactionalOperations()) {
			transactionalDelete(ids);
		} else {
			nonTransactionalDelete(ids);
		}
	}

	@Transactional
	private void transactionalDelete(final List<I> ids) {
		nonTransactionalDelete(ids);
	}

	private void nonTransactionalDelete(final List<I> ids) {
		ListIterator<I> iter = ids.listIterator();
		while (iter.hasNext()) {
			this.delete(iter.next());
		}
	}

	/**
	 * Gets the results from delegate.
	 * 
	 * @return The list of matched query results.
	 */
	@Override
	public List<T> findAll() {
		return getDelegate().findAll();
	}

	protected C getDelegate() {
		if (this.delegate == null) {
			this.delegate = Beans.getReference(getDelegateClass());
		}

		return this.delegate;
	}

	protected Class<C> getDelegateClass() {
		if (this.delegateClass == null) {
			this.delegateClass = Reflections.getGenericTypeArgument(this.getClass(), 2);
		}

		return this.delegateClass;
	}

	/**
	 * Delegates the insert operation of the given instance.
	 * 
	 * @param bean
	 *            A entity to be inserted by the delegate
	 */
	@Override
	public void insert(final T bean) {
		if (isRunningTransactionalOperations()) {
			transactionalInsert(bean);
		} else {
			nonTransactionalInsert(bean);
		}
	}

	@Transactional
	private void transactionalInsert(final T bean) {
		nonTransactionalInsert(bean);
	}

	private void nonTransactionalInsert(final T bean) {
		getDelegate().insert(bean);
	}

	/**
	 * Returns the instance of the given entity with the given identifier
	 * 
	 * @return The instance
	 */
	@Override
	public T load(final I id) {
		return getDelegate().load(id);
	}

	/**
	 * Delegates the update operation of the given instance.
	 * 
	 * @param bean
	 *            The instance containing the updated state.
	 */
	@Override
	public void update(final T bean) {
		if (isRunningTransactionalOperations()) {
			transactionalUpdate(bean);
		} else {
			nonTransactionalUpdate(bean);
		}
	}

	@Transactional
	private void transactionalUpdate(final T bean) {
		nonTransactionalUpdate(bean);
	}

	private void nonTransactionalUpdate(final T bean) {
		getDelegate().update(bean);
	}

	private boolean isRunningTransactionalOperations() {
		return !(Beans.getReference(Transaction.class) instanceof DefaultTransaction);
	}
}
