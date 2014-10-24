package ${package}.util.renderer;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.api.UIData;
import org.primefaces.component.paginator.PageLinkRenderer;
import org.primefaces.component.paginator.PaginatorElementRenderer;

public class NextPageLinkRenderer extends PageLinkRenderer implements
		PaginatorElementRenderer {

	@Override
	public void render(FacesContext context, UIData uidata) throws IOException {
		int currentPage = uidata.getPage();
        int pageCount = uidata.getPageCount();
        
        boolean disabled = (currentPage == (pageCount - 1)) || (currentPage == 0 && pageCount == 0);
       
        this.render(context, uidata, UIData.PAGINATOR_NEXT_PAGE_LINK_CLASS, UIData.PAGINATOR_NEXT_PAGE_ICON_CLASS, disabled);
	}
	

	public void render(FacesContext context, UIData uidata, String linkClass, String iconClass, boolean disabled) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String styleClass = disabled ? linkClass + " ui-state-disabled" : linkClass;

        writer.startElement("span", null);
        writer.writeAttribute("class", styleClass, null);
        
        writer.startElement("span", null);
        writer.writeAttribute("class", iconClass, null);
        writer.writeText("›", null);
        writer.endElement("span");
        
        writer.endElement("span");
    }    

}
