package com.senolight.InventoryManagementSystem.constants;

public class Constants {
    // Application Info
    public static final String APP_NAME = "Seno Light Inventory Management";
    public static final String APP_VERSION = "1.0.0";
    public static final String COMPANY_NAME = "Seno Light Electricals";

    // UI Constants
    public static final String CURRENCY_SYMBOL = "₦";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm";

    // Colors
    public static final String PRIMARY_COLOR = "var(--lumo-primary-color)";
    public static final String SUCCESS_COLOR = "var(--lumo-success-color)";
    public static final String ERROR_COLOR = "var(--lumo-error-color)";
    public static final String WARNING_COLOR = "var(--lumo-contrast-color)";

    // Messages
    public static final String SUCCESS_SAVE = "Record saved successfully!";
    public static final String SUCCESS_DELETE = "Record deleted successfully!";
    public static final String SUCCESS_UPDATE = "Record updated successfully!";
    public static final String ERROR_SAVE = "Error saving record: ";
    public static final String ERROR_DELETE = "Error deleting record: ";
    public static final String ERROR_UPDATE = "Error updating record: ";
    public static final String ERROR_VALIDATION = "Please check your input and try again.";
    public static final String ERROR_INSUFFICIENT_STOCK = "Insufficient stock available.";

    // Validation
    public static final int MAX_NAME_LENGTH = 255;
    public static final int MAX_SKU_LENGTH = 50;
    public static final double MIN_PRICE = 0.01;
    public static final int MIN_QUANTITY = 0;

    private Constants() {
        // Utility class - prevent instantiation
    }
}
}
