package ${package}.business;

import java.util.List;

import javax.inject.Inject;

import org.ticpy.tekoporu.pagination.Pagination;
import org.ticpy.tekoporu.template.PaginatedDelegateCrud;

import ${package}.persistence.RolDAO;

import app_common.app_common.domain.Rol;

public class RolBC extends PaginatedDelegateCrud<Rol, Long, RolDAO> {
	private static final long serialVersionUID = 1L;

	@Inject
	private RolDAO rolDAO;

	/*public List<Rol> findPage(int pageSize, int first, String sortField,
			boolean sortOrderAsc) {
		return rolDAO.findPage(pageSize, first, sortField, sortOrderAsc);
	}

	public int count() {
		return rolDAO.count();
	}*/

	public List<Rol> findAll(String sortField, boolean sortOrder, Rol filtro,
			Pagination pag) {
		rolDAO.setPagination(pag);
		if (filtro != null) {
			if (sortField != null) {
				return rolDAO.findAll(sortField, sortOrder, filtro);
			} else {
				return rolDAO.findByExample(filtro);
			}
		} else {
			if (sortField != null) {
				return rolDAO.findAll(sortField, sortOrder);
			} else {
				return rolDAO.findAll();
			}
		}
	}

	public Rol updateRol(Rol r) {
		return rolDAO.updateRol(r);
	}

}
