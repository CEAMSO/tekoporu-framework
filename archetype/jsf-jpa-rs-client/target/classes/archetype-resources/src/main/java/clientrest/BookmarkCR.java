package ${package}.clientrest;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

import ${package}.configs.BackEndConfig;

import app_common.app_common.api.BookmarkAPI;
import app_common.app_common.domain.Bookmark;
import app_common.app_common.util.JSONParameterObject;
import app_common.app_common.util.PagedList;

/**
 * Cliente rest para las operaciones sobre bookmarks.
 * 
 * @author desa2
 * 
 */
public class BookmarkCR {

	/** Inyección de dependencias **/
	@Inject
	private BackEndConfig config;

	/**
	 * Obtiene la API Rest de bookmark
	 * 
	 * @return BookmarkAPI
	 */
	public BookmarkAPI getAPI() {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.getCredentialsProvider().setCredentials(
				new AuthScope(config.getHostServerDomain(),
						config.getHostServerPort()),
				new UsernamePasswordCredentials(config.getServerUser(), config
						.getServerPassword()));
		ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpClient);
		ResteasyClient client = new ResteasyClientBuilder().httpEngine(engine)
				.build();
		ResteasyWebTarget target = client.target(config.getHostServer());
		BookmarkAPI api = target.proxy(BookmarkAPI.class);
		return api;
	}

	/**
	 * Obtiene un bookmark dado su id por medio del servicio rest
	 * 
	 * @param id
	 *            Id del bookmark que se desea obtener
	 * @return El bookmark
	 */
	public Bookmark getBookmark(Long id) {
		Bookmark t;
		try {
			BookmarkAPI api = getAPI();
			Response res = api.getBookmark(id);
			t = res.readEntity(Bookmark.class);
			res.close();
		} catch (Exception e) {
			e.printStackTrace();
			t = null;
		}
		return t;
	}

	/**
	 * Llama al método crear un bookmark del servicio rest
	 * 
	 * @param bookmark
	 *            Bookmark que se desea crear
	 * @return El bookmark creado
	 */
	public Bookmark createBookmark(Bookmark bookmark) {
		Bookmark t;
		try {
			Response response = getAPI().createBookmark(bookmark);
			String json = response.readEntity(String.class);
			Map<String, Object> map = JSONParameterObject.valueOf(json)
					.getObject();
			t = Bookmark.valueOf((String) map.get("bookmark"));
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
			t = null;
		}
		return t;
	}

	/**
	 * Llama al método actualizar un bookmark del servicio rest
	 * 
	 * @param bookmark
	 *            Bookmark que se desea actualizar
	 * @return El bookmark actualizado
	 */
	public Bookmark updateBookmark(Bookmark bookmark) {
		Bookmark t;
		try {
			Response response = getAPI().updateBookmark(bookmark.getId(),
					bookmark);
			String json = response.readEntity(String.class);
			Map<String, Object> map = JSONParameterObject.valueOf(json)
					.getObject();
			t = Bookmark.valueOf((String) map.get("bookmark"));
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
			t = null;
		}
		return t;
	}

	/**
	 * Obtiene todos los bookmarks de la BD con paginación por medio del
	 * servicio rest
	 * 
	 * @param pageSize
	 *            Tamaño de página
	 * @param first
	 *            Primer elemento de la lista
	 * @param sortField
	 *            Campo por el que se ordenarán los elementos de la lista
	 * @param sortOrder
	 *            Tipo de orden: ascendente o descendente
	 * @param bookmark
	 *            El bookmark que servirá como filtro
	 * @return
	 */
	public PagedList<Bookmark> getBookmarks(int pageSize, int first,
			String sortField, String sortOrder, Bookmark bookmark) {
		PagedList<Bookmark> root;
		try {
			int page = (int) first / pageSize + 1;
			Response response = getAPI().getBookmarks(page, pageSize,
					sortField, sortOrder, bookmark);
			GenericType<PagedList<Bookmark>> pagedListType = new GenericType<PagedList<Bookmark>>() {
			};

			root = response.readEntity(pagedListType);
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
			root = null;
		}
		return root;

	}

	public void deleteBookmark(Long id) {
		BookmarkAPI bookmark = getAPI();
		Response response = bookmark.deleteBookmark(id);
		response.close();
	}

}
