package com.senolight.InventoryManagementSystem.views;

import com.senolight.InventoryManagementSystem.model.Product;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class ProductForm extends FormLayout {
    TextField name = new TextField("Product Name");
    TextField sku = new TextField("SKU");
    IntegerField quantity = new IntegerField("Quantity");
    NumberField price = new NumberField("Price");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Close");

    Binder<Product> binder = new BeanValidationBinder<>(Product.class);

    public ProductForm() {
        addClassName("product-form");

        quantity.setMin(0);
        quantity.setStepButtonsVisible(true);

        price.setMin(0.01);
        price.setStep(0.01);
        price.setPrefixComponent(new Span("₦"));

        binder.forField(name)
                .asRequired("Name is required")
                .bind(Product::getName, Product::setName);

        binder.forField(sku)
                .asRequired("SKU is required")
                .bind(Product::getSku, Product::setSku);

        binder.forField(quantity)
                .withNullRepresentation(0)
                .bind(Product::getQuantity, Product::setQuantity);

        binder.forField(price)
                .withNullRepresentation(0.0d)
                .bind(Product::getPrice, Product::setPrice);

        add(name, sku, quantity, price, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            if (binder.getBean() == null) {
                binder.setBean(new Product());
            }
            binder.writeBean(binder.getBean());
            fireEvent(new SaveEvent(this, binder.getBean()));
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public void setProduct(Product product) {
        binder.setBean(product);
    }

    //Events
    public static abstract class ProductFormEvent extends ComponentEvent<ProductForm> {
        private final Product product;

        protected ProductFormEvent(ProductForm source, Product product) {
            super(source, false);
            this.product = product;
        }

        public Product getProduct() {
            return product;
        }
    }

    public static class SaveEvent extends ProductFormEvent {
        SaveEvent(ProductForm source, Product product) {
            super(source, product);
        }
    }

    public static class DeleteEvent extends ProductFormEvent {
        DeleteEvent(ProductForm source, Product product) {
            super(source, product);
        }
    }

    public static class CloseEvent extends ProductFormEvent {
        CloseEvent(ProductForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}