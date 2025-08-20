# Inventory Management System

## Setup Guide

## 📋 Project Overview

The Seno Light Inventory Management System is a full-stack web application built with:
- **Backend**: Spring Boot 3.5.3, Spring Security, Spring Data JPA
- **Frontend**: Vaadin 24.4 (Java-based UI framework)
- **Database**: MySQL (Production), H2 (Testing)
- **Authentication**: Role-based security (ADMIN/STAFF roles)

## 🚀 Features

### ✅ Backend Features
- **Product Management**: CRUD operations for products
- **Sales Management**: Record sales, track inventory changes
- **Statistics**: Daily, weekly, monthly revenue and quantity reports
- **Invoice Generation**: PDF invoices using iText7
- **RESTful APIs**: Complete API endpoints for all operations
- **Security**: JWT-based authentication with role-based access
- **Comprehensive Testing**: Unit tests, integration tests, controller tests

### ✅ Frontend Features
- **Dashboard**: Overview with key metrics and charts
- **Product Management**: Add, edit, delete products with real-time validation
- **Sales Recording**: Easy-to-use sales interface with stock validation
- **Reports & Analytics**: Visual charts and statistics (Admin only)
- **Responsive Design**: Works on desktop, tablet, and mobile
- **Modern UI**: Clean, professional interface with Vaadin components

## 📁 Project Structure

```
InventoryManagementSystem/
├── src/main/java/com/senolight/InventoryManagementSystem/
│   ├── controller/          # REST API controllers
│   ├── model/              # JPA entities
│   ├── repository/         # Data access layer
│   ├── service/            # Business logic
│   ├── security/           # Security configuration
│   ├── views/              # Vaadin UI views
│   ├── config/             # Configuration classes
│   ├── utils/              # Utility classes
│   └── constants/          # Application constants
├── src/main/resources/
│   ├── application.properties
│   └── META-INF/resources/themes/
├── src/test/java/          # Test files
└── frontend/themes/        # Vaadin themes
```

## ⚙️ Setup Instructions

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- MySQL 8.0+ (for production)
- IDE (IntelliJ IDEA recommended)

### Step 1: Clone and Setup
```bash
git clone <your-repo-url>
cd InventoryManagementSystem
```

### Step 2: Database Configuration
1. Create MySQL database:
```sql
CREATE DATABASE inventory_management;
CREATE USER 'inventory_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON inventory_management.* TO 'inventory_user'@'localhost';
FLUSH PRIVILEGES;
```

2. Update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_management
spring.datasource.username=inventory_user
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### Step 3: Install Dependencies
```bash
mvn clean install
```

### Step 4: Run Tests
```bash
mvn test
```

### Step 5: Start Application
```bash
mvn spring-boot:run
```

The application will be available at: `http://localhost:8080`

## 👥 Default Users

The application automatically creates default users on startup:

| Username | Password | Role  | Access |
|----------|----------|-------|---------|
| admin    | admin123 | ADMIN | Full access to all features |
| staff    | staff123 | STAFF | Limited access (no reports) |

## 🎯 Usage Guide

### 1. Login
- Navigate to `http://localhost:8080`
- Use default credentials or create new users

### 2. Dashboard
- View key metrics (revenue, quantities sold)
- Interactive charts showing performance trends
- Quick navigation to all modules

### 3. Product Management
- **Add Products**: Click "Add Product" button
- **Edit Products**: Click on any product in the grid
- **Delete Products**: Use delete button in product form
- **Search**: Use the filter box to find products

### 4. Sales Management
- **Record Sale**: Click "Record Sale" button
- **View Sales**: Browse sales history with date filters
- **Download Invoices**: Click download button for any sale
- **Filter Sales**: Use date range picker to filter results

### 5. Reports (Admin Only)
- **Revenue Analytics**: Daily, weekly, monthly breakdowns
- **Performance Charts**: Visual representation of sales data
- **Trend Analysis**: Compare performance across time periods

## 🔧 Configuration Options

### Application Properties
```properties
# Server Configuration
server.port=8080

# Vaadin Configuration
vaadin.productionMode=false
vaadin.whitelisted-packages=com.senolight.InventoryManagementSystem

# Database Configuration
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update

# Security Configuration
spring.security.user.name=admin
spring.security.user.password=admin123
```

### Custom Themes
The application uses custom Vaadin themes located in:
- `frontend/themes/inventorymanagement/`
- Modify `styles.css` to customize appearance
- Update `theme.json` to change Lumo imports

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Error**
    - Check MySQL is running
    - Verify connection details in `application.properties`
    - Ensure database and user exist

2. **Vaadin Build Issues**
    - Run `mvn clean install` to rebuild frontend
    - Check Node.js is installed (Vaadin will install if needed)

3. **Test Failures**
    - Ensure H2 database dependency is included
    - Check test configuration in `application-test.properties`

4. **Access Denied**
    - Check user roles are properly assigned
    - Verify security configuration

## 🚀 Production Deployment

### 1. Build Production Bundle
```bash
mvn clean package -Pproduction
```

### 2. Environment Variables
```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://prod-server:3306/inventory_management
export SPRING_DATASOURCE_USERNAME=prod_user
export SPRING_DATASOURCE_PASSWORD=secure_password
export VAADIN_PRODUCTION_MODE=true
```

### 3. Run Production JAR
```bash
java -jar target/InventoryManagementSystem-0.0.1-SNAPSHOT.jar
```

## 📊 API Documentation

### Product Endpoints
- `GET /api/products/all` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products/add` - Add new product
- `PUT /api/products/sell/{id}` - Update product quantity
- `DELETE /api/products/{id}` - Delete product

### Sales Endpoints
- `POST /api/sales/record` - Record new sale
- `GET /api/sales/range` - Get sales by date range
- `GET /api/sales/invoice/{saleId}` - Download invoice PDF

### Statistics Endpoints (Admin Only)
- `GET /api/stats/revenue/today` - Today's revenue
- `GET /api/stats/revenue/week` - Weekly revenue
- `GET /api/stats/revenue/month` - Monthly revenue
- `GET /api/stats/quantity/today` - Today's quantity sold

## 📈 Future Enhancements

- **Inventory Alerts**: Low stock notifications
- **Barcode Scanning**: Product identification via barcode
- **Multi-location Support**: Manage inventory across locations
- **Advanced Reports**: Custom report builder
- **Mobile App**: Native mobile application
- **Supplier Management**: Track suppliers and purchase orders

## 📞 Support

For support and questions:
- Check the troubleshooting section
- Review application logs: `logs/spring-boot-logger.log`
- Create GitHub issues for bugs or feature requests

---

**Built by Prince Obiuto**
