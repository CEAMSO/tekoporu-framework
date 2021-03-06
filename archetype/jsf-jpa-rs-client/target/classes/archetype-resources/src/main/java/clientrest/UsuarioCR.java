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

import app_common.app_common.api.BookmarkAPI;
import app_common.app_common.api.UsuarioAPI;
import app_common.app_common.domain.Usuario;
import app_common.app_common.util.JSONParameterObject;
import app_common.app_common.util.PagedList;

/**
 * Cliente rest para las operaciones sobre usuarios.
 * 
 * @author desa2
 * 
 */
public class UsuarioCR {

	/** Inyección de dependencias **/
	@Inject
	private BackEndConfig config;

	/**
	 * Obtiene la API Rest de usuario
	 * 
	 * @return UsuarioAPI
	 */
	public UsuarioAPI getAPI() {
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
		UsuarioAPI api = target.proxy(UsuarioAPI.class);
		return api;
	}

	/**
	 * Obtiene un usuario dado su id por medio del servicio rest
	 * 
	 * @param id
	 *            Id del usuario que se desea obtener
	 * @return El usuario
	 */
	public Usuario getUsuario(Long id) {
		Usuario t;
		try {
			UsuarioAPI api = getAPI();
			Response res = api.getUsuario(id);
			t = res.readEntity(Usuario.class);
			res.close();
		} catch (Exception e) {
			e.printStackTrace();
			t = null;
		}
		return t;
	}

	/**
	 * Llama al método crear un usuario del servicio rest
	 * 
	 * @param usuario
	 *            Trámite que se desea crear
	 * @return El usuario creado
	 */
	public Usuario createUsuario(Usuario usuario) {
		Usuario t;
		try {
			Response response = getAPI().createUsuario(usuario);
			String json = response.readEntity(String.class);
			Map<String, Object> map = JSONParameterObject.valueOf(json)
					.getObject();
			t = Usuario.valueOf((String) map.get("usuario"));
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
			t = null;
		}
		return t;
	}

	/**
	 * Llama al método actualizar un usuario del servicio rest
	 * 
	 * @param usuario
	 *            Trámite que se desea actualizar
	 * @return El usuario actualizado
	 */
	public Usuario updateUsuario(Usuario usuario) {
		Usuario t;
		try {
			Response response = getAPI()
					.updateUsuario(usuario.getId(), usuario);
			String json = response.readEntity(String.class);
			Map<String, Object> map = JSONParameterObject.valueOf(json)
					.getObject();
			t = Usuario.valueOf((String) map.get("usuario"));
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
			t = null;
		}
		return t;
	}

	/**
	 * Obtiene todos los usuarios de la BD con paginación por medio del servicio
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
	 * @param usuario
	 *            El usuario que servirá como filtro
	 * @return
	 */
	public PagedList<Usuario> getUsuarios(int pageSize, int first,
			String sortField, String sortOrder, Usuario usuario) {
		PagedList<Usuario> root;
		try {
			int page = (int) first / pageSize + 1;
			Response response = getAPI().getUsuarios(page, pageSize, sortField,
					sortOrder, usuario);
			GenericType<PagedList<Usuario>> pagedListType = new GenericType<PagedList<Usuario>>() {
			};

			root = response.readEntity(pagedListType);
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
			root = null;
		}
		return root;

	}

	public void deleteUsuario(Long id) {
		UsuarioAPI usuario = getAPI();
		Response response = usuario.deleteUsuario(id);
		response.close();
	}
}
