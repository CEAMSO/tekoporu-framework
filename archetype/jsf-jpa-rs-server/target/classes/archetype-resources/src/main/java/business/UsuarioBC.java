package ${package}.business;

import java.util.List;

import javax.inject.Inject;

import org.ticpy.tekoporu.pagination.Pagination;
import org.ticpy.tekoporu.template.PaginatedDelegateCrud;

import ${package}.persistence.RolDAO;
import ${package}.persistence.UsuarioDAO;

import app_common.app_common.domain.Usuario;

public class UsuarioBC extends PaginatedDelegateCrud<Usuario, Long, UsuarioDAO> {
	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioDAO usuarioDAO;

	/*@Inject
	private RolDAO rolDAO;

	public List<Usuario> listarUsuarios() {
		return usuarioDAO.find();
	}

	public List<Usuario> findPage(int pageSize, int first, String sortField,
			boolean sortOrderAsc) {
		return usuarioDAO.findPage(pageSize, first, sortField, sortOrderAsc);
	}

	public int count() {
		return usuarioDAO.count();
	}*/

	public List<Usuario> findAll(String sortField, boolean sortOrder,
			Usuario filtro, Pagination pag) {
		usuarioDAO.setPagination(pag);
		if (filtro != null) {
			if (sortField != null) {
				return usuarioDAO.findAll(sortField, sortOrder, filtro);
			} else {
				return usuarioDAO.findByExample(filtro);
			}
		} else {
			if (sortField != null) {
				return usuarioDAO.findAll(sortField, sortOrder);
			} else {
				return usuarioDAO.findAll();
			}
		}
	}

	public Usuario updateUsuario(Usuario u) {
		return usuarioDAO.updateUsuario(u);
	}

}
