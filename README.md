# ShopEase — E-Commerce Management System

A Java full-stack portfolio project built with **React.js, Java 17, Spring Boot, Spring Security, JWT, Spring Data JPA and MySQL**.

## Features

### Customer
- Browse and search products
- Register and sign in
- BCrypt password hashing and JWT authentication
- Shopping cart and demo order placement
- Stock validation and stock reduction
- View personal order history

### Administrator
- Role-protected admin dashboard
- Dashboard statistics: products, customers, orders and revenue
- Create, read, update and delete products
- View all orders and update order status

## Tech stack
- Frontend: React 18, Vite, JavaScript, CSS
- Backend: Java 17, Spring Boot 3.4, Spring Security, Spring Data JPA, Maven
- Database: MySQL 8+
- Authentication: JWT (JJWT) and BCrypt

## Requirements
- Java 17+
- Maven 3.8+
- Node.js 18+ and npm
- MySQL 8+

## 1. Database setup
Run `database/schema.sql` in MySQL Workbench or the MySQL CLI. It creates `ecommerce_db` and the `users`, `products`, `customer_orders`, and `order_items` tables.

## 2. Configure backend
Set these environment variables before starting the backend:

```bash
export DB_URL='jdbc:mysql://localhost:3306/ecommerce_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC'
export DB_USERNAME='root'
export DB_PASSWORD='YOUR_MYSQL_PASSWORD'
export JWT_SECRET='replace-with-a-long-random-secret-at-least-32-characters'
export ADMIN_EMAIL='admin@shopease.local'
export ADMIN_PASSWORD='Choose-A-Strong-Admin-Password'
```

Never commit real passwords or production secrets.

## 3. Run backend
```bash
cd backend
mvn spring-boot:run
```
API base URL: `http://localhost:8080/api`

On first startup, sample products and an administrator account are seeded if they do not already exist. The administrator password comes from `ADMIN_PASSWORD`; the local development fallback is `ChangeMe123!`. Change it before sharing or deploying.

## 4. Run frontend
Open a second terminal:
```bash
cd frontend
npm install
npm run dev
```
Open the Vite URL, usually `http://localhost:5173`.

Optional: copy `frontend/.env.example` to `frontend/.env.local` and set `VITE_API_URL`.

## Demo administrator
- Email: `admin@shopease.local`
- Password: your `ADMIN_PASSWORD` value (development fallback: `ChangeMe123!`)

The admin account is created by the backend. Public registration always creates a `USER`; users cannot select their own role.

## Main API endpoints

| Method | Endpoint | Access | Purpose |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register customer |
| POST | `/api/auth/login` | Public | Login and receive JWT |
| GET | `/api/products` | Public | List/search products |
| GET | `/api/products/{id}` | Public | Product details |
| POST | `/api/orders` | Authenticated | Place order |
| GET | `/api/orders/my` | Authenticated | Customer order history |
| GET | `/api/admin/stats` | Admin | Dashboard statistics |
| GET/POST | `/api/admin/products` | Admin | List/create products |
| PUT/DELETE | `/api/admin/products/{id}` | Admin | Update/delete product |
| GET | `/api/admin/orders` | Admin | List all orders |
| PATCH | `/api/admin/orders/{id}/status` | Admin | Update order status |

Protected API calls use `Authorization: Bearer <JWT_TOKEN>`.

## Database relationships
- One user can have many orders.
- One order has many order items.
- Each order item references a product. The product reference can become null if a product is deleted, while the item name and price snapshot remain.

## Project structure
```text
ecommerce-management-system/
├── backend/
│   └── src/main/java/com/example/ecommerce/
│       ├── config/
│       ├── controller/
│       ├── dto/
│       ├── model/
│       ├── repository/
│       ├── security/
│       └── service/
├── database/schema.sql
├── frontend/src/
└── README.md
```

## Security and scope notes
- Passwords are stored as BCrypt hashes.
- JWT authentication is stateless; admin APIs require the ADMIN role.
- This is an educational portfolio project, not a production commerce platform.
- Demo checkout does not collect or process real payments.
- It does not yet include payment processing, email verification, password reset, refresh-token rotation, rate limiting, or production-grade stock reservation.
- For production schema migrations, use Flyway or Liquibase instead of relying on Hibernate `ddl-auto=update`.

## Suggested next improvements
- Add backend integration tests and frontend tests.
- Add pagination and more detailed validation errors.
- Add password reset and email verification.
- Add Docker Compose for MySQL, backend and frontend.
