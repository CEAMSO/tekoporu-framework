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

import app_common.app_common.api.RolAPI;
import app_common.app_common.domain.Rol;
import app_common.app_common.util.JSONParameterObject;
import app_common.app_common.util.PagedList;

/**
 * Cliente rest para las operaciones sobre rols.
 * 
 * @author desa2
 * 
 */
public class RolCR {

	/** Inyección de dependencias **/
	@Inject
	private BackEndConfig config;

	/**
	 * Obtiene la API Rest de rol
	 * 
	 * @return RolAPI
	 */
	public RolAPI getAPI() {
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
		RolAPI api = target.proxy(RolAPI.class);
		return api;
	}

	/**
	 * Obtiene todos los Roles de la BD por medio del servicio rest
	 * 
	 * @return Lista de Roles
	 */
	public List<Rol> getRoles() {
		List<Rol> lista;
		try {
			RolAPI api = getAPI();
			Response response = api.getRoles();
			GenericType<List<Rol>> listType = new GenericType<List<Rol>>() {
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
	 * Obtiene un rol dado su id por medio del servicio rest
	 * 
	 * @param id
	 *            Id del rol que se desea obtener
	 * @return El rol
	 */
	public Rol getRol(Long id) {
		Rol t;
		try {
			RolAPI api = getAPI();
			Response res = api.getRol(id);
			t = res.readEntity(Rol.class);
			res.close();
		} catch (Exception e) {
			e.printStackTrace();
			t = null;
		}
		return t;
	}

	/**
	 * Llama al método crear un rol del servicio rest
	 * 
	 * @param rol
	 *            Trámite que se desea crear
	 * @return El rol creado
	 */
	public Rol createRol(Rol rol) {
		Rol t;
		try {
			Response response = getAPI().createRol(rol);
			String json = response.readEntity(String.class);
			Map<String, Object> map = JSONParameterObject.valueOf(json)
					.getObject();
			t = Rol.valueOf((String) map.get("rol"));
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
			t = null;
		}
		return t;
	}

	/**
	 * Llama al método actualizar un rol del servicio rest
	 * 
	 * @param rol
	 *            Trámite que se desea actualizar
	 * @return El rol actualizado
	 */
	public Rol updateRol(Rol rol) {
		Rol t;
		try {
			Response response = getAPI().updateRol(rol.getId(), rol);
			String json = response.readEntity(String.class);
			Map<String, Object> map = JSONParameterObject.valueOf(json)
					.getObject();
			t = Rol.valueOf((String) map.get("rol"));
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
			t = null;
		}
		return t;
	}

	/**
	 * Obtiene todos los roles de la BD con paginación por medio del servicio
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
	 * @param rol
	 *            El rol que servirá como filtro
	 * @return
	 */
	public PagedList<Rol> getRoles(int pageSize, int first, String sortField,
			String sortOrder, Rol rol) {
		PagedList<Rol> root;
		try {
			int page = (int) first / pageSize + 1;
			Response response = getAPI().getRoles(page, pageSize, sortField,
					sortOrder, rol);
			GenericType<PagedList<Rol>> pagedListType = new GenericType<PagedList<Rol>>() {
			};

			root = response.readEntity(pagedListType);
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
			root = null;
		}
		return root;

	}

	public void deleteRol(Long id) {
		RolAPI rol = getAPI();
		Response response = rol.deleteRol(id);
		response.close();
	}

}
