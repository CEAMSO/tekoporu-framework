package ${package}.view;

import javax.inject.Inject;

import org.ticpy.tekoporu.stereotype.ViewController;

import ${package}.clientrest.BookmarkCR;

import app_common.app_common.domain.Bookmark;

@ViewController
public class BookmarkReadOnlyMB {

	@Inject
	BookmarkCR bookmarkCR;

	protected Long idBookmark;

	public Long getIdBookmark() {
		return idBookmark;
	}

	public void setIdBookmark(Long idBookmark) {
		this.idBookmark = idBookmark;
	}

	Bookmark bookmark;

	public Bookmark getBookmark() {
		return bookmark;
	}

	public void setBookmark(Bookmark bookmark) {
		this.bookmark = bookmark;
	}

	protected boolean init = false;

	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public void cargarBookmark() {
		if (!init) {
			this.bookmark = bookmarkCR.getBookmark(idBookmark);
			init = true;
		}

	}
}
