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

import ${package}.domain.Rol;

/**
 * Interfaz de servicios para Roles
 * 
 * @author desa2
 * 
 */
@Path("/rol")
public interface RolAPI {

	/**
	 * Recupera todos los Roles existentes
	 * 
	 * @return
	 */
	@GET
	@Path("/roles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRoles();

	/**
	 * Obtiene una lista de Rols paginada, ordenada y filtrada
	 * 
	 * @param page
	 *            Página
	 * @param limit
	 *            Cantidad por página
	 * @param sortField
	 *            Campo de ordenamiento
	 * @param sortOrder
	 *            Tipo de ordenamiento (ASCENDING, DESCENDING)
	 * @param rol
	 *            Rol con datos de filtrado
	 * @return
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRoles(@QueryParam("page") int page,
			@QueryParam("limit") int limit,
			@QueryParam("sortField") String sortField,
			@QueryParam("sortOrder") String sortOrder,
			@QueryParam("filtros") Rol rol);

	/**
	 * Recupera un rol por su id
	 * 
	 * @param id
	 *            id del rol
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRol(@PathParam("id") Long id);

	/**
	 * Elimina un Trámite
	 * 
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRol(@PathParam("id") Long id);

	/**
	 * Crea un Trámite
	 * 
	 * @param rol
	 *            Rol a ser creado
	 * @return
	 */
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRol(Rol rol);

	/**
	 * Actualiza un rol
	 * 
	 * @param id
	 *            id del rol a ser actualizado
	 * @param rol
	 *            Rol a ser actualizado
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRol(@PathParam("id") Long id, Rol rol);

}
