package ${package}.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.ticpy.tekoporu.internal.implementation.PaginationImpl;
import org.ticpy.tekoporu.pagination.Pagination;

import ${package}.business.PermisoBC;

import app_common.app_common.api.PermisoAPI;
import app_common.app_common.domain.Permiso;
import app_common.app_common.domain.Rol;
import app_common.app_common.util.PagedList;

/**
 * Servicio REST para las operaciones sobre Permisos, implementa la interfaz
 * PermisoAPI
 * 
 * @author desa2
 * 
 */
public class PermisoRS implements PermisoAPI {

	/** Inyecciones de dependencias **/

	@Inject
	private PermisoBC permisoBC;

	@Inject
	private Logger log;

	/** Métodos de la API **/

	@Override
	public Response getPermisos() {
		List<Permiso> permisos = permisoBC.findAll();
		for (Permiso p : permisos) {
			p.setRoles(null);
		}
		return Response.ok(permisos).build();
	}

	@Override
	public Response getPermisos(int page, int limit, String sortField,
			String sortOrder, Permiso permiso) {
		List<Permiso> finalList = new ArrayList<Permiso>();
		PagedList<Permiso> lista = new PagedList<Permiso>();

		Pagination pag = null;

		// paginar o no
		if (page != 0) {
			pag = new PaginationImpl();
			pag.setPageSize(limit);
			pag.setCurrentPage(page - 1);
		}

		// setear order by
		boolean asc = true;
		if (sortField != null) {
			if (sortOrder.compareTo("ASCENDING") != 0) {
				asc = false;
			}
		}
		finalList = permisoBC.findAll(sortField, asc, permiso, pag);
		lista.setList(finalList);
		if (pag != null) {
			lista.setTotal(pag.getTotalResults());
		} else {
			lista.setTotal(finalList.size());
		}
		return Response.ok(lista).build();
	}

	@Override
	public Response getPermiso(Long id) {
		Permiso permiso = permisoBC.load(id);

		if (permiso == null) {
			return Response.ok(new HashMap<Object, Object>()).build();
		} else {
			return Response.ok(permiso).build();
		}
	}

	@Override
	public Response deletePermiso(Long id) {
		Map<String, Object> root = new HashMap<String, Object>();

		try {
			permisoBC.delete(id);
			root.put("success", true);
		} catch (EntityNotFoundException e) {
			log.error("WARNING - No existe el ID");
			root.put("success", false);
		}

		return Response.ok(root).build();
	}

	@Override
	public Response createPermiso(Permiso permiso) {
		Map<String, Object> root = new HashMap<String, Object>();
		if (permiso.getId() != null && permisoBC.load(permiso.getId()) != null) {
			log.error("WARNING - Ya existe el ID");
			root.put("success", false);
		} else {
			Permiso b = permisoBC.updatePermiso(permiso);
			root.put("success", true);
			root.put("Permiso", b.toString());
		}

		return Response.ok(root).build();
	}

	@Override
	public Response updatePermiso(Long id, Permiso permiso) {
		Map<String, Object> root = new HashMap<String, Object>();
		Permiso permisoRecuperado = permisoBC.load(permiso.getId());
		if (permiso.getId() != null && permisoRecuperado != null) {
			permisoRecuperado.setClave(permiso.getClave());
			permisoRecuperado.setInstancia(permiso.getInstancia());
			permisoRecuperado.setOperacion(permiso.getOperacion());
			permisoRecuperado.setRecurso(permiso.getRecurso());
			Permiso t = permisoBC.updatePermiso(permisoRecuperado);
			root.put("success", true);
			root.put("Permiso", t.toString());
		} else {
			log.error("WARNING - No existe un Permiso con el ID asignado");
			root.put("success", false);
		}
		return Response.ok(root).build();
	}

}
