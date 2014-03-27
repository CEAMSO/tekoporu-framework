package ${package}.business;

import java.util.List;
import javax.inject.Inject;

import ${package}.domain.Rol;
import ${package}.domain.Usuario;
import ${package}.persistence.RolDAO;
import ${package}.persistence.UsuarioDAO;
import ${package}.util.HashUtils;

import org.ticpy.tekoporu.lifecycle.Startup;
import org.ticpy.tekoporu.template.PaginatedDelegateCrud;
import org.ticpy.tekoporu.transaction.Transactional;

public class UsuarioBC extends PaginatedDelegateCrud<Usuario, Long, UsuarioDAO> {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioDAO usuarioDAO;
	
	@Inject
	private RolDAO rolDAO;
	
	
	@Startup
	@Transactional
	public void load() {
		if (findAll().isEmpty()) {
			
			//insert default roles
			if(rolDAO.findAll().isEmpty()) {
				rolDAO.insert(new Rol("admin"));
				rolDAO.insert(new Rol("user"));
			}
			
			List<Rol> list = rolDAO.findAll();
			Rol rol1 = list.get(0);
			Rol rol2 = list.get(1);
			
			insert(new Usuario("S","Administrator","admin@admin.com","Administrator",HashUtils.md5("admin"),rol1,"5441345","admin"));
			insert(new Usuario("S","User","user@user.com","User",HashUtils.md5("user"),rol2,"787454","user"));
			System.out.println(usuarioDAO.findAll().get(0).getUsername()+" "+usuarioDAO.findAll().get(0).getRol().getRolId()+" "+usuarioDAO.findAll().get(0).getRol().getDescripcion());
		}
		

	}
	
	public List<Usuario> listarUsuarios(){
		return usuarioDAO.find(); 
	}
	
	public List<Usuario> findPage(int pageSize, int first, String sortField, boolean sortOrderAsc){
		return usuarioDAO.findPage(pageSize,first, sortField, sortOrderAsc);
	}
	
	public int count() {
		return usuarioDAO.count();
	}
	
	
}
