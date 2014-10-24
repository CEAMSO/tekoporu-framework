package ${package}.view;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.ticpy.tekoporu.stereotype.ViewController;

import ${package}.clientrest.RolCR;

import app_common.app_common.domain.Permiso;
import app_common.app_common.domain.Rol;

@ViewController
public class RolReadOnlyMB {

	@Inject
	RolCR rolCR;

	protected Long idRol;

	public Long getIdRol() {
		return idRol;
	}

	public void setIdRol(Long idRol) {
		this.idRol = idRol;
	}

	Rol rol;

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	protected boolean init = false;

	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	private List<Permiso> permisos = new ArrayList<Permiso>();

	public List<Permiso> getPermisos() {
		return permisos;
	}

	public void setPermisos(List<Permiso> permisos) {
		this.permisos = permisos;
	}

	public void cargarRol() {
		if (!init) {
			this.rol = rolCR.getRol(idRol);
			for (Permiso p : rol.getPermisos()) {
				permisos.add(p);
			}
			init = true;
		}

	}
}
