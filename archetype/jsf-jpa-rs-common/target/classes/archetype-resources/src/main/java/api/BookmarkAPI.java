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

import ${package}.domain.Bookmark;

/**
 * Interfaz de servicios para Bookmark
 * 
 * @author desa2
 * 
 */
@Path("/bookmark")
public interface BookmarkAPI {

	/**
	 * Obtiene una lista de Bookmarks paginada, ordenada y filtrada
	 * 
	 * @param page
	 *            Página
	 * @param limit
	 *            Cantidad por página
	 * @param sortField
	 *            Campo de ordenamiento
	 * @param sortOrder
	 *            Tipo de ordenamiento (ASCENDING, DESCENDING)
	 * @param bookmark
	 *            Bookmark con datos de filtrado
	 * @return
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBookmarks(@QueryParam("page") int page,
			@QueryParam("limit") int limit,
			@QueryParam("sortField") String sortField,
			@QueryParam("sortOrder") String sortOrder,
			@QueryParam("filtros") Bookmark bookmark);

	/**
	 * Recupera un bookmark por su id
	 * 
	 * @param id
	 *            id del bookmark
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBookmark(@PathParam("id") Long id);

	/**
	 * Elimina un Trámite
	 * 
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteBookmark(@PathParam("id") Long id);

	/**
	 * Crea un Trámite
	 * 
	 * @param bookmark
	 *            Bookmark a ser creado
	 * @return
	 */
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createBookmark(Bookmark bookmark);

	/**
	 * Actualiza un bookmark
	 * 
	 * @param id
	 *            id del bookmark a ser actualizado
	 * @param bookmark
	 *            Bookmark a ser actualizado
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateBookmark(@PathParam("id") Long id, Bookmark bookmark);

}
