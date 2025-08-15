package com.senolight.InventoryManagementSystem.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Access Denied")
@Route("access-denied")
@AnonymousAllowed
public class AccessDeniedView extends VerticalLayout {

    public AccessDeniedView() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        H1 header = new H1("Access Denied");
        header.getStyle().set("color", "var(--lumo-error-color)");

        Paragraph message = new Paragraph("You don't have permission to access this resource.");

        Button backButton = new Button("Go Back", VaadinIcon.ARROW_LEFT.create());
        backButton.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate(DashBoardView.class));
        });

        add(header, message, backButton);
    }
}