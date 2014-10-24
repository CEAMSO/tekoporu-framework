package ${package}.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ${package}.domain.Usuario;

/**
 * Interfaz de servicios para Usuario
 * 
 * @author desa2
 * 
 */
@Path("/usuario")
public interface UsuarioAPI {

	/**
	 * Obtiene una lista de Usuarios paginada, ordenada y filtrada
	 * 
	 * @param page
	 *            Página
	 * @param limit
	 *            Cantidad por página
	 * @param sortField
	 *            Campo de ordenamiento
	 * @param sortOrder
	 *            Tipo de ordenamiento (ASCENDING, DESCENDING)
	 * @param usuario
	 *            Usuario con datos de filtrado
	 * @return
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsuarios(@QueryParam("page") int page,
			@QueryParam("limit") int limit,
			@QueryParam("sortField") String sortField,
			@QueryParam("sortOrder") String sortOrder,
			@QueryParam("filtros") Usuario usuario);

	/**
	 * Recupera un usuario por su id
	 * 
	 * @param id
	 *            id del usuario
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsuario(@PathParam("id") Long id);

	/**
	 * Elimina un Trámite
	 * 
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUsuario(@PathParam("id") Long id);

	/**
	 * Crea un Trámite
	 * 
	 * @param usuario
	 *            Usuario a ser creado
	 * @return
	 */
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUsuario(Usuario usuario);

	/**
	 * Actualiza un usuario
	 * 
	 * @param id
	 *            id del usuario a ser actualizado
	 * @param usuario
	 *            Usuario a ser actualizado
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUsuario(@PathParam("id") Long id, Usuario usuario);

}
