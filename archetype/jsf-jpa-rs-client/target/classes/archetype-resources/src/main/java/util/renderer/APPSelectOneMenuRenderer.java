package ${package}.util.renderer;

import java.io.IOException;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.selectonemenu.SelectOneMenuRenderer;

public class APPSelectOneMenuRenderer extends SelectOneMenuRenderer {
	@Override	
	protected void encodePanel(FacesContext context, SelectOneMenu menu, List<SelectItem> selectItems) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        boolean customContent = menu.getVar() != null;
        String panelStyle = menu.getPanelStyle();
        String panelStyleClass = menu.getPanelStyleClass();
        panelStyleClass = panelStyleClass == null ? SelectOneMenu.PANEL_CLASS : SelectOneMenu.PANEL_CLASS + " " + panelStyleClass;
               
        writer.startElement("div", null);
        writer.writeAttribute("id", menu.getClientId(context) + "_panel", null);
        writer.writeAttribute("class", panelStyleClass, null);
        
        if(panelStyle != null) {
            writer.writeAttribute("style", panelStyle, null);
        }
        
        if(menu.isFilter()) {
            encodeFilter(context, menu);
        }
        
        writer.startElement("div", null);
        writer.writeAttribute("class", SelectOneMenu.ITEMS_WRAPPER_CLASS, null);
        writer.writeAttribute("style", "height:" + calculateWrapperHeight(menu, selectItems.size()), null);

        if(customContent) {
            writer.startElement("table", menu);
            writer.writeAttribute("class", SelectOneMenu.TABLE_CLASS, null);
            writer.startElement("tbody", menu);
            encodeOptionsAsTable(context, menu, selectItems);
            writer.endElement("tbody");
            writer.endElement("table");
        } 
        else {
            writer.startElement("ul", menu);
            writer.writeAttribute("class", SelectOneMenu.LIST_CLASS, null);
            encodeOptionsAsList(context, menu, selectItems);
            writer.endElement("ul");
        }
        
        writer.endElement("div");
        writer.endElement("div");
    }
	
	@Override	
	protected void encodeMarkup(FacesContext context, SelectOneMenu menu) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        List<SelectItem> selectItems = getSelectItems(context, menu);
        String clientId = menu.getClientId(context);
        Converter converter = menu.getConverter();
        Object values = getValues(menu);
        Object submittedValues = getSubmittedValues(menu);
        boolean valid = menu.isValid();
                
        String style = menu.getStyle();
        String styleClass = menu.getStyleClass();
        styleClass = styleClass == null ? SelectOneMenu.STYLE_CLASS : SelectOneMenu.STYLE_CLASS + " " + styleClass;
        styleClass = !valid ? styleClass + " ui-state-error" : styleClass;
        styleClass = menu.isDisabled() ? styleClass + " ui-state-disabled" : styleClass;

        writer.startElement("div", menu);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleclass");
        if(style != null) {
            writer.writeAttribute("style", style, "style");
        }

        encodeInput(context, menu, clientId, selectItems, values, submittedValues, converter);
        encodeLabel(context, menu, selectItems);
        //encodeMenuIcon(context, menu, valid);
        encodePanel(context, menu, selectItems);
        
        writer.startElement("div", menu);
        writer.writeAttribute("class", "ui-selectonemenu-trigger", null);
        writer.endElement("div");
        
        writer.startElement("span", menu);
        writer.writeAttribute("class", "caret", null);
        writer.endElement("span");
        
        
        writer.endElement("div");
    }
}
