package ${package}.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.ticpy.tekoporu.internal.implementation.PaginationImpl;
import org.ticpy.tekoporu.pagination.Pagination;

import ${package}.business.BookmarkBC;

import app_common.app_common.api.BookmarkAPI;
import app_common.app_common.domain.Bookmark;
import app_common.app_common.util.PagedList;

/**
 * Servicio REST para las operaciones sobre Bookmarks, implementa la interfaz
 * BookmarkAPI
 * 
 * @author desa2
 * 
 */
public class BookmarkRS implements BookmarkAPI {

	/** Inyecciones de dependencias **/

	@Inject
	private BookmarkBC bookmarkBC;

	@Inject
	private Logger log;

	/** Métodos de la API **/

	@Override
	public Response getBookmarks(int page, int limit, String sortField,
			String sortOrder, Bookmark bookmark) {
		List<Bookmark> finalList = new ArrayList<Bookmark>();
		PagedList<Bookmark> lista = new PagedList<Bookmark>();

		Pagination pag = null;

		// paginar o no
		if (page != 0) {
			pag = new PaginationImpl();
			pag.setPageSize(limit);
			pag.setCurrentPage(page - 1);
		}

		// setear order by
		boolean asc = true;
		if (sortField != null) {
			if (sortOrder.compareTo("ASCENDING") != 0) {
				asc = false;
			}
		}
		finalList = bookmarkBC.findAll(sortField, asc, bookmark, pag);
		lista.setList(finalList);
		if (pag != null) {
			lista.setTotal(pag.getTotalResults());
		} else {
			lista.setTotal(finalList.size());
		}
		return Response.ok(lista).build();
	}

	@Override
	public Response getBookmark(Long id) {
		Bookmark bookmark = bookmarkBC.load(id);

		if (bookmark == null) {
			return Response.ok(new HashMap<Object, Object>()).build();
		} else {
			return Response.ok(bookmark).build();
		}
	}

	@Override
	public Response deleteBookmark(Long id) {
		Map<String, Object> root = new HashMap<String, Object>();
		try {
			bookmarkBC.delete(id);
			root.put("success", true);
		} catch (EntityNotFoundException e) {
			log.error("WARNING - No existe el ID");
			root.put("success", false);
		}

		return Response.ok(root).build();
	}

	@Override
	public Response createBookmark(Bookmark bookmark) {
		Map<String, Object> root = new HashMap<String, Object>();
		if (bookmark.getId() != null
				&& bookmarkBC.load(bookmark.getId()) != null) {
			log.error("WARNING - Ya existe el ID");
			root.put("success", false);
		} else {
			Bookmark b = bookmarkBC.updateBookmark(bookmark);
			root.put("success", true);
			root.put("bookmark", b.toString());
		}

		return Response.ok(root).build();
	}

	@Override
	public Response updateBookmark(Long id, Bookmark bookmark) {
		Map<String, Object> root = new HashMap<String, Object>();
		Bookmark bookmarkRecuperado = bookmarkBC.load(bookmark.getId());
		if (bookmark.getId() != null && bookmarkRecuperado != null) {
			bookmarkRecuperado.setDescription(bookmark.getDescription());
			bookmarkRecuperado.setLink(bookmark.getLink());
			Bookmark t = bookmarkBC.updateBookmark(bookmarkRecuperado);
			root.put("success", true);
			root.put("bookmark", t.toString());
		} else {
			log.error("WARNING - No existe un Bookmark con el ID asignado");
			root.put("success", false);
		}
		return Response.ok(root).build();
	}

}
