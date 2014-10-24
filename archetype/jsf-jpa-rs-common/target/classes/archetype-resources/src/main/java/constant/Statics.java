package app_common.app_common.constant;

/*
 * Esta clase cuenta con todas las constantes del sistema.
 * 
 */
public class Statics {

	/** Extensiones **/
	public static String html = ".html";
	public static String xhtml = ".xhtml";

	public static String RECURSO_BOOK = "bookmark";
	public static String RECURSO_USUARIO = "usuario";
	public static String RECURSO_ROL = "rol";
	public static String RECURSO_PERMISO = "permiso";

	public static String OPERACION_LISTAR = ":listar";
	public static String OPERACION_LEER = ":leer";
	public static String OPERACION_ELIMINAR = ":eliminar";
	public static String OPERACION_CREAR = ":crear";
	public static String OPERACION_MODIFICAR = ":modificar";

	public static String URL_PAGINA_PRINCIPAL = "/index.html";
	
	public static String STATUS_403 = "/public/403.html";
	public static String STATUS_404 = "/public/404.html";
	public static String STATUS_422 = "/public/422.html";

	public static String URL_ENLACE_INVALIDO = "/public/enlace_invalido.html";
	public static String URL_ENLACE_EXPIRADO = "/public/enlace_expirado.html";
	
	public static String URL_PERMISO_READ = "/secure/permiso_read_only";
	public static String URL_BOOKMARK_READ = "/secure/bookmark_read_only";
	public static String URL_USUARIO_READ = "/secure/usuario_read_only";
	public static String URL_ROL_READ = "/secure/rol_read_only";
	
	public static String URL_PERMISO_EDIT = "/secure/permiso_edit";
	public static String URL_BOOKMARK_EDIT = "/secure/bookmark_edit";
	public static String URL_USUARIO_EDIT = "/secure/usuario_edit";
	public static String URL_ROL_EDIT = "/secure/rol_edit";
	
	/** TAMAÑO DE PAGINA **/
	public static int PAGE_SIZE = 8;

	/** LONGITUD DEL HASH **/
	public static int HASH_SIZE = 64;
}
