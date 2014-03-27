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

package org.ticpy.tekoporu.internal.producer;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;

import org.slf4j.Logger;

import org.ticpy.tekoporu.DemoiselleException;
import org.ticpy.tekoporu.annotation.Name;
import org.ticpy.tekoporu.configuration.Configuration;
import org.ticpy.tekoporu.internal.configuration.EntityManagerConfig;
import org.ticpy.tekoporu.internal.proxy.EntityManagerProxy;
import org.ticpy.tekoporu.util.ResourceBundle;

/**
 * <p>
 * Factory class responsible to produces instances of EntityManager. Produces instances based on informations defined in
 * persistence.xml, demoiselle.properties or @PersistenceUnit annotation.
 * </p>
 * TODO allow users to define EntityManager's scope using demoiselle.properties
 */
@RequestScoped
public class EntityManagerProducer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger logger;

	@Inject
	@Name("demoiselle-jpa-bundle")
	private ResourceBundle bundle;

	private final Map<String, EntityManager> cache = Collections.synchronizedMap(new HashMap<String, EntityManager>());

	@Inject
	private EntityManagerFactoryProducer factory;

	/**
	 * <p>
	 * Default EntityManager factory. Tries two strategies to produces EntityManager instances.
	 * <li>The first one is based on informations available on demoiselle properties file
	 * ("frameworkdemoiselle.persistence.unit.name" key).</li>
	 * <li>The second one is based on persistence.xml file. If exists only one Persistence Unit defined, this one is
	 * used.</li>
	 * 
	 * @param config
	 *            Suplies informations about EntityManager defined in properties file.
	 * @return Produced EntityManager.
	 */
	@Default
	@Produces
	public EntityManager create(InjectionPoint ip, EntityManagerConfig config) {
		String persistenceUnit = getPersistenceUnit(ip, config);
		return new EntityManagerProxy(persistenceUnit);
	}

	public EntityManager getEntityManager(String persistenceUnit) {
		EntityManager entityManager = null;

		if (cache.containsKey(persistenceUnit)) {
			entityManager = cache.get(persistenceUnit);

		} else {
			entityManager = factory.create(persistenceUnit).createEntityManager();
			entityManager.setFlushMode(FlushModeType.AUTO);
			// entityManager.setFlushMode(FlushModeType.COMMIT);

			cache.put(persistenceUnit, entityManager);
			this.logger.info(bundle.getString("entity-manager-was-created", persistenceUnit));
		}

		return entityManager;
	}

	private String getPersistenceUnit(InjectionPoint ip, EntityManagerConfig config) {
		String persistenceUnitName;

		if (ip != null && ip.getAnnotated()!=null && ip.getAnnotated().isAnnotationPresent(Name.class)) {
			//Quando o comando Beans.getReference é usado para simular injeção, não existe
			//anotação @Inject então precisamos testar se #getAnnotated() retorna nulo aqui.
			persistenceUnitName = ip.getAnnotated().getAnnotation(Name.class).value();

		} else {
			persistenceUnitName = getFromProperties(config);

			if (persistenceUnitName == null) {
				persistenceUnitName = getFromXML();
			}
		}

		return persistenceUnitName;
	}

	/**
	 * Tries to get persistence unit name from demoiselle.properties.
	 * 
	 * @param config
	 *            Configuration containing persistence unit name.
	 * @return Persistence unit name.
	 */
	private String getFromProperties(EntityManagerConfig config) {
		String persistenceUnit = config.getPersistenceUnitName();

		if (persistenceUnit != null) {
			this.logger.debug(bundle.getString("getting-persistence-unit-from-properties",
					Configuration.DEFAULT_RESOURCE));
		}

		return persistenceUnit;
	}

	/**
	 * Uses persistence.xml to get informations about which persistence unit to use. Throws DemoiselleException if
	 * more than one Persistence Unit is defined.
	 * 
	 * @return Persistence Unit Name
	 */
	private String getFromXML() {
		Set<String> persistenceUnits = factory.getCache().keySet();

		if (persistenceUnits.size() > 1) {
			throw new DemoiselleException(bundle.getString("more-than-one-persistence-unit-defined",
					Name.class.getSimpleName()));
		} else {
			return persistenceUnits.iterator().next();
		}
	}

	@PostConstruct
	public void init() {
		for (String persistenceUnit : factory.getCache().keySet()) {
			getEntityManager(persistenceUnit);
		}
	}

	@PreDestroy
	public void close() {
		for (EntityManager entityManager : cache.values()) {
			entityManager.close();
		}

		cache.clear();
	}

	public Map<String, EntityManager> getCache() {
		return cache;
	}
}
