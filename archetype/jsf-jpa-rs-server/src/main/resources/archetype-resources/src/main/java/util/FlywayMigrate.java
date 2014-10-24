package ${package}.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

import com.googlecode.flyway.core.Flyway;

/**
 * Ejecuta los scripts sql que se encuentran en src/main/resources/db/migration
 * Por medio de la interfaz Integrator la migración ser realiza antes que la
 * validación de hibernate
 * 
 * @author desa3
 * 
 */
public class FlywayMigrate implements Integrator {

	@Override
	public void integrate(final Configuration configuration,
			final SessionFactoryImplementor sessionFactoryImplementor,
			final SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {

		try {
			final Flyway flyway = new Flyway();
			PropertiesConfiguration config = new PropertiesConfiguration("db.properties");
			String dbHost = config.getString("db.host");
			String dbPort = config.getString("db.port");
			String dbSchema = config.getString("db.schema");
			String dbName = config.getString("db.database.name");
			String dbUser = config.getString("db.user");
			String dbPassword = config.getString("db.password");
			String dbEnvironmnet = config.getString("db.environment");
					
			System.out.println("\n\n>> Estableciendo DataSource en " + dbHost);

			flyway.setSqlMigrationPrefix("");
			flyway.setDataSource("jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName,
					dbUser, dbPassword);
			flyway.setInitVersion("0");
			flyway.setSchemas(dbSchema);
			Map<String, String> placeholder = new HashMap<String, String>();
			
			if ("desarrollo".compareTo(dbEnvironmnet) == 0) {
				placeholder.put("inicio_comentario", "");
				placeholder.put("fin_comentario", "");
			} else {
				placeholder.put("inicio_comentario", "/*");
				placeholder.put("fin_comentario", "*/");
			}
			
			flyway.setPlaceholders(placeholder);
			
			/**
			 * Flyway no ejecuta scripts de versiones inferiores a la última
			 * ejecutada, si se desea ejecutar un script con una versión
			 * inferior a la última se debe setear el atributo OutOfOrder.
			 * OutOfOrder verifica que cada script haya sido ejecutado, los que
			 * no han sido ejecutados se ejecutan en orden
			 */
			// flyway.setOutOfOrder(true);
			/*
			 * Ejecutar init() si da error de empty schema (no se porque a veces
			 * da y otras no)
			 */
			// flyway.init();

			System.out.println("\n\n>> A punto de ejecutar FlyWay migrate...");
			flyway.migrate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void integrate(final MetadataImplementor metadataImplementor,
			final SessionFactoryImplementor sessionFactoryImplementor,
			final SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
	}

	@Override
	public void disintegrate(
			final SessionFactoryImplementor sessionFactoryImplementor,
			final SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
	}

}
