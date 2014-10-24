package ${package}.view;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.ticpy.tekoporu.stereotype.ViewController;

import ${package}.clientrest.UsuarioCR;

import app_common.app_common.domain.Rol;
import app_common.app_common.domain.Usuario;

@ViewController
public class UsuarioReadOnlyMB {

	@Inject
	UsuarioCR usuarioCR;

	protected Long idUsuario;

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	Usuario usuario;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	protected boolean init = false;

	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	private List<Rol> roles = new ArrayList<Rol>();

	public List<Rol> getRoles() {
		return roles;
	}

	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}

	public void cargarUsuario() {
		if (!init) {
			this.usuario = usuarioCR.getUsuario(idUsuario);
			for (Rol r : usuario.getRoles()) {
				roles.add(r);
			}
			init = true;
		}

	}
}
