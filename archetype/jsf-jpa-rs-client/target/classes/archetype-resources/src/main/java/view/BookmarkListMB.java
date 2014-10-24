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

import ${package}.clientrest.BookmarkCR;

import app_common.app_common.constant.Statics;
import app_common.app_common.domain.Bookmark;
import app_common.app_common.domain.Permiso;
import app_common.app_common.util.PagedList;

/**
 * Controlador del listado de bookmarks de la vista bookmark_list.xthml
 * 
 * @author desa2
 * 
 */
@ViewController
@NextView("/secure/bookmark_read_only.xhtml?faces-redirect=true&amp;includeViewParams=true")
public class BookmarkListMB extends AbstractListPageBean<Bookmark, Long> {

	private static final long serialVersionUID = 1L;

	/** Inyecciones de dependencias **/

	@Inject
	private BookmarkCR bookmarkCR;

	@Inject
	private Logger log;

	@Inject
	private SessionMB sessionMB;

	/** Atributos de la clase **/

	private LazyDataModel<Bookmark> bookmarks;

	private Long tid = 5l;

	private String descripcion;

	private boolean cargarLista = false;

	private boolean mostrarResultados = false;

	private String motivo;

	private Long idBookmarkAEliminar;

	private Long idBookmarkAEditar;

	private boolean disableEliminar = true;

	/** RECURSOS **/

	private String reBookmark = Statics.RECURSO_BOOK;

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

	public LazyDataModel<Bookmark> getBookmarks() {
		return bookmarks;
	}

	public void setBookmarks(LazyDataModel<Bookmark> bookmarks) {
		this.bookmarks = bookmarks;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String des) {
		this.descripcion = des;
	}

	public boolean isDisableEliminar() {
		return disableEliminar;
	}

	public void setDisableEliminar(boolean disableEliminar) {
		this.disableEliminar = disableEliminar;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	@Override
	protected List<Bookmark> handleResultList() {
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

	public Long getIdBookmarkAEliminar() {
		return idBookmarkAEliminar;
	}

	public void setIdBookmarkAEliminar(Long idBookmarkAEliminar) {
		this.idBookmarkAEliminar = idBookmarkAEliminar;
	}

	public Long getIdBookmarkAEditar() {
		return idBookmarkAEditar;
	}

	public void setIdBookmarkAEditar(Long idBookmarkAEditar) {
		this.idBookmarkAEditar = idBookmarkAEditar;
	}

	/**
	 * Retorna el path al formulario de creación de bookmarks
	 * 
	 * @return
	 */
	public String agregarBookmark() {
		return "/secure/bookmark_edit.html?faces-redirect=true";
	}

	public void eliminarBookmarks() {
		for (Long bid : getSelectedList()) {
			bookmarkCR.deleteBookmark(bid);
		}
		setSelection(new HashMap<Long, Boolean>());
	}

	public void handleChange() {
		if (getSelectedList().size() >= 1) {
			this.disableEliminar = false;
		} else {
			this.disableEliminar = true;
		}
	}

	public void eliminarBookmark() {
		bookmarkCR.deleteBookmark(this.idBookmarkAEliminar);
	}

	/**
	 * Define el método load de la lista de bookmarks
	 */
	@SuppressWarnings("serial")
	@PostConstruct
	public void loadLazyModel() {

		bookmarks = new LazyDataModel<Bookmark>() {

			@Override
			public List<Bookmark> load(int first, int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, String> filters) {
				if (sortField == null) {
					sortField = "id"; // default sort field
				}
				// Bookmark bookmark = new Bookmark();

				PagedList<Bookmark> bookmarks;
				bookmarks = bookmarkCR.getBookmarks(pageSize, first, sortField,
						sortOrder.toString(), null);
				this.setRowCount((int) bookmarks.getTotal());
				mostrarResultados = true;
				return bookmarks.getList();

			}
		};
		bookmarks.setPageSize(Statics.PAGE_SIZE);
	}

	public void chekPermissions(ComponentSystemEvent event) {
		if (!sessionMB.isPermitted(reBookmark + listar)) {
			sessionMB.redirect(Statics.STATUS_403);
		}
	}

	/**
	 * Verifica si el usuario posee el permiso de leer el Bookmark t
	 * 
	 * @param t
	 * @return
	 */
	public boolean isPermittedLeerBookmark(Bookmark t) {
		String username = sessionMB.getLoguedUser();
		boolean isPermitted = false;

		if (sessionMB.isPermitted(reBookmark + listar)) {
			isPermitted = true;
		}

		return isPermitted;
	}

	/**
	 * Verifica si el usuario posee el permiso de agregar procesos.
	 * 
	 * @return
	 */
	public boolean isPermittedCrearBookmark() {
		boolean isPermitted = false;

		if (sessionMB.isPermitted(reBookmark + crear)) {
			isPermitted = true;
		}
		return isPermitted;
	}

	public String verDetallesBookmark(Bookmark b) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bid", b.getId());
		return Statics.URL_BOOKMARK_READ + Statics.html
				+ sessionMB.createQueryString(params);
	}

	public String editarBookmark(Bookmark b) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bid", b.getId());
		return Statics.URL_BOOKMARK_EDIT + Statics.html
				+ sessionMB.createQueryString(params);
	}

}
