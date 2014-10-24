package ${package}.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ${package}.persistence.CasDAO;

@Path("/seguridad")
public class CasRS {
	
	@Inject
	private CasDAO casDAO;
	
	@GET
	@Path("/perfiles/{nombreusuario}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPerfiles(@PathParam("nombreusuario") String usuario) {
		List<Map<String, String>> lista = new ArrayList<Map<String, String>>();
		List<Map<String, Object>> permisosBD = casDAO.getPermisos(usuario);
		for (Map<String, Object> map : permisosBD) {
			Map<String, String> root1 = new HashMap<String, String>();
			String codigo = "bookmark_" + map.get("recurso").toString() + "_"
					+ map.get("operacion").toString();
			root1.put("codigo", codigo);
			root1.put("estado", "A");
			root1.put("nombre", "Nombre del permiso");
			root1.put("descripcion", "Descripcion del permiso");
			lista.add(root1);
		}

		return Response.ok(lista).build();
	}

}
