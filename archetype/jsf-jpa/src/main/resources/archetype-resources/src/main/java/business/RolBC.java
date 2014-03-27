package ${package}.business;

import java.util.List;

import javax.inject.Inject;

import ${package}.domain.Rol;
import ${package}.persistence.RolDAO;

import org.ticpy.tekoporu.template.PaginatedDelegateCrud;

public class RolBC extends PaginatedDelegateCrud<Rol, Long, RolDAO> {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private RolDAO rolDAO;
	
	public List<Rol> findPage(int pageSize, int first, String sortField, boolean sortOrderAsc){
		return rolDAO.findPage(pageSize,first, sortField, sortOrderAsc);
	}
	
	public int count() {
		return rolDAO.count();
	}
	
}
