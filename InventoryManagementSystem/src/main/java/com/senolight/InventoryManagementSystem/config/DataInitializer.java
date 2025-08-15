package com.senolight.InventoryManagementSystem.config;

import com.senolight.InventoryManagementSystem.model.Product;
import com.senolight.InventoryManagementSystem.model.Role;
import com.senolight.InventoryManagementSystem.model.User;
import com.senolight.InventoryManagementSystem.repository.ProductRepository;
import com.senolight.InventoryManagementSystem.repository.RoleRepository;
import com.senolight.InventoryManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

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
        // Create roles if they don't exist
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ADMIN");
                    return roleRepository.save(role);
                });

        Role staffRole = roleRepository.findByName("STAFF")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("STAFF");
                    return roleRepository.save(role);
                });

        // Create admin user if doesn't exist
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
        }

        // Create staff user if doesn't exist
        if (userRepository.findByUsername("staff").isEmpty()) {
            User staff = new User();
            staff.setUsername("staff");
            staff.setPassword(passwordEncoder.encode("staff123"));
            staff.setRoles(Set.of(staffRole));
            userRepository.save(staff);
        }

        // Create sample products if none exist
        if (productRepository.count() == 0) {
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
