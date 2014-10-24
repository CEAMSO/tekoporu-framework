package ${package}.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.ticpy.tekoporu.annotation.NextView;
import org.ticpy.tekoporu.stereotype.ViewController;
import org.ticpy.tekoporu.template.AbstractListPageBean;

import ${package}.clientrest.UsuarioCR;

import app_common.app_common.constant.Statics;
import app_common.app_common.domain.Bookmark;
import app_common.app_common.domain.Usuario;
import app_common.app_common.util.PagedList;

/**
 * Controlador del listado de usuarios de la vista usuario_list.xthml
 * 
 * @author desa2
 * 
 */
@ViewController
@NextView("/secure/usuario_read_only.xhtml?faces-redirect=true&amp;includeViewParams=true")
public class UsuarioListMB extends AbstractListPageBean<Usuario, Long> {

	private static final long serialVersionUID = 1L;

	/** Inyecciones de dependencias **/

	@Inject
	private UsuarioCR usuarioCR;

	@Inject
	private Logger log;

	@Inject
	private SessionMB sessionMB;

	/** Atributos de la clase **/

	private LazyDataModel<Usuario> usuarios;

	private Long tid = 5l;

	private String descripcion;

	private boolean cargarLista = false;

	private boolean mostrarResultados = false;

	private String motivo;

	private Long idUsuarioAEliminar;

	private Long idUsuarioAEditar;

	private boolean disableEliminar = true;

	/** RECURSOS **/

	private String reUsuario = Statics.RECURSO_USUARIO;

	/** OPERACIONES **/

	private String crear = Statics.OPERACION_CREAR;

	private String listar = Statics.OPERACION_LISTAR;

	private String eliminar = Statics.OPERACION_ELIMINAR;

	private String modificar = Statics.OPERACION_MODIFICAR;

	/** Setters y getters **/

	public Long getTid() {
		return tid;
	}

	public void setTid(Long tid) {
		this.tid = tid;
	}

	public boolean isCargarLista() {
		return cargarLista;
	}

	public void setCargarLista(boolean cargarLista) {
		this.cargarLista = cargarLista;
	}

	public LazyDataModel<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(LazyDataModel<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String des) {
		this.descripcion = des;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public boolean isDisableEliminar() {
		return disableEliminar;
	}

	public void setDisableEliminar(boolean disableEliminar) {
		this.disableEliminar = disableEliminar;
	}

	@Override
	protected List<Usuario> handleResultList() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isMostrarResultados() {
		return mostrarResultados;
	}

	public void setMostrarResultados(boolean mostrarResultados) {
		this.mostrarResultados = mostrarResultados;
	}

	public String getMensajeListaVacia() {
		if (this.mostrarResultados == true) {
			return "No se han encontrado registros";
		} else {
			return "";
		}
	}

	public Long getIdUsuarioAEliminar() {
		return idUsuarioAEliminar;
	}

	public void setIdUsuarioAEliminar(Long idUsuarioAEliminar) {
		this.idUsuarioAEliminar = idUsuarioAEliminar;
	}

	public Long getIdUsuarioAEditar() {
		return idUsuarioAEditar;
	}

	public void setIdUsuarioAEditar(Long idUsuarioAEditar) {
		this.idUsuarioAEditar = idUsuarioAEditar;
	}

	/**
	 * Retorna el path al formulario de creación de usuarios
	 * 
	 * @return
	 */
	public String agregarUsuario() {
		return "/secure/usuario_edit.html?faces-redirect=true&llamadoid=264821";
	}

	public void eliminarUsuarios() {
		for (Long uid : getSelectedList()) {
			usuarioCR.deleteUsuario(uid);
		}
		setSelection(new HashMap<Long, Boolean>());
	}

	public void eliminarUsuario() {
		usuarioCR.deleteUsuario(this.idUsuarioAEliminar);
	}

	public void handleChange() {
		if (getSelectedList().size() >= 1) {
			this.disableEliminar = false;
		} else {
			this.disableEliminar = true;
		}
	}

	/**
	 * Define el método load de la lista de usuarios
	 */
	@SuppressWarnings("serial")
	@PostConstruct
	public void loadLazyModel() {

		usuarios = new LazyDataModel<Usuario>() {

			@Override
			public List<Usuario> load(int first, int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, String> filters) {
				if (sortField == null) {
					sortField = "id"; // default sort field
				}
				//Usuario usuario = new Usuario();
				//usuario.setActivo(true);

				PagedList<Usuario> usuarios;
				usuarios = usuarioCR.getUsuarios(pageSize, first, sortField,
						sortOrder.toString(), null);
				this.setRowCount((int) usuarios.getTotal());
				mostrarResultados = true;
				return usuarios.getList();

			}
		};
		usuarios.setPageSize(Statics.PAGE_SIZE);
	}

	public void chekPermissions(ComponentSystemEvent event) {
		if (!sessionMB.isPermitted(reUsuario + listar)) {
			sessionMB.redirect(Statics.STATUS_403);
		}
	}

	/**
	 * Verifica si el usuario posee el permiso de leer el Usuario t
	 * 
	 * @param t
	 * @return
	 */
	public boolean isPermittedLeerUsuario(Usuario t) {
		String username = sessionMB.getLoguedUser();
		boolean isPermitted = false;

		if (sessionMB.isPermitted(reUsuario + listar)) {
			isPermitted = true;
		}

		return isPermitted;
	}

	/**
	 * Verifica si el usuario posee el permiso de agregar procesos.
	 * 
	 * @return
	 */
	public boolean isPermittedCrearUsuario() {
		boolean isPermitted = false;

		if (sessionMB.isPermitted(reUsuario + crear)) {
			isPermitted = true;
		}
		return isPermitted;
	}

	public String verDetallesUsuario(Usuario u) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", u.getId());
		return Statics.URL_USUARIO_READ + Statics.html
				+ sessionMB.createQueryString(params);
	}

	public String editarUsuario(Usuario u) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", u.getId());
		return Statics.URL_USUARIO_EDIT + Statics.html
				+ sessionMB.createQueryString(params);
	}

}
