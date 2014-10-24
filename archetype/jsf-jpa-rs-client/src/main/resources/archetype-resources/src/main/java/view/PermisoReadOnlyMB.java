package ${package}.view;

import javax.inject.Inject;

import org.ticpy.tekoporu.stereotype.ViewController;

import ${package}.clientrest.PermisoCR;

import app_common.app_common.domain.Permiso;

@ViewController
public class PermisoReadOnlyMB {

	@Inject
	PermisoCR permisoCR;

	protected Long idPermiso;

	public Long getIdPermiso() {
		return idPermiso;
	}

	public void setIdPermiso(Long idPermiso) {
		this.idPermiso = idPermiso;
	}

	Permiso permiso;

	public Permiso getPermiso() {
		return permiso;
	}

	public void setPermiso(Permiso permiso) {
		this.permiso = permiso;
	}

	protected boolean init = false;

	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public void cargarPermiso() {
		if (!init) {
			this.permiso = permisoCR.getPermiso(idPermiso);
			init = true;
		}

	}
}
