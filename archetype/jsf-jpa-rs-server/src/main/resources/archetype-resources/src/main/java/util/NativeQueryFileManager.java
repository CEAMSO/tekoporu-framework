package ${package}.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.slf4j.Logger;

import app_common.app_common.util.FileCache;

/**
 * NativeQueryManager ejecuta un query nativo que lee eficientemente de un
 * archivo utilizando la clase CacheFile. El query que se encuentra en el
 * archivo puede ser modificado en cualquier momento sin necesidad de
 * re-deployar la aplicación. Utiliza una conexión sql por medio de la clase
 * QueryExecuter para ejecutar la consulta y obtener un ResultSet.
 * 
 * @author desa3
 * 
 */
public class NativeQueryFileManager {

	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;

	/**
	 * Ejecuta un query nativo que se encuentra en el archivo especificado en el
	 * parámetro path.
	 * 
	 * @param path
	 *            camino real al archivo
	 * @return Objeto de clase ResultSet resultante de la ejecución del query
	 *         nativo
	 * @throws SQLException
	 */
	public List<Map<String, Object>> executeNativeQueryFromFile(String path)
			throws SQLException {

		FileCache cache = new FileCache();
		String content = null;
		try {
			content = cache.getFileFromCache(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Map<String, Object>> res = null;
		if (content != null) {
			Session session = em.unwrap(Session.class);
			QueryExecuter queryExecuter = new QueryExecuter();
			queryExecuter.setQuery(content);
			logger.info("Query ejecutado: " + content);
			try {
				session = session.getSessionFactory().openSession();
				res = session.doReturningWork(queryExecuter);
			} catch (HibernateException e) {
				e.printStackTrace();
			} finally {
				session.close();
			}
			return res;

		} else {
			logger.info("NativeQueryFileManager: Archivo no encontrado [ "
					+ path + " ]");
		}
		return null;

	}

	/**
	 * Ejecuta el query parametrizado
	 * 
	 * @param path
	 *            camino real al archivo
	 * @param parameters
	 *            parámetros del query
	 * @return Objeto de clase ResultSet resultante de la ejecución del query
	 *         nativo
	 * @throws SQLException
	 */
	public List<Map<String, Object>> executeNativeQueryFromFileParameters(
			String path, String... parameters) throws SQLException {
		FileCache cache = new FileCache();
		String content = null;
		try {
			content = cache.getFileFromCache(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Map<String, Object>> res = null;
		if (content != null) {
			for (int i = 0; i < parameters.length; i++) {
				content = content.replace("{" + i + "}", parameters[i]);
			}
			logger.info("Query ejecutado: " + content);
			Session session = em.unwrap(Session.class);
			QueryExecuter queryExecuter = new QueryExecuter();
			queryExecuter.setQuery(content);
			try {
				session = session.getSessionFactory().openSession();
				res = session.doReturningWork(queryExecuter);
			} catch (HibernateException e) {
				e.printStackTrace();
			} finally {

				session.close();
			}

			return res;
		} else {
			logger.info("NativeQueryFileManager: Archivo no encontrado [ "
					+ path + " ]");
		}
		return null;
	}

	/**
	 * Ejecuta el query parametrizado
	 * 
	 * @param query
	 *            consulta a ser ejecutada
	 * @param parameters
	 *            parámetros del query
	 * @return Objeto de clase ResultSet resultante de la ejecución del query
	 *         nativo
	 * @throws SQLException
	 */
	public List<Map<String, Object>> executeNativeQueryWithParameters(
			String query, String... parameters) throws SQLException {

		List<Map<String, Object>> res = null;
		if (query != null) {
			for (int i = 0; i < parameters.length; i++) {
				query = query.replace("{" + i + "}", parameters[i]);
			}
			logger.info("Query ejecutado: " + query);
			Session session = em.unwrap(Session.class);
			QueryExecuter queryExecuter = new QueryExecuter();
			queryExecuter.setQuery(query);
			try {
				session = session.getSessionFactory().openSession();
				res = session.doReturningWork(queryExecuter);
			} catch (HibernateException e) {
				e.printStackTrace();
			} finally {

				session.close();
			}

			return res;
		} else {
			logger.info("query es null");
		}
		return null;
	}

	/**
	 * Clase que ejecuta el query a través de Connection y obtiene un resultado
	 * de tipo ResultSet que lo transforma a una lista de Maps por medio del
	 * método createMapFromResulSet de la clase CreateMap
	 * 
	 * @author desa3
	 * 
	 */
	private static class QueryExecuter implements
			ReturningWork<List<Map<String, Object>>> {
		private List<Map<String, Object>> res;
		private String query = "";

		public List<Map<String, Object>> execute(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet result = ps.executeQuery();
			res = new CreateMap().createMapFromResulSet(result);
			return res;
		}

		public void setQuery(String query) {
			this.query = query;
		}

	}

}
