package com.senolight.InventoryManagementSystem.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Login - Inventory Management System")
@Route("login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login");
        login.setForgotPasswordButtonVisible(false);

        H1 title = new H1("Seno Light");
        H2 subtitle = new H2("Inventory Management System");

        title.getStyle().set("color", "var(--lumo-primary-color)");
        subtitle.getStyle().set("color", "var(--lumo-secondary-text-color)");

        add(title, subtitle, login);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // Check for various error parameters
        boolean hasError = beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error");

        boolean hasLogout = beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("logout");

        if (hasError) {
            login.setError(true);
        }

        if (hasLogout) {
            //TODO: add a success message here
            login.setError(false);
        }
    }
}
