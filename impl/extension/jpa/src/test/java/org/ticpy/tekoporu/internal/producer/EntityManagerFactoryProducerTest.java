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
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verify;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.reflect.Whitebox.setInternalState;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

import org.ticpy.tekoporu.DemoiselleException;
import org.ticpy.tekoporu.util.ResourceBundle;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Persistence.class)
public class EntityManagerFactoryProducerTest {

	private EntityManagerFactory emFactory;
	private EntityManagerFactoryProducer producer;
	private Map<ClassLoader, Map<String, EntityManagerFactory>> cache;
	private Logger logger;
	private ResourceBundle bundle;
	
	@Before
	public void setUp() {
		logger = createMock(Logger.class);
		bundle = ResourceBundleProducer.create("demoiselle-jpa-bundle", Locale.getDefault());
		producer = new EntityManagerFactoryProducer();
		cache = Collections.synchronizedMap(new HashMap<ClassLoader, Map<String, EntityManagerFactory>>());
		setInternalState(producer, "factoryCache", cache);
		setInternalState(producer, Logger.class, logger);
		setInternalState(producer, ResourceBundle.class, bundle);
		emFactory = createMock(EntityManagerFactory.class);
	}
	
	@Test
	public void testCreateWithUnitPersistenceExisting() {
		ClassLoader cl = this.getClass().getClassLoader();
		HashMap<String, EntityManagerFactory> emEntry = new HashMap<String, EntityManagerFactory>();
		emEntry.put("pu1", emFactory);
		cache.put(cl,emEntry);

		Assert.assertEquals(emFactory, producer.create("pu1"));
	}
	
	
	@Test
	public void testCreateWithUnitPersistenceNotExisting() {
		
		mockStatic(Persistence.class);
		expect(Persistence.createEntityManagerFactory("pu1")).andReturn(emFactory);
		
		replay(Persistence.class);
		
		Assert.assertEquals(emFactory, producer.create("pu1"));
	}
	
	/**
	 * Test if after producing an entity manager, the EntityManagerFactory is correctly stored in the
	 * cache associated with the current class loader.
	 */
	/*
	@Test
	public void testStorageCacheAfterCreate(){
		mockStatic(Persistence.class);
		expect(Persistence.createEntityManagerFactory("pu1")).andReturn(emFactory);
		replay(Persistence.class);

		Map<String,EntityManagerFactory> producerCache = producer.getCache();
		Assert.assertNotNull(producerCache);
		Assert.assertTrue(producerCache.containsKey("pu1"));
		Assert.assertTrue(producerCache.containsValue(emFactory));
	}
	*/
	/*
	@Test
	public void testInitWithoutError() {
		mockStatic(Persistence.class);
		expect(Persistence.createEntityManagerFactory("pu1")).andReturn(emFactory);
		replay(Persistence.class);
		
		producer.loadPersistenceUnits();
		
		ClassLoader cl = this.getClass().getClassLoader();
		Map<String, EntityManagerFactory> internalCache = cache.get(cl);
		
		Assert.assertNotNull(internalCache);
		Assert.assertEquals(emFactory, internalCache.get("pu1"));
	}
	*/
	/*@Test
	public void testInitWithError() {
		try {
			
			producer.loadPersistenceUnits();
			Assert.fail();
		}catch(DemoiselleException cause) {
			Assert.assertTrue(true);
		}
	}*/
	
	@Test
	public void testGetCache() {
		ClassLoader cl = this.getClass().getClassLoader();
		HashMap<String, EntityManagerFactory> emEntry = new HashMap<String, EntityManagerFactory>();
		emEntry.put("pu1", emFactory);
		cache.put(cl, emEntry);
		
		mockStatic(Persistence.class);
		expect(Persistence.createEntityManagerFactory("pu1")).andReturn(emFactory);
		replay(Persistence.class);
		
		Assert.assertEquals(cache.get(this.getClass().getClassLoader()), producer.getCache());
	}
	
	@Test
	public void testClose() {
		ClassLoader cl = this.getClass().getClassLoader();
		HashMap<String, EntityManagerFactory> emEntry = new HashMap<String, EntityManagerFactory>();
		emEntry.put("pu1", emFactory);
		cache.put(cl, emEntry);

		emFactory.close();
		replay(emFactory);
		producer.close();
		verify(emFactory);
	}
	
	/**
	 * Test if detecting the persistence unit with an empty name throws correct error.
	 */
	@Test
	public void testEmptyPersistenceUnitName(){
		EntityManagerFactoryProducer.ENTITY_MANAGER_RESOURCE = "persistence-empty-name.xml";
		
		try{
			producer.getCache();
			Assert.fail();
		}
		catch(DemoiselleException de){
			//
		}
	}
	
	/**
	 * Test if asking to create an entity manager still not in the cache will correctly create it and put it in the cache.
	 */
	@Test
	public void testCreatePersistenceUnitNotInCache() {
		mockStatic(Persistence.class);
		expect(Persistence.createEntityManagerFactory("pu1")).andReturn(emFactory);
		replay(Persistence.class);
		
		ClassLoader cl = this.getClass().getClassLoader();
		HashMap<String, EntityManagerFactory> emEntry = new HashMap<String, EntityManagerFactory>();
		cache.put(cl,emEntry);
		
		producer.create("pu1");
		
		Map<String,EntityManagerFactory> producerCache = producer.getCache();
		Assert.assertNotNull(producerCache);
		Assert.assertTrue(producerCache.containsKey("pu1"));
		Assert.assertTrue(producerCache.containsValue(emFactory));
	}
}
