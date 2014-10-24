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

import ${package}.business.UsuarioBC;

import app_common.app_common.api.UsuarioAPI;
import app_common.app_common.domain.Usuario;
import app_common.app_common.util.PagedList;

/**
 * Servicio REST para las operaciones sobre Usuairos, implementa la interfaz
 * UsuarioAPI
 * 
 * @author desa2
 * 
 */
public class UsuarioRS implements UsuarioAPI {

	/** Inyecciones de dependencias **/

	@Inject
	private UsuarioBC usuarioBC;

	@Inject
	private Logger log;

	/** Métodos de la API **/

	@Override
	public Response getUsuarios(int page, int limit, String sortField,
			String sortOrder, Usuario usuario) {
		List<Usuario> finalList = new ArrayList<Usuario>();
		PagedList<Usuario> lista = new PagedList<Usuario>();

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
		finalList = usuarioBC.findAll(sortField, asc, usuario, pag);
		lista.setList(finalList);
		if (pag != null) {
			lista.setTotal(pag.getTotalResults());
		} else {
			lista.setTotal(finalList.size());
		}
		return Response.ok(lista).build();
	}

	@Override
	public Response getUsuario(Long id) {
		Usuario usuario = usuarioBC.load(id);

		if (usuario == null) {
			return Response.ok(new HashMap<Object, Object>()).build();
		} else {
			return Response.ok(usuario).build();
		}
	}

	@Override
	public Response deleteUsuario(Long id) {
		Map<String, Object> root = new HashMap<String, Object>();

		try {
			usuarioBC.delete(id);
			root.put("success", true);
		} catch (EntityNotFoundException e) {
			log.error("WARNING - No existe el ID");
			root.put("success", false);
		}

		return Response.ok(root).build();
	}

	@Override
	public Response createUsuario(Usuario usuario) {
		Map<String, Object> root = new HashMap<String, Object>();
		if (usuario.getId() != null && usuarioBC.load(usuario.getId()) != null) {
			log.error("WARNING - Ya existe el ID");
			root.put("success", false);
		} else {
			Usuario b = usuarioBC.updateUsuario(usuario);
			root.put("success", true);
			root.put("usuario", b.toString());
		}

		return Response.ok(root).build();
	}

	@Override
	public Response updateUsuario(Long id, Usuario usuario) {
		Map<String, Object> root = new HashMap<String, Object>();
		Usuario usuarioRecuperado = usuarioBC.load(usuario.getId());
		if (usuario.getId() != null && usuarioRecuperado != null) {
			usuarioRecuperado.setNombre(usuario.getNombre());
			usuarioRecuperado.setApellido(usuario.getApellido());
			usuarioRecuperado.setEmail(usuario.getEmail());
			usuarioRecuperado.setUsername(usuario.getUsername());
			usuarioRecuperado.setPwd(usuario.getPwd());
			usuarioRecuperado.setTelefono(usuario.getTelefono());
			usuarioRecuperado.setActivo(usuario.isActivo());
			usuarioRecuperado.setRoles(usuario.getRoles());
			Usuario t = usuarioBC.updateUsuario(usuarioRecuperado);
			root.put("success", true);
			root.put("usuario", t.toString());
		} else {
			log.error("WARNING - No existe un Usuario con el ID asignado");
			root.put("success", false);
		}
		return Response.ok(root).build();
	}

}
