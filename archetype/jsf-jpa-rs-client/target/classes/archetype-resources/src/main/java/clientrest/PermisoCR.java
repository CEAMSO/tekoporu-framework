package ${package}.clientrest;

import java.util.List;
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

import app_common.app_common.api.PermisoAPI;
import app_common.app_common.api.RolAPI;
import app_common.app_common.domain.Permiso;
import app_common.app_common.domain.Rol;
import app_common.app_common.util.JSONParameterObject;
import app_common.app_common.util.PagedList;

/**
 * Cliente rest para las operaciones sobre permisos.
 * 
 * @author desa2
 * 
 */
public class PermisoCR {

	/** Inyección de dependencias **/
	@Inject
	private BackEndConfig config;

	/**
	 * Obtiene la API Rest de permiso
	 * 
	 * @return PermisoAPI
	 */
	public PermisoAPI getAPI() {
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
		PermisoAPI api = target.proxy(PermisoAPI.class);
		return api;
	}

	/**
	 * Obtiene todos los Permisos de la BD por medio del servicio rest
	 * 
	 * @return Lista de Permisos
	 */
	public List<Permiso> getPermisos() {
		List<Permiso> lista;
		try {
			PermisoAPI api = getAPI();
			Response response = api.getPermisos();
			GenericType<List<Permiso>> listType = new GenericType<List<Permiso>>() {
			};
			lista = response.readEntity(listType);
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
			lista = null;
		}
		return lista;
	}

	/**
	 * Obtiene un permiso dado su id por medio del servicio rest
	 * 
	 * @param id
	 *            Id del permiso que se desea obtener
	 * @return El permiso
	 */
	public Permiso getPermiso(Long id) {
		Permiso t;
		try {
			PermisoAPI api = getAPI();
			Response res = api.getPermiso(id);
			t = res.readEntity(Permiso.class);
			res.close();
		} catch (Exception e) {
			e.printStackTrace();
			t = null;
		}
		return t;
	}

	/**
	 * Llama al método crear un permiso del servicio rest
	 * 
	 * @param permiso
	 *            Permiso que se desea crear
	 * @return El permiso creado
	 */
	public Permiso createPermiso(Permiso permiso) {
		Permiso t;
		try {
			Response response = getAPI().createPermiso(permiso);
			String json = response.readEntity(String.class);
			Map<String, Object> map = JSONParameterObject.valueOf(json)
					.getObject();
			t = Permiso.valueOf((String) map.get("permiso"));
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
			t = null;
		}
		return t;
	}

	/**
	 * Llama al método actualizar un permiso del servicio rest
	 * 
	 * @param permiso
	 *            Permiso que se desea actualizar
	 * @return El permiso actualizado
	 */
	public Permiso updatePermiso(Permiso permiso) {
		Permiso t;
		try {
			Response response = getAPI()
					.updatePermiso(permiso.getId(), permiso);
			String json = response.readEntity(String.class);
			Map<String, Object> map = JSONParameterObject.valueOf(json)
					.getObject();
			t = Permiso.valueOf((String) map.get("permiso"));
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
			t = null;
		}
		return t;
	}

	/**
	 * Obtiene todos los permisos de la BD con paginación por medio del servicio
	 * rest
	 * 
	 * @param pageSize
	 *            Tamaño de página
	 * @param first
	 *            Primer elemento de la lista
	 * @param sortField
	 *            Campo por el que se ordenarán los elementos de la lista
	 * @param sortOrder
	 *            Tipo de orden: ascendente o descendente
	 * @param permiso
	 *            El permiso que servirá como filtro
	 * @return
	 */
	public PagedList<Permiso> getPermisos(int pageSize, int first,
			String sortField, String sortOrder, Permiso permiso) {
		PagedList<Permiso> root;
		try {
			int page = (int) first / pageSize + 1;
			Response response = getAPI().getPermisos(page, pageSize, sortField,
					sortOrder, permiso);
			GenericType<PagedList<Permiso>> pagedListType = new GenericType<PagedList<Permiso>>() {
			};

			root = response.readEntity(pagedListType);
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
			root = null;
		}
		return root;

	}

}
