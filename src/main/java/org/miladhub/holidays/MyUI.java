package org.miladhub.holidays;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

public class MyUI extends UI {
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();

        final TextField name = new TextField();
        name.setCaption("Type your name here:");

        Button button = new Button("Click Me");
        button.addClickListener(e -> {
            layout.addComponent(new Label("Thanks " + name.getValue()
                    + ", it works!"));
        });

        Table table = new Table();

        table.addContainerProperty("Label", Label.class, null);
        table.addContainerProperty("Comments", TextField.class, null);
        table.addContainerProperty("Details", Button.class, null);

        for (int i = 0; i < 100; i++) {
            final int row = i;

            Label label = new Label("Hello");

            TextField comments = new TextField();
            comments.addValueChangeListener((Property.ValueChangeListener) event -> {
                Notification.show("Label on row " + row
                        + " changed to " + event.getProperty().getValue());
            });

            Button showDetails = new Button("show details");
            showDetails.setData(i);
            showDetails.addClickListener((Button.ClickListener) event -> {
                Integer iid = (Integer) event.getButton().getData();
                Notification.show("Link " + iid + " clicked.");
            });
            showDetails.addStyleName("link");

            table.addItem(new Object[]{label, comments, showDetails}, i);
        }

        table.setPageLength(3);

        layout.addComponents(name, button, table);
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
