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

package org.ticpy.tekoporu.internal.implementation;

import static org.ticpy.tekoporu.annotation.Priority.MIN_PRIORITY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.ticpy.tekoporu.annotation.Priority;
import org.ticpy.tekoporu.configuration.ConfigurationException;
import org.ticpy.tekoporu.internal.producer.ResourceBundleProducer;
import org.ticpy.tekoporu.util.ResourceBundle;

public final class StrategySelector implements Serializable {

	public static final int CORE_PRIORITY = MIN_PRIORITY;

	public static final int EXTENSIONS_L1_PRIORITY = CORE_PRIORITY - 100;

	public static final int EXTENSIONS_L2_PRIORITY = EXTENSIONS_L1_PRIORITY - 100;

	public static final int COMPONENTS_PRIORITY = EXTENSIONS_L2_PRIORITY - 100;

	private static final long serialVersionUID = 1L;

	private static ResourceBundle bundle;

	private StrategySelector() {
	}

	private static ResourceBundle getBundle() {
		if (bundle == null) {
			bundle = ResourceBundleProducer.create("demoiselle-core-bundle");
		}

		return bundle;
	}

	public static <T> Class<? extends T> getClass(Class<T> type, List<Class<? extends T>> options)
			throws ConfigurationException {
		Class<? extends T> selected = null;

		for (Class<? extends T> option : options) {
			if (selected == null || getPriority(option) < getPriority(selected)) {
				selected = option;
			}
		}

		checkForAmbiguity(type, selected, options);

		return selected;
	}

	private static <T> void checkForAmbiguity(Class<T> type, Class<? extends T> selected,
			List<Class<? extends T>> options) throws ConfigurationException {
		int selectedPriority = getPriority(selected);

		List<Class<? extends T>> ambiguous = new ArrayList<Class<? extends T>>();

		for (Class<? extends T> option : options) {
			if (selected != option && selectedPriority == getPriority(option)) {
				ambiguous.add(option);
			}
		}

		if (!ambiguous.isEmpty()) {
			ambiguous.add(selected);

			String message = getExceptionMessage(type, ambiguous);
			throw new ConfigurationException(message);
		}
	}

	private static <T> String getExceptionMessage(Class<T> type, List<Class<? extends T>> ambiguous) {
		StringBuffer classes = new StringBuffer();

		int i = 0;
		for (Class<? extends T> clazz : ambiguous) {
			if (i++ != 0) {
				classes.append(", ");
			}

			classes.append(clazz.getCanonicalName());
		}

		return getBundle().getString("ambiguous-strategy-resolution", type.getCanonicalName(), classes.toString());
	}

	private static <T> int getPriority(Class<T> type) {
		int result = Priority.MAX_PRIORITY;
		Priority priority = type.getAnnotation(Priority.class);

		if (priority != null) {
			result = priority.value();
		}

		return result;
	}

	// public static <T> T getExplicitReference(String configKey, Class<T> strategyType, Class<T> defaultType) {
	// Class<T> selectedType = loadSelected(configKey, strategyType, defaultType);
	// return Beans.getReference(selectedType);
	// }
	//
	// @SuppressWarnings("unchecked")
	// private static <T> Class<T> loadSelected(String configKey, Class<T> strategyType, Class<T> defaultType) {
	// ResourceBundle bundle = ResourceBundleProducer.create("demoiselle-core-bundle",
	// Beans.getReference(Locale.class));
	// Class<T> result = null;
	// String canonicalName = null;
	// String typeName = strategyType.getSimpleName().toLowerCase();
	// String key = null;
	// try {
	// URL url = ConfigurationLoader.getResourceAsURL("demoiselle.properties");
	// Configuration config = new PropertiesConfiguration(url);
	// canonicalName = config.getString(configKey, defaultType.getCanonicalName());
	// ClassLoader classLoader = ConfigurationLoader.getClassLoaderForClass(canonicalName);
	// if (classLoader == null) {
	// classLoader = Thread.currentThread().getContextClassLoader();
	// }
	// result = (Class<T>) Class.forName(canonicalName, false, classLoader);
	// result.asSubclass(strategyType);
	// } catch (org.apache.commons.configuration.ConfigurationException cause) {
	// throw new ConfigurationException(bundle.getString("file-not-found", "demoiselle.properties"));
	// } catch (ClassNotFoundException cause) {
	// key = Strings.getString("{0}-class-not-found", typeName);
	// throw new ConfigurationException(bundle.getString(key, canonicalName));
	// } catch (FileNotFoundException e) {
	// throw new ConfigurationException(bundle.getString("file-not-found", "demoiselle.properties"));
	// } catch (ClassCastException cause) {
	// key = Strings.getString("{0}-class-must-be-of-type", typeName);
	// throw new ConfigurationException(bundle.getString(key, canonicalName, strategyType));
	// }
	// return result;
	// }
}
