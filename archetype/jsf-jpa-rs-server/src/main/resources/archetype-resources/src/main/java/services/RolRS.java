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

import ${package}.business.RolBC;

import app_common.app_common.api.RolAPI;
import app_common.app_common.domain.Rol;
import app_common.app_common.util.PagedList;

/**
 * Servicio REST para las operaciones sobre Roles, implementa la interfaz RolAPI
 * 
 * @author desa2
 * 
 */
public class RolRS implements RolAPI {

	/** Inyecciones de dependencias **/

	@Inject
	private RolBC rolBC;

	@Inject
	private Logger log;

	/** Métodos de la API **/

	@Override
	public Response getRoles() {
		List<Rol> roles = rolBC.findAll();
		for (Rol r : roles) {
			r.setUsuarios(null);
		}
		return Response.ok(roles).build();
	}

	@Override
	public Response getRoles(int page, int limit, String sortField,
			String sortOrder, Rol rol) {
		List<Rol> finalList = new ArrayList<Rol>();
		PagedList<Rol> lista = new PagedList<Rol>();

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
		finalList = rolBC.findAll(sortField, asc, rol, pag);
		lista.setList(finalList);
		if (pag != null) {
			lista.setTotal(pag.getTotalResults());
		} else {
			lista.setTotal(finalList.size());
		}
		return Response.ok(lista).build();
	}

	@Override
	public Response getRol(Long id) {
		Rol rol = rolBC.load(id);

		if (rol == null) {
			return Response.ok(new HashMap<Object, Object>()).build();
		} else {
			return Response.ok(rol).build();
		}
	}

	@Override
	public Response deleteRol(Long id) {
		Map<String, Object> root = new HashMap<String, Object>();

		try {
			rolBC.delete(id);
			root.put("success", true);
		} catch (EntityNotFoundException e) {
			log.error("WARNING - No existe el ID");
			root.put("success", false);
		}

		return Response.ok(root).build();
	}

	@Override
	public Response createRol(Rol rol) {
		Map<String, Object> root = new HashMap<String, Object>();
		if (rol.getId() != null && rolBC.load(rol.getId()) != null) {
			log.error("WARNING - Ya existe el ID");
			root.put("success", false);
		} else {
			Rol b = rolBC.updateRol(rol);
			root.put("success", true);
			root.put("rol", b.toString());
		}

		return Response.ok(root).build();
	}

	@Override
	public Response updateRol(Long id, Rol rol) {
		Map<String, Object> root = new HashMap<String, Object>();
		Rol rolRecuperado = rolBC.load(rol.getId());
		if (rol.getId() != null && rolRecuperado != null) {
			rolRecuperado.setDescripcion(rol.getDescripcion());
			rolRecuperado.setPermisos(rol.getPermisos());
			Rol t = rolBC.updateRol(rolRecuperado);
			root.put("success", true);
			root.put("rol", t.toString());
		} else {
			log.error("WARNING - No existe un Rol con el ID asignado");
			root.put("success", false);
		}
		return Response.ok(root).build();
	}

}
