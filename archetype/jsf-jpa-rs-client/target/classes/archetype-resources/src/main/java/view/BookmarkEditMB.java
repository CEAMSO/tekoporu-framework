package ${package}.view;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.primefaces.context.RequestContext;
import org.ticpy.tekoporu.stereotype.ViewController;

import ${package}.clientrest.BookmarkCR;

import app_common.app_common.constant.Statics;
import app_common.app_common.domain.Bookmark;

@ViewController
public class BookmarkEditMB {

	@Inject
	BookmarkCR bookmarkCR;

	@Inject
	private SessionMB sessionMB;

	protected Long idBookmark;

	public Long getIdBookmark() {
		return idBookmark;
	}

	public void setIdBookmark(Long idBookmark) {
		this.idBookmark = idBookmark;
	}

	Bookmark bookmark = new Bookmark();

	public Bookmark getBookmark() {
		return bookmark;
	}

	public void setBookmark(Bookmark bookmark) {
		this.bookmark = bookmark;
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

	public void cargarBookmark() {
		if (!init) {
			if (idBookmark != null) {
				this.bookmark = bookmarkCR.getBookmark(idBookmark);
				modoEdicion = true;
			}

			init = true;
		}
	}

	/**
	 * Crea un nuevo evento.
	 */
	public void insertAjax() {
		try {
			bookmark = bookmarkCR.createBookmark(bookmark);
			RequestContext.getCurrentInstance().addCallbackParam("exito", true);
			redirectAfterInsert();
		} catch (Exception e) {
			RequestContext.getCurrentInstance().addCallbackParam("excepcion",
					true);
		}
	}

	public void redirectAfterInsert() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bid", getBookmark().getId());
		String editUrl = Statics.URL_BOOKMARK_EDIT + Statics.html;
		sessionMB.redirect(editUrl, params);
	}

	/**
	 * Actualiza un evento existente.
	 */
	public void updateAjax() {
		try {
			Bookmark b = bookmarkCR.updateBookmark(this.bookmark);
			setBookmark(b);
			RequestContext.getCurrentInstance().addCallbackParam("exito", true);
		} catch (Exception e) {
			RequestContext.getCurrentInstance().addCallbackParam("excepcion",
					true);
		}

	}
}
