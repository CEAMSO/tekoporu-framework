package app_client.app_client.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.ticpy.tekoporu.stereotype.ViewController;

import app_client.app_client.clientrest.AuthServiceCR;
import app_client.app_client.security.UsernameToken;
import app_common.app_common.constant.Statics;
import app_common.app_common.domain.Permiso;

/**
 * Controlador de sesión, posee los métodos y atributos relacionados a la
 * sessión
 * 
 * @author desa3
 * 
 */
@ManagedBean
@ViewController
// @SessionScoped
public class SessionMB {

	/** Inyecciones de dependencias **/

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;
	

	/**
	 * Método que ejecuta una redirección a una url determinada
	 * 
	 * @param url
	 *            La url a la que se desea redirigir
	 */
	public void redirect(String url) {
		try {
			ExternalContext ec = FacesContext.getCurrentInstance()
					.getExternalContext();
			String context = ec.getRequestContextPath();
			log.info("Redirigiendo a : " + url);

			ec.redirect(context + url);
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("\n No se pudo redirigir a " + url);
		}

	}

	/**
	 * Método que ejecuta una redirección a una url determinada con parámtros en
	 * el querystring.
	 * 
	 * @param url
	 *            La url a la que se desea redirigir
	 */
	public void redirect(String url, Map<String, Object> params) {
		redirect(url + createQueryString(params));
	}

	/**
	 * Método sobrecargado, redirige a una url con parámetros y hash de location
	 * 
	 * @param url
	 * @param params
	 * @param location
	 */
	public void redirect(String url, Map<String, Object> params, String location) {
		redirect(url + createQueryString(params) + "#" + location);
	}

	/**
	 * Crea el queryString de la url a partir del Map de parámetros
	 * 
	 * @param params
	 *            Map con clave nombre del parámetro y su valor como valor del
	 *            parámetro.
	 * @return
	 */
	public String createQueryString(Map<String, Object> params) {
		String queryString = "?";
		int numberParameters = params.keySet().size();
		int i = 1;
		for (String param : params.keySet()) {
			queryString = queryString + param + "=" + params.get(param);
			if (i < numberParameters) {
				queryString = queryString + "&";
				i++;
			}
		}
		return queryString;
	}

	/**
	 * Verifica que el usuario posee un permiso
	 * 
	 * @param permiso
	 *            Permiso a ser verificado
	 * @return true si posee el permiso y false de lo contrario
	 */
	public boolean isPermitted(String permiso) {
		Subject _subject = null;
		boolean permitted = false;

		try {
			_subject = SecurityUtils.getSubject();
			if (!_subject.isAuthenticated()) {
				permitted = false;
			}
		} catch (Exception e) {
			permitted = false;
		}

		if (_subject.isAuthenticated() && _subject.isPermitted(permiso)) {
			permitted = true;
		}

		return permitted;
	}

	/**
	 * Obtiene el username del usuario logueado
	 * 
	 * @return Username del usuario logueado
	 */
	public String getLoguedUser() {

		Subject _subject = null;
		String username = null;
		try {
			_subject = SecurityUtils.getSubject();
			if (_subject.isAuthenticated()) {
				username = (String) _subject.getPrincipal();
			}
		} catch (Exception e) {
			username = null;
		}

		return username;
	}

	/**
	 * Obtiene un atributo de la sesión del usuario
	 * 
	 * @param attributeKey
	 *            Clave del atributo
	 * @return El atributo
	 */
	public Object getAttributeFromSession(String attributeKey) {
		ExternalContext ec = facesContext.getExternalContext();
		HttpSession session = (HttpSession) ec.getSession(false);

		if (session != null) {
			return session.getAttribute(attributeKey);
		} else {
			return null;
		}

	}

	/**
	 * Guarda un atributo en la sesión dle usuario
	 * 
	 * @param attributeKey
	 *            Clave del atributo
	 * @param value
	 *            El atributo a ser guardado
	 */
	public void setAttributeFromSession(String attributeKey, Object value) {
		ExternalContext ec = facesContext.getExternalContext();
		HttpSession session = (HttpSession) ec.getSession(false);
		if (session != null) {
			session.setAttribute(attributeKey, value);
		} else {
			log.info("No se puedo establecer el atributo " + attributeKey
					+ " en la sesión");
		}
	}

	/**
	 * Verifica si la página pasada como parámetro es la anterior.
	 * 
	 * @param jsfPageName
	 *            Nombre de la página .jsf
	 * @return
	 */
	public boolean isReferer(String jsfPageName) {
		HttpServletRequest request = (HttpServletRequest) facesContext
				.getExternalContext().getRequest();
		try {
			String referer = request.getHeader("Referer");
			if (referer != null && jsfPageName != null) {
				String[] split = referer.split("/");
				int index = split.length;
				String url = split[index - 1];
				split = url.split(Pattern.quote("?"));
				System.out.println("\n \nreferer: " + split[0]);
				return jsfPageName.contains(split[0]);
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public void chekP(ComponentSystemEvent event) {
		if ((Long) this.getAttributeFromSession("llamadoid") == null) {
			this.redirect("/public/sesionExpirada.html");
		}
	}

	public void addArgs(String name, Object obj) {
		if (obj instanceof String) {
			obj = ((String) obj).replaceAll(Pattern.quote("&"), "&#38;");
		}

		RequestContext.getCurrentInstance().addCallbackParam(name, obj);
	}

	String menuJson = null;

	AuthServiceCR authCR = new AuthServiceCR();

	/*
	 * public String getMenuJson() { if (menuJson == null) { menuJson =
	 * authCR.getMenu(getLoguedUser()); } return menuJson; }
	 */

	/*
	 * public boolean renderMenuReportes() { return
	 * isPermitted(Statics.RECURSO_REPORTE + Statics.OPERACION_LIST); }
	 */
	
	String username;
	String password;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean autenticar(String username){
		Subject _subject = null;
	       
        if (username != null) { 
        	System.out.println("======== Autenticando User:" + username);
        	
    		boolean failure = false;
    		try {
    			_subject = SecurityUtils.getSubject();
    			if (_subject != null) {
    				if (_subject.isAuthenticated()) {
    					if (username.compareTo((String) _subject.getPrincipal()) != 0) {
            	    		System.out.println("Hay un usuario distinto autenticado");
            	    		_subject.logout();
            	    		
            	    	}
        	    		System.out.println("ya está autenticado");
        	    		return true;
        	    	}
        	    	
    				_subject.login(new UsernameToken(username));
    			}
    		} catch (Exception e) {
    			failure = true;
    			System.out.println("======== Error al autenticar: " + username);
    			e.printStackTrace();
    		}
    		return !failure;
        }else{
        	return false;
        }
	}

	public void iniciarSesion(){
		
		//loguear al usuario
		boolean ok = this.autenticar(username);
		
		System.out.println("USERNAME: " + username);
		if (ok){
			setAttributeFromSession("username",username);
			redirect(Statics.URL_PAGINA_PRINCIPAL);
		} else {
			redirect(Statics.STATUS_403);
		}
	}
	
	public void cerrarSession(){
		System.out.println("============= Logout");

		Subject _subject = SecurityUtils.getSubject();
		if (_subject != null) {
			_subject.logout();
		}
		//redirect("login.html");
	}

}
