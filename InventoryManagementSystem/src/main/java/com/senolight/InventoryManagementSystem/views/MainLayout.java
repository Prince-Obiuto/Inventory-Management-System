package com.senolight.InventoryManagementSystem.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@PageTitle("Inventory Management")
@PermitAll
public class MainLayout extends AppLayout {

    private H1 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        Button logoutButton = new Button("Logout", new Icon(VaadinIcon.SIGN_OUT));
        logoutButton.addClickListener(e -> {
            VaadinServletRequest vaadinServletRequest = (VaadinServletRequest) VaadinService.getCurrentRequest();
            HttpServletRequest httpServletRequest = vaadinServletRequest.getHttpServletRequest();
            new SecurityContextLogoutHandler().logout(httpServletRequest, null, null);
            UI.getCurrent().getPage().setLocation("/login?logout");
        });

        Header header = new Header(toggle, viewTitle);
        header.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.Display.FLEX, LumoUtility.Padding.End.MEDIUM, LumoUtility.Width.FULL);
        header.add(logoutButton);

        addToNavbar(true, header);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Seno Light Inventory Management");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "Guest";
        Span userInfo = new Span("Welcome, " + username);
        userInfo.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        Header header = new Header(appName, userInfo);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller);
    }

    private boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return false;
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            if (grantedAuthority.getAuthority().equals("ROLE_" + role)) {
                return true;
            }
        }
        return false;
    }

    private SideNav createNavigation() {
        SideNav sideNav = new SideNav();

        sideNav.addItem(new SideNavItem("Dashboard", DashBoardView.class, VaadinIcon.DASHBOARD.create()));
SideNavItem sidenavitem = new SideNavItem("Products", ProductView.class, VaadinIcon.PACKAGE.create());
sidenavitem.setEnabled(true);
sideNav.addItem(sidenavitem);
        sideNav.addItem(new SideNavItem("Sales", SalesView.class, VaadinIcon.CART.create()));
        if (hasRole("ADMIN")) {
            sideNav.addItem(new SideNavItem("Reports", ReportsView.class, VaadinIcon.CHART.create()));
        }
        return sideNav;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        if (getContent() == null) {
            return "";
        }

        if (getContent().getClass().isAnnotationPresent(PageTitle.class)) {
            return getContent().getClass().getAnnotation(PageTitle.class).value();
        }

        if (getContent() instanceof HasDynamicTitle) {
            return ((HasDynamicTitle) getContent()).getPageTitle();
        }
        return "";
    }
}
