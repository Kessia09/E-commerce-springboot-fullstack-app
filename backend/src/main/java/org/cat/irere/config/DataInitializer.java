package org.cat.irere.config;

import com.github.javafaker.Faker;
import org.cat.irere.model.Product;
import org.cat.irere.model.Quantity;
import org.cat.irere.repository.ProductRepository;
import org.cat.irere.repository.QuantityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Configuration
public class DataInitializer {

    @Value("${app.data.initialize:true}")
    private boolean shouldInitializeData;

    private final String[] PRODUCT_TYPES = {
            "Electronics", "Clothing", "Groceries", "Home Appliances",
            "Furniture", "Books", "Toys", "Beauty", "Sports", "Tools"
    };

    private final String[] IMAGE_TEMPLATES = {
            "https://source.unsplash.com/random/800x600/?product,electronics",
            "https://source.unsplash.com/random/800x600/?product,clothing",
            "https://source.unsplash.com/random/800x600/?product,food",
            "https://source.unsplash.com/random/800x600/?product,appliance",
            "https://source.unsplash.com/random/800x600/?product,furniture",
            "https://source.unsplash.com/random/800x600/?product,book",
            "https://source.unsplash.com/random/800x600/?product,toy",
            "https://source.unsplash.com/random/800x600/?product,beauty",
            "https://source.unsplash.com/random/800x600/?product,sport",
            "https://source.unsplash.com/random/800x600/?product,tool"
    };

    @Bean
    public CommandLineRunner initData(ProductRepository productRepository,
            QuantityRepository quantityRepository) {
        return args -> {
            if (!shouldInitializeData) {
                return;
            }

            if (productRepository.count() > 0) {
                System.out.println("Database already contains products. Skipping data initialization.");
                return;
            }

            Faker faker = new Faker(new Locale("en-US"));
            Random random = new Random();

            List<Product> products = new ArrayList<>();
            List<Quantity> quantities = new ArrayList<>();

            System.out.println("Generating dummy products...");

            for (int i = 0; i < 50; i++) {
                // Generate product details
                int typeIndex = random.nextInt(PRODUCT_TYPES.length);
                String productType = PRODUCT_TYPES[typeIndex];

                String code = "P" + String.format("%04d", i + 1);
                String name = faker.commerce().productName();
                BigDecimal price = new BigDecimal(faker.commerce().price(10, 2000));

                // Generate a random date within the last year
                LocalDate inDate = faker.date()
                        .past(365, TimeUnit.DAYS)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                String image = IMAGE_TEMPLATES[typeIndex];

                Product product = new Product(code, name, productType, price, inDate, image);
                products.add(product);

                // Generate quantity for the product (initial stock)
                int initialQuantity = 10 + random.nextInt(90); // Between 10 and 100
                Quantity quantity = new Quantity(code, initialQuantity, "IN", LocalDate.now());
                quantities.add(quantity);
            }

            // Save all products and quantities
            productRepository.saveAll(products);
            quantityRepository.saveAll(quantities);

            System.out.println("Data initialization complete. Generated " + products.size() + " products.");
        };
    }
}