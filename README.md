# Binary Supermarket Online Shopping System

This is an online shopping management system for Binary Supermarket, located in Kimironko sector in Gasabo district. The system enables customers to order and purchase products online, in addition to the existing physical store purchases.

## Features

### Product Management
- Register product details (code, name, product type, price, in-date, image)
- Track product quantities with detailed operations log (IN/OUT)

### Customer Management
- Register customers with name, email (as username), phone, and password
- Secure authentication system

### Shopping Cart Management
- Add products to a shopping cart
- Update quantities in the cart
- Remove items from the cart
- View cart contents

### Purchase System
- Buy products directly
- Checkout items from the shopping cart
- Automatic calculation of total purchase amount
- History of all customer purchases

## Technical Implementation

### Backend
- Java Spring Boot application
- RESTful API endpoints for all operations
- Spring Security for authentication
- Spring Data JPA for database operations
- PostgreSQL database with triggers for price calculation

### Database Design
The system includes the following main entities:
- Product (code, name, product type, price, in date, image)
- Quantity (id, product code, quantity, operation, date)
- Customer (id, firstname, phone, email, password)
- CartItem (id, customer id, product code, quantity)
- Purchase (id, product code, quantity, total, date)

## Getting Started

### Prerequisites
- Java 17 or higher
- PostgreSQL 12 or higher
- Maven

### Database Setup
1. Create a PostgreSQL database named `binary_supermarket`
2. Configure database credentials in `application.properties` if necessary

### Running the Application
1. Clone the repository
2. Navigate to the project directory
3. Run `mvn clean install` to build the project
4. Run `mvn spring-boot:run` to start the application
5. The application will be accessible at `http://localhost:8080`

### API Endpoints

#### Authentication
- POST `/api/auth/register` - Register a new customer
- POST `/api/auth/login` - Login with email and password
- GET `/api/customer/me` - Get current logged in user profile

#### Products
- GET `/api/products/all` - Get all products
- GET `/api/products/{code}` - Get a specific product
- POST `/api/products` - Create a new product
- PUT `/api/products/{code}` - Update a product
- DELETE `/api/products/{code}` - Delete a product

#### Quantities
- GET `/api/quantities/product/{productCode}` - Get quantities for a product
- POST `/api/quantities/add` - Add quantity to a product
- POST `/api/quantities/remove` - Remove quantity from a product
- GET `/api/quantities/available/{productCode}` - Get available quantity for a product

#### Cart
- GET `/api/cart/{customerId}` - Get cart items for a customer
- POST `/api/cart/{customerId}/add` - Add item to cart
- PUT `/api/cart/{customerId}/update/{cartItemId}` - Update cart item quantity
- DELETE `/api/cart/{customerId}/remove/{cartItemId}` - Remove item from cart
- DELETE `/api/cart/{customerId}/clear` - Clear cart

#### Purchases
- GET `/api/purchases/{customerId}` - Get purchase history for a customer
- POST `/api/purchases/{customerId}/buy` - Buy a product directly
- POST `/api/purchases/{customerId}/checkout` - Checkout all items in cart 
