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

import ${package}.clientrest.PermisoCR;

import app_common.app_common.constant.Statics;
import app_common.app_common.domain.Permiso;
import app_common.app_common.util.PagedList;

/**
 * Controlador del listado de permisos de la vista permiso_list.xthml
 * 
 * @author desa2
 * 
 */
@ViewController
@NextView("/secure/permiso_read_only.xhtml?faces-redirect=true&amp;includeViewParams=true")
public class PermisoListMB extends AbstractListPageBean<Permiso, Long> {

	private static final long serialVersionUID = 1L;

	/** Inyecciones de dependencias **/

	@Inject
	private PermisoCR permisoCR;

	@Inject
	private Logger log;

	@Inject
	private SessionMB sessionMB;

	/** Atributos de la clase **/

	private LazyDataModel<Permiso> permisos;

	private boolean cargarLista = false;

	private boolean mostrarResultados = false;

	/** RECURSOS **/

	private String rePermiso = Statics.RECURSO_PERMISO;

	/** OPERACIONES **/

	private String listar = Statics.OPERACION_LISTAR;
	private String leer = Statics.OPERACION_LEER;

	/** Setters y getters **/

	public boolean isCargarLista() {
		return cargarLista;
	}

	public void setCargarLista(boolean cargarLista) {
		this.cargarLista = cargarLista;
	}

	public LazyDataModel<Permiso> getPermisos() {
		return permisos;
	}

	public void setPermisos(LazyDataModel<Permiso> permisos) {
		this.permisos = permisos;
	}

	@Override
	protected List<Permiso> handleResultList() {
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

	/**
	 * Define el método load de la lista de permisos
	 */
	@SuppressWarnings("serial")
	@PostConstruct
	public void loadLazyModel() {

		permisos = new LazyDataModel<Permiso>() {

			@Override
			public List<Permiso> load(int first, int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, String> filters) {
				if (sortField == null) {
					sortField = "id"; // default sort field
				}
				// falta crear el permiso con los filtros
				Permiso permiso = new Permiso();

				PagedList<Permiso> permisos;
				permisos = permisoCR.getPermisos(pageSize, first, sortField,
						sortOrder.toString(), null);

				this.setRowCount((int) permisos.getTotal());
				mostrarResultados = true;
				return permisos.getList();

			}
		};
		permisos.setPageSize(Statics.PAGE_SIZE);
	}

	public void chekPermissions(ComponentSystemEvent event) {
		if (!sessionMB.isPermitted(rePermiso + listar)) {
			sessionMB.redirect(Statics.STATUS_403);
		}
	}

	/**
	 * Verifica si el usuario posee el permiso de leer el Permiso t
	 * 
	 * @param t
	 * @return
	 */
	public boolean isPermittedLeerPermiso(Permiso t) {
		String username = sessionMB.getLoguedUser();
		boolean isPermitted = false;

		if (sessionMB.isPermitted(rePermiso + leer)) {
			isPermitted = true;
		}

		return isPermitted;
	}

	public String verDetallesPermiso(Permiso p) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pid", p.getId());
		return Statics.URL_PERMISO_READ + Statics.html
				+ sessionMB.createQueryString(params);
	}

}
