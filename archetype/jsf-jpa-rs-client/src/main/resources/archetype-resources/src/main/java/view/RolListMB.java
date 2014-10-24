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

import ${package}.clientrest.RolCR;

import app_common.app_common.constant.Statics;
import app_common.app_common.domain.Bookmark;
import app_common.app_common.domain.Permiso;
import app_common.app_common.domain.Rol;
import app_common.app_common.util.PagedList;

/**
 * Controlador del listado de roles de la vista rol_list.xthml
 * 
 * @author desa2
 * 
 */
@ViewController
@NextView("/secure/rol_read_only.xhtml?faces-redirect=true&amp;includeViewParams=true")
public class RolListMB extends AbstractListPageBean<Rol, Long> {

	private static final long serialVersionUID = 1L;

	/** Inyecciones de dependencias **/

	@Inject
	private RolCR rolCR;

	@Inject
	private Logger log;

	@Inject
	private SessionMB sessionMB;

	/** Atributos de la clase **/

	private LazyDataModel<Rol> roles;

	private Long tid = 5l;

	private String descripcion;

	private boolean cargarLista = false;

	private boolean mostrarResultados = false;

	private String motivo;

	private Long idRolAEliminar;

	private Long idRolAEditar;

	private boolean disableEliminar = true;

	/** RECURSOS **/

	private String reRol = Statics.RECURSO_ROL;

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

	public LazyDataModel<Rol> getRoles() {
		return roles;
	}

	public void setRoles(LazyDataModel<Rol> roles) {
		this.roles = roles;
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
	protected List<Rol> handleResultList() {
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

	public Long getIdRolAEliminar() {
		return idRolAEliminar;
	}

	public void setIdRolAEliminar(Long idRolAEliminar) {
		this.idRolAEliminar = idRolAEliminar;
	}

	public Long getIdRolAEditar() {
		return idRolAEditar;
	}

	public void setIdRolAEditar(Long idRolAEditar) {
		this.idRolAEditar = idRolAEditar;
	}

	/**
	 * Retorna el path al formulario de creación de rols
	 * 
	 * @return
	 */
	public String agregarRol() {
		return "/secure/rol_edit.html?faces-redirect=true&llamadoid=264821";
	}

	public void eliminarRoles() {
		for (Long rid : getSelectedList()) {
			rolCR.deleteRol(rid);
		}
		setSelection(new HashMap<Long, Boolean>());
	}

	public void eliminarRol() {
		rolCR.deleteRol(this.idRolAEliminar);
	}

	public void handleChange() {
		if (getSelectedList().size() >= 1) {
			this.disableEliminar = false;
		} else {
			this.disableEliminar = true;
		}
	}

	/**
	 * Define el método load de la lista de roles
	 */
	@SuppressWarnings("serial")
	@PostConstruct
	public void loadLazyModel() {

		roles = new LazyDataModel<Rol>() {

			@Override
			public List<Rol> load(int first, int pageSize, String sortField,
					SortOrder sortOrder, Map<String, String> filters) {
				if (sortField == null) {
					sortField = "id"; // default sort field
				}
				//Rol rol = new Rol();

				PagedList<Rol> roles;
				roles = rolCR.getRoles(pageSize, first, sortField,
						sortOrder.toString(), null);
				this.setRowCount((int) roles.getTotal());
				mostrarResultados = true;
				return roles.getList();

			}
		};
		roles.setPageSize(Statics.PAGE_SIZE);
	}

	public void chekPermissions(ComponentSystemEvent event) {
		if (!sessionMB.isPermitted(reRol + listar/* + parte */)) {
			sessionMB.redirect(Statics.STATUS_403);
		} /*
		 * else if ((Long) sessionMB.getAttributeFromSession("llamadoid") ==
		 * null){ sessionMB.redirect("/public/sesionExpirada.html"); }
		 */
	}

	/**
	 * Verifica si el usuario posee el permiso de leer el Rol t
	 * 
	 * @param t
	 * @return
	 */
	public boolean isPermittedLeerRol(Rol t) {
		String username = sessionMB.getLoguedUser();
		boolean isPermitted = false;

		if (sessionMB.isPermitted(reRol + listar)) {
			isPermitted = true;
		}

		// log.info(" ==== isPermitted " + "eventos:list" + " " + isPermitted);
		return isPermitted;
	}

	/**
	 * Verifica si el usuario posee el permiso de agregar procesos.
	 * 
	 * @return
	 */
	public boolean isPermittedCrearRol() {
		boolean isPermitted = false;

		if (sessionMB.isPermitted(reRol + crear)) {
			isPermitted = true;
		}

		// log.info(" ==== isPermitted " + "rols:agregarproceso" + " " +
		// isPermitted);
		return isPermitted;
	}

	public String verDetallesRol(Rol r) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rid", r.getId());
		return Statics.URL_ROL_READ + Statics.html
				+ sessionMB.createQueryString(params);
	}

	public String editarRol(Rol r) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rid", r.getId());
		return Statics.URL_ROL_EDIT + Statics.html
				+ sessionMB.createQueryString(params);
	}

}
