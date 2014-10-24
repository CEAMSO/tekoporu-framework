package ${package}.util.renderer;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.api.UIData;
import org.primefaces.component.paginator.PageLinkRenderer;
import org.primefaces.component.paginator.PaginatorElementRenderer;

public class FirstPageLinkRenderer extends PageLinkRenderer implements
		PaginatorElementRenderer {

	@Override
	public void render(FacesContext context, UIData uidata) throws IOException {
        boolean disabled = uidata.getPage() == 0;
       
        this.render(context, uidata, UIData.PAGINATOR_FIRST_PAGE_LINK_CLASS, UIData.PAGINATOR_FIRST_PAGE_ICON_CLASS, disabled);
	}
	

	public void render(FacesContext context, UIData uidata, String linkClass, String iconClass, boolean disabled) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String styleClass = disabled ? linkClass + " ui-state-disabled" : linkClass;

        writer.startElement("span", null);
        writer.writeAttribute("class", styleClass, null);
        writer.startElement("span", null);
        writer.writeAttribute("class", iconClass, null);
        writer.writeText("«", null);
        writer.endElement("span");
        
        writer.endElement("span");
    }    

}
