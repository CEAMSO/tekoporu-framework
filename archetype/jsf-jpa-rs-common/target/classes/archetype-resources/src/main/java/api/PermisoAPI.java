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

import ${package}.domain.Permiso;

/**
 * Interfaz de servicios para Permiso
 * 
 * @author desa2
 * 
 */
@Path("/permiso")
public interface PermisoAPI {

	/**
	 * Recupera todos los Permisos existentes
	 * 
	 * @return
	 */
	@GET
	@Path("/permisos")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPermisos();

	/**
	 * Obtiene una lista de Permisos paginada, ordenada y filtrada
	 * 
	 * @param page
	 *            Página
	 * @param limit
	 *            Cantidad por página
	 * @param sortField
	 *            Campo de ordenamiento
	 * @param sortOrder
	 *            Tipo de ordenamiento (ASCENDING, DESCENDING)
	 * @param permiso
	 *            Permiso con datos de filtrado
	 * @return
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPermisos(@QueryParam("page") int page,
			@QueryParam("limit") int limit,
			@QueryParam("sortField") String sortField,
			@QueryParam("sortOrder") String sortOrder,
			@QueryParam("filtros") Permiso permiso);

	/**
	 * Recupera un permiso por su id
	 * 
	 * @param id
	 *            id del permiso
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPermiso(@PathParam("id") Long id);

	/**
	 * Elimina un Trámite
	 * 
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletePermiso(@PathParam("id") Long id);

	/**
	 * Crea un Trámite
	 * 
	 * @param permiso
	 *            Permiso a ser creado
	 * @return
	 */
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createPermiso(Permiso permiso);

	/**
	 * Actualiza un permiso
	 * 
	 * @param id
	 *            id del permiso a ser actualizado
	 * @param permiso
	 *            Permiso a ser actualizado
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePermiso(@PathParam("id") Long id, Permiso permiso);

}
