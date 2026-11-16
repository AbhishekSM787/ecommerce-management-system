# Database setup
Run `schema.sql` in MySQL 8+ to create `ecommerce_db` and its four related tables: `users`, `products`, `customer_orders`, and `order_items`.

The Spring Boot application uses Hibernate `ddl-auto=update` for local development. The SQL file documents the intended schema and constraints. Do not use `ddl-auto=update` as a production migration strategy; use Flyway or Liquibase for managed schema migrations.
