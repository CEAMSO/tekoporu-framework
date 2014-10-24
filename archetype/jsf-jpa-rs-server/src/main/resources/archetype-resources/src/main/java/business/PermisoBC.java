package ${package}.business;

import java.util.List;

import javax.inject.Inject;

import org.ticpy.tekoporu.pagination.Pagination;
import org.ticpy.tekoporu.template.PaginatedDelegateCrud;

import ${package}.persistence.PermisoDAO;

import app_common.app_common.domain.Permiso;

public class PermisoBC extends PaginatedDelegateCrud<Permiso, Long, PermisoDAO> {
	private static final long serialVersionUID = 1L;

	@Inject
	private PermisoDAO permisoDAO;

	/*public List<Permiso> findPage(int pageSize, int first, String sortField,
			boolean sortOrderAsc) {
		return permisoDAO.findPage(pageSize, first, sortField, sortOrderAsc);
	}

	public int count() {
		return permisoDAO.count();
	}*/

	public List<Permiso> findAll(String sortField, boolean sortOrder,
			Permiso filtro, Pagination pag) {
		permisoDAO.setPagination(pag);
		if (filtro != null) {
			if (sortField != null) {
				return permisoDAO.findAll(sortField, sortOrder, filtro);
			} else {
				return permisoDAO.findByExample(filtro);
			}
		} else {
			if (sortField != null) {
				return permisoDAO.findAll(sortField, sortOrder);
			} else {
				return permisoDAO.findAll();
			}
		}
	}

	public Permiso updatePermiso(Permiso r) {
		return permisoDAO.updatePermiso(r);
	}

}
