# E-Commerce Management System

A beginner-friendly full-stack portfolio project built with **React**, **Java Spring Boot**, and **H2 SQL database**.

## Features
- Browse products loaded from a REST API
- Search products by name
- Add/remove products in a shopping cart
- Update cart quantities and see the total
- Place a demo order (no real payment is collected)
- View a confirmation with the generated order ID
- H2 database with sample products seeded on startup

## Tech stack
- Frontend: React 18, Vite, JavaScript, CSS
- Backend: Java 17, Spring Boot 3.4, Spring Data JPA, Maven
- Database: H2 (file-based local database)

## Requirements
- Java 17+
- Maven 3.8+
- Node.js 18+ and npm

## Run the backend
```bash
cd backend
mvn spring-boot:run
```
Backend API: `http://localhost:8080/api/products`  
H2 console: `http://localhost:8080/h2-console`  
JDBC URL: `jdbc:h2:file:./data/ecommerce_db`  
Username: `sa`  
Password: *(leave blank)*

## Run the frontend
Open a second terminal:
```bash
cd frontend
npm install
npm run dev
```
Open the local URL printed by Vite (usually `http://localhost:5173`).

## Main API endpoints
| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/products` | List all products |
| GET | `/api/products?search=phone` | Search products |
| GET | `/api/products/{id}` | Get one product |
| POST | `/api/orders` | Place a demo order |

Example order request:
```json
{
  "customerName": "Abhishek Kumar",
  "customerEmail": "abhishek@example.com",
  "items": [
    { "productId": 1, "quantity": 2 }
  ]
}
```

## Project structure
```text
ecommerce-management-system/
├── backend/
│   ├── pom.xml
│   └── src/main/java/com/example/ecommerce/
│       ├── EcommerceApplication.java
│       ├── config/CorsConfig.java
│       ├── controller/
│       ├── dto/
│       ├── model/
│       ├── repository/
│       └── service/
└── frontend/
    ├── package.json
    ├── index.html
    └── src/
        ├── App.jsx
        ├── main.jsx
        ├── api.js
        └── style.css
```

## Important notes
- This is a learning/demo project. It does not include authentication, stock reservation, shipping, or a real payment gateway.
- Do not describe features on your resume as completed until you have run, tested, and understood them.
- A good next improvement is Spring Security + JWT authentication, followed by an admin product-management screen and automated tests.
