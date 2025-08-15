package com.senolight.InventoryManagementSystem.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.text.NumberFormat;
import java.util.Locale;

public class ViewUtils {
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("en", "NG"));

    public static String formatCurrency(double amount) {
        return "₦" + String.format("%,.2f", amount);
    }

    public static Component createMetricCard(String title, String value, String description, VaadinIcon icon, String color) {
        Icon cardIcon = icon.create();
        cardIcon.setColor(color);
        cardIcon.setSize("2em");

        Span titleSpan = new Span(title);
        titleSpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY, LumoUtility.FontWeight.MEDIUM);

        Span valueSpan = new Span(value);
        valueSpan.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.BOLD);

        Span descSpan = new Span(description);
        descSpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.TERTIARY);

        VerticalLayout content = new VerticalLayout(titleSpan, valueSpan, descSpan);
        content.setPadding(false);
        content.setSpacing(false);

        HorizontalLayout card = new HorizontalLayout(cardIcon, content);
        card.setAlignItems(FlexComponent.Alignment.CENTER);
        card.addClassNames("metric-card");
        card.setPadding(true);

        return card;
    }

    public static Button createPrimaryButton(String text, VaadinIcon icon) {
        Button button = new Button(text, icon.create());
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return button;
    }

    public static Button createSecondaryButton(String text, VaadinIcon icon) {
        Button button = new Button(text, icon.create());
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return button;
    }

    public static Button createSuccessButton(String text, VaadinIcon icon) {
        Button button = new Button(text, icon.create());
        button.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        return button;
    }

    public static Button createErrorButton(String text, VaadinIcon icon) {
        Button button = new Button(text, icon.create());
        button.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        return button;
    }
}
