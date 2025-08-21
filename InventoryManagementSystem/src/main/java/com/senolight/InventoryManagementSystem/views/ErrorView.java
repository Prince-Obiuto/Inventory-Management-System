package com.senolight.InventoryManagementSystem.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.servlet.http.HttpServletResponse;

@PageTitle("Error")
@ParentLayout(MainLayout.class)
@AnonymousAllowed
public class ErrorView extends Div implements HasErrorParameter<Exception> {

    public ErrorView() {
        H1 header = new H1("Something went wrong");
        add(header);

        Paragraph explanation = new Paragraph("We're sorry, but an error occurred while processing your request.");
        add(explanation);

        RouterLink homeLink = new RouterLink("Go to Dashboard", DashBoardView.class);
        add(homeLink);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent beforeEnterEvent, ErrorParameter<Exception> errorParameter) {
        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }
}