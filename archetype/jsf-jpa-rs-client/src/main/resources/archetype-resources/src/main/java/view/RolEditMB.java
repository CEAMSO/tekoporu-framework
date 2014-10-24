package ${package}.view;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.primefaces.context.RequestContext;
import org.ticpy.tekoporu.stereotype.ViewController;

import ${package}.clientrest.PermisoCR;
import ${package}.clientrest.RolCR;

import app_common.app_common.constant.Statics;
import app_common.app_common.domain.Permiso;
import app_common.app_common.domain.Rol;

@ViewController
public class RolEditMB {

	@Inject
	RolCR rolCR;

	@Inject
	PermisoCR permisoCR;

	@Inject
	private SessionMB sessionMB;

	protected Long idRol;

	public Long getIdRol() {
		return idRol;
	}

	public void setIdRol(Long idRol) {
		this.idRol = idRol;
	}

	Rol rol = new Rol();

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	protected boolean init = false;
	private boolean modoEdicion = false;

	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public boolean isModoEdicion() {
		return modoEdicion;
	}

	public void setModoEdicion(boolean modoEdicion) {
		this.modoEdicion = modoEdicion;
	}

	/**
	 * Verifica que el permiso se encuentre en la lista de permisos del rol
	 * 
	 * @param permiso
	 *            El permiso a verificarse si es poseído por el rol
	 * @return true si el rol posee el permiso y false de lo contrario
	 */
	private boolean contiene(Permiso permiso) {
		for (Permiso m : this.rol.getPermisos()) {
			if (m.getId().longValue() == rol.getId().longValue()) {
				return true;
			}
		}
		return false;
	}

	private List<Permiso> listaPermisos;

	public List<Permiso> getListaPermisos() {
		if (listaPermisos == null) {
			listaPermisos = permisoCR.getPermisos();
		}

		return listaPermisos;
	}

	private Map<Long, Boolean> selectionPermisos = new HashMap<Long, Boolean>();

	public Map<Long, Boolean> getSelectionPermisos() {
		return selectionPermisos;
	}

	public void setSelectionPermisos(Map<Long, Boolean> selectionPermisos) {
		this.selectionPermisos = selectionPermisos;
	}

	public void cargarRol() {
		if (!init) {
			if (idRol != null) {
				this.rol = rolCR.getRol(idRol);
				modoEdicion = true;
				Map<Long, Boolean> map2 = new HashMap<Long, Boolean>();
				for (Permiso p : rol.getPermisos()) {
					map2.put(p.getId(), true);
				}
				setSelectionPermisos(map2);
			}

			init = true;
		}
	}

	/**
	 * Crea un nuevo evento.
	 */
	public void insertAjax() {
		try {

			Set<Permiso> permisos = new HashSet<Permiso>();
			for (Permiso p : getListaPermisos()) {
				boolean seleccionado = getSelectionPermisos().get(rol.getId())
						.booleanValue();
				// boolean contiene = this.contiene(cargo);
				/**
				 * Si es true el rol se le asignó y se debe agregar a la lista
				 * de cargos del funcionario
				 */
				if (seleccionado) {
					permisos.add(p);
				}
			}
			rol.setPermisos(permisos);

			rol = rolCR.createRol(rol);
			RequestContext.getCurrentInstance().addCallbackParam("exito", true);
			redirectAfterInsert();
		} catch (Exception e) {
			RequestContext.getCurrentInstance().addCallbackParam("excepcion",
					true);
		}
	}

	public void redirectAfterInsert() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rid", getRol().getId());
		String editUrl = Statics.URL_ROL_EDIT + Statics.html;
		sessionMB.redirect(editUrl, params);
	}

	/**
	 * Actualiza un evento existente.
	 */
	public void updateAjax() {
		try {

			Set<Permiso> permisos = new HashSet<Permiso>();
			for (Permiso p : getListaPermisos()) {
				boolean seleccionado = getSelectionPermisos().get(p.getId())
						.booleanValue();
				// boolean contiene = this.contiene(cargo);
				/**
				 * Si es true el rol se le asignó y se debe agregar a la lista
				 * de cargos del funcionario
				 */
				if (seleccionado) {
					permisos.add(p);
				}
			}
			rol.setPermisos(permisos);

			Rol b = rolCR.updateRol(this.rol);
			setRol(b);
			RequestContext.getCurrentInstance().addCallbackParam("exito", true);
		} catch (Exception e) {
			e.printStackTrace();
			RequestContext.getCurrentInstance().addCallbackParam("excepcion",
					true);
		}

	}
}
