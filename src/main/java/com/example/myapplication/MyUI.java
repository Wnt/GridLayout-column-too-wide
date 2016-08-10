package com.example.myapplication;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    private VerticalLayout layout;
    private Map<String, String> i18n = new HashMap<>();
    private Component createHeaderComponent;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        i18n.put("generatedOn", "Report generated on");
        i18n.put("name", "Report title");
        i18n.put("recipents", "Recipents");
        i18n.put("company", "Company");
        i18n.put("site", "Site");

        layout = new VerticalLayout(new Button("X", e -> {
            redraw();
        }));
        setContent(layout);
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setWidth("915px");
        redraw();
    }

    private void redraw() {
        if (createHeaderComponent != null) {
            layout.removeComponent(createHeaderComponent);
        }
        createHeaderComponent = createHeaderComponent(getDummyHeaderData());
        layout.addComponent(createHeaderComponent);
        
    }

    private Map<String, String> getDummyHeaderData() {
        HashMap<String, String> headerData = new HashMap<String, String>();
        headerData.put(i18n.get("generatedOn"), new Date().toString());
        headerData.put(i18n.get("name"), "My fine report");
        headerData.put(i18n.get("recipents"), prettyPrintRecipents(
                "jonni@example.com,jonni.nakari@example.com\njonni@example.com"));

        // TODO check these two
        headerData.put(i18n.get("company"), "Example Company");
        headerData.put(i18n.get("site"), "Turku");
        return headerData;
    }

    private String prettyPrintRecipents(String recipients) {
        StringBuilder sb = new StringBuilder();
        for (String recipient : recipients.split(",|;|\\n")) {
            sb.append(recipient.trim());
            sb.append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    public Component createHeaderComponent(Map<String, String> headerData) {
        GridLayout reportProperties = new GridLayout(2, 1);
        reportProperties.addStyleName("report-properties");
        reportProperties.setHeight("100%");
        reportProperties.setWidth("396px");
        CssLayout legend = new CssLayout();
        legend.addStyleName("legend");
        legend.setHeight("100%");
        legend.setWidth("396px");
        CssLayout headerLayout = new CssLayout(reportProperties, legend);
        headerLayout.addStyleName("header-layout");
        headerLayout.setSizeFull();
        CssLayout headerWrapper = new CssLayout(headerLayout);
        headerWrapper.addStyleName("header-wrapper");
        headerWrapper.setHeight("250px");
        headerWrapper.setWidth("100%");
        
        for (String caption : headerData.keySet()) {
            // reportProperties.insertRow(reportProperties.getRows());
            String value = headerData.get(caption);
            Label captionLabel = new Label(caption);
            captionLabel.setWidthUndefined();
            captionLabel.addStyleName("caption");
            Label valueLabel = new Label(value);
            valueLabel.setWidthUndefined();
            valueLabel.addStyleName("value");
            reportProperties.addComponent(captionLabel);
            reportProperties.addComponent(valueLabel);
        }
        reportProperties.setColumnExpandRatio(1, 1);
        
        return headerWrapper;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
