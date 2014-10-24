package ${package}.util.renderer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.api.UIData;
import org.primefaces.component.datatable.DataTableRenderer;
import org.primefaces.component.paginator.CurrentPageReportRenderer;
import org.primefaces.component.paginator.JumpToPageDropdownRenderer;
import org.primefaces.component.paginator.PageLinksRenderer;
import org.primefaces.component.paginator.PaginatorElementRenderer;
import org.primefaces.component.paginator.RowsPerPageDropdownRenderer;

public class APPDataTableRenderer extends DataTableRenderer {
    
	private static Map<String,PaginatorElementRenderer> PAGINATOR_ELEMENTS;
    
    static {
        PAGINATOR_ELEMENTS = new HashMap<String, PaginatorElementRenderer>();
        PAGINATOR_ELEMENTS.put("{CurrentPageReport}", new CurrentPageReportRenderer());
        PAGINATOR_ELEMENTS.put("{FirstPageLink}", new FirstPageLinkRenderer());
        PAGINATOR_ELEMENTS.put("{PreviousPageLink}", new PrevPageLinkRenderer());
        PAGINATOR_ELEMENTS.put("{NextPageLink}", new NextPageLinkRenderer());
        PAGINATOR_ELEMENTS.put("{LastPageLink}", new LastPageLinkRenderer());
        PAGINATOR_ELEMENTS.put("{PageLinks}", new PageLinksRenderer());
        PAGINATOR_ELEMENTS.put("{RowsPerPageDropdown}", new RowsPerPageDropdownRenderer());
        PAGINATOR_ELEMENTS.put("{JumpToPageDropdown}", new JumpToPageDropdownRenderer());
    }
    
    protected void encodePaginatorMarkup(FacesContext context, UIData uidata, String position) throws IOException {
    	if(!uidata.isPaginatorAlwaysVisible() && uidata.getPageCount() <= 1) {
            return;
        }
        
        ResponseWriter writer = context.getResponseWriter();
        boolean isTop = position.equals("top");
        
        String styleClass = isTop ? UIData.PAGINATOR_TOP_CONTAINER_CLASS : UIData.PAGINATOR_BOTTOM_CONTAINER_CLASS;
        String id = uidata.getClientId(context) + "_paginator_" + position; 
        
        //add corners
        if(!isTop && uidata.getFooter() == null) {
            styleClass = styleClass + " ui-corner-bottom";
        }
        else if(isTop && uidata.getHeader() == null) {
            styleClass = styleClass + " ui-corner-top";
        }
        
        writer.startElement("div", null);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("class", styleClass, null);
        
        String[] elements = uidata.getPaginatorTemplate().split(" ");
        for(String element : elements) {            
            PaginatorElementRenderer renderer = PAGINATOR_ELEMENTS.get(element);
            if(renderer != null)
                renderer.render(context, uidata);
            else
                writer.write(element + " ");
        }

        writer.endElement("div");
    }
}

