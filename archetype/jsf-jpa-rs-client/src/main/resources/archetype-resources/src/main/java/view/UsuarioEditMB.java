package ${package}.view;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.primefaces.context.RequestContext;
import org.ticpy.tekoporu.stereotype.ViewController;

import ${package}.clientrest.RolCR;
import ${package}.clientrest.UsuarioCR;

import app_common.app_common.constant.Statics;
import app_common.app_common.domain.Rol;
import app_common.app_common.domain.Usuario;

@ViewController
public class UsuarioEditMB {

	@Inject
	UsuarioCR usuarioCR;

	@Inject
	RolCR rolCR;

	@Inject
	private SessionMB sessionMB;

	protected Long idUsuario;

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	Usuario usuario = new Usuario();

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
	 * Verifica que el rol se encuentre en la lista de roles del usuario
	 * 
	 * @param rol
	 *            El rol a verificarse si es poseído por el usuario
	 * @return true si el usuario posee el rol y false de lo contrario
	 */
	private boolean contiene(Rol rol) {
		for (Rol m : this.usuario.getRoles()) {
			if (m.getId().longValue() == rol.getId().longValue()) {
				return true;
			}
		}
		return false;
	}

	private List<Rol> listaRoles;

	public List<Rol> getListaRoles() {
		if (listaRoles == null) {
			listaRoles = rolCR.getRoles();
		}

		return listaRoles;
	}

	private Map<Long, Boolean> selectionRoles = new HashMap<Long, Boolean>();

	public Map<Long, Boolean> getSelectionRoles() {
		return selectionRoles;
	}

	public void setSelectionRoles(Map<Long, Boolean> selectionRoles) {
		this.selectionRoles = selectionRoles;
	}

	private String pwd = "";

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void cargarUsuario() {
		if (!init) {
			if (idUsuario != null) {
				this.usuario = usuarioCR.getUsuario(idUsuario);
				modoEdicion = true;
				this.pwd = this.usuario.getPwd();
				Map<Long, Boolean> map2 = new HashMap<Long, Boolean>();
				for (Rol rol : usuario.getRoles()) {
					map2.put(rol.getId(), true);
				}
				setSelectionRoles(map2);
			}

			init = true;
		}
	}

	/**
	 * Crea un nuevo evento.
	 */
	public void insertAjax() {
		try {

			usuario.setActivo(true);
			usuario.setPwd(new Sha256Hash(pwd).toHex());
			Set<Rol> roles = new HashSet<Rol>();
			for (Rol rol : getListaRoles()) {
				boolean seleccionado = getSelectionRoles().get(rol.getId())
						.booleanValue();
				// boolean contiene = this.contiene(cargo);
				/**
				 * Si es true el rol se le asignó y se debe agregar a la lista
				 * de cargos del funcionario
				 */
				if (seleccionado) {
					roles.add(rol);
				}
			}
			usuario.setRoles(roles);

			usuario = usuarioCR.createUsuario(usuario);
			RequestContext.getCurrentInstance().addCallbackParam("exito", true);
			redirectAfterInsert();
		} catch (Exception e) {
			RequestContext.getCurrentInstance().addCallbackParam("excepcion",
					true);
		}
	}

	public void redirectAfterInsert() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", getUsuario().getId());
		String editUrl = Statics.URL_USUARIO_EDIT + Statics.html;
		sessionMB.redirect(editUrl, params);
	}

	/**
	 * Actualiza un evento existente.
	 */
	public void updateAjax() {
		try {

			usuario.setPwd(new Sha256Hash(pwd).toHex());
			Set<Rol> roles = new HashSet<Rol>();
			for (Rol rol : getListaRoles()) {
				boolean seleccionado = getSelectionRoles().get(rol.getId())
						.booleanValue();
				// boolean contiene = this.contiene(cargo);
				/**
				 * Si es true el rol se le asignó y se debe agregar a la lista
				 * de cargos del funcionario
				 */
				if (seleccionado) {
					roles.add(rol);
				}
			}
			usuario.setRoles(roles);

			Usuario b = usuarioCR.updateUsuario(this.usuario);
			setUsuario(b);
			RequestContext.getCurrentInstance().addCallbackParam("exito", true);
		} catch (Exception e) {
			RequestContext.getCurrentInstance().addCallbackParam("excepcion",
					true);
		}

	}
}
