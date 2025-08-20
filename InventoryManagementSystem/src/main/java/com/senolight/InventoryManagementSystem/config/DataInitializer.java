package com.senolight.InventoryManagementSystem.config;

import com.senolight.InventoryManagementSystem.model.Product;
import com.senolight.InventoryManagementSystem.model.Role;
import com.senolight.InventoryManagementSystem.model.User;
import com.senolight.InventoryManagementSystem.repository.ProductRepository;
import com.senolight.InventoryManagementSystem.repository.RoleRepository;
import com.senolight.InventoryManagementSystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting data initialization...");

        try {
            // Create roles if they don't exist
            logger.info("Creating roles...");
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> {
                        logger.info("Creating ADMIN role");
                        Role role = new Role();
                        role.setName("ADMIN");
                        return roleRepository.save(role);
                    });

            Role staffRole = roleRepository.findByName("STAFF")
                    .orElseGet(() -> {
                        logger.info("Creating STAFF role");
                        Role role = new Role();
                        role.setName("STAFF");
                        return roleRepository.save(role);
                    });

            // Create admin user if doesn't exist
            if (userRepository.findByUsername("admin").isEmpty()) {
                logger.info("Creating admin user...");
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of(adminRole));
                userRepository.save(admin);
                logger.info("Admin user created successfully!");
            } else {
                logger.info("Admin user already exists");
            }

            // Create staff user if doesn't exist
            if (userRepository.findByUsername("staff").isEmpty()) {
                logger.info("Creating staff user...");
                User staff = new User();
                staff.setUsername("staff");
                staff.setPassword(passwordEncoder.encode("staff123"));
                staff.setRoles(Set.of(staffRole));
                userRepository.save(staff);
                logger.info("Staff user created successfully!");
            } else {
                logger.info("Staff user already exists");
            }

            // Verify users were created
            long userCount = userRepository.count();
            logger.info("Total users in database: {}", userCount);

            // Create sample products if none exist
            if (productRepository.count() == 0) {
                logger.info("Creating sample products...");
                Product[] sampleProducts = {
                        createProduct("LED Bulb 9W", "LED-9W-001", 100, 1500.00),
                        createProduct("LED Bulb 12W", "LED-12W-001", 80, 2000.00),
                        createProduct("LED Strip Light", "LED-STRIP-001", 50, 3500.00),
                        createProduct("Solar Panel 100W", "SOLAR-100W-001", 25, 35000.00),
                        createProduct("Battery 12V 100Ah", "BAT-12V-100AH", 15, 45000.00),
                        createProduct("Inverter 1000W", "INV-1000W-001", 20, 25000.00),
                        createProduct("LED Floodlight 50W", "LED-FLOOD-50W", 30, 8000.00),
                        createProduct("Solar Street Light", "SOLAR-STREET-001", 12, 75000.00)
                };

                for (Product product : sampleProducts) {
                    productRepository.save(product);
                }
                logger.info("Sample products created successfully!");
            }

            logger.info("Data initialization completed successfully!");

        } catch (Exception e) {
            logger.error("Error during data initialization", e);
            throw e;
        }
    }

    private Product createProduct(String name, String sku, int quantity, double price) {
        Product product = new Product();
        product.setName(name);
        product.setSku(sku);
        product.setQuantity(quantity);
        product.setPrice(price);
        return product;
    }
}