# System C Manufacturing Demo

Spring Boot MVC + Maven + Thymeleaf + Swagger/OpenAPI + Log4j2 demo for the ISM integration scenario.

## Demo position

This application represents **System C - Manufacturing System**.

The latest integration architecture is:

```text
System A -> ISM -> System B     = API-based integration
System B <-> ISM 2              = DB-based integration, no System B API exposed for ISM 2
ISM 2 <-> System C              = API-based integration
```

System C exposes REST APIs for ISM 2 to create and check manufacturing work orders. It also has a simple Thymeleaf dashboard for maintaining:

- Raw materials / components
- Finished products
- Product BOM items
- Work orders received from ISM 2

This demo does **not** maintain stock count/inventory quantity in System C. It only maintains master data and BOM material requirements.

## Tech stack

- Java 17
- Spring Boot 3.3.5
- Maven
- Spring MVC
- Thymeleaf
- Spring Data JPA
- MySQL on port `3307`
- Optional H2 profile for fallback demo testing
- Swagger/OpenAPI using springdoc
- Log4j2

## Database setup - MySQL port 3307

The default configuration uses MySQL:

```text
Host: localhost
Port: 3307
Database: system_c_manufacturing_demo
Username: root
Password: root
```

The application connection is configured in:

```text
src/main/resources/application.yml
```

Default JDBC URL:

```text
jdbc:mysql://localhost:3307/system_c_manufacturing_demo?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kuala_Lumpur
```

### Option 1: Start MySQL using Docker

A `docker-compose.yml` file is included.

```bash
docker compose up -d
```

This starts MySQL container port mapping:

```text
Host port 3307 -> Container port 3306
```

### Option 2: Use existing local MySQL

Create the database manually:

```sql
CREATE DATABASE IF NOT EXISTS system_c_manufacturing_demo
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

Then update `src/main/resources/application.yml` if your username/password is different.

## Run

Make sure MySQL is running first, then run:

```bash
mvn clean spring-boot:run
```

Or package and run:

```bash
mvn clean package
java -jar target/system-c-manufacturing-demo-1.0.0.jar
```

## Optional fallback: run with H2 instead of MySQL

H2 is still included only as an optional fallback profile.

```bash
mvn clean spring-boot:run -Dspring-boot.run.profiles=h2
```

Or:

```bash
java -jar target/system-c-manufacturing-demo-1.0.0.jar --spring.profiles.active=h2
```

H2 console settings:

```text
URL: http://localhost:8083/h2-console
JDBC URL: jdbc:h2:file:./data/systemc-demo-db
User: sa
Password: blank
```

## URLs

| Feature | URL |
|---|---|
| Dashboard | http://localhost:8083/dashboard |
| Materials | http://localhost:8083/materials |
| Products | http://localhost:8083/products |
| Work Orders | http://localhost:8083/work-orders |
| Swagger UI | http://localhost:8083/swagger-ui.html |
| OpenAPI JSON | http://localhost:8083/v3/api-docs |

## Main API for ISM 2

### Create Work Order

`POST /api/integration/work-orders`

Example request:

```json
{
  "goodsRequestNo": "GR-0001",
  "orderNo": "ORD-0001",
  "productCode": "PRD-001",
  "quantity": 5,
  "externalReferenceNo": "ISM2-TXN-0001"
}
```

What happens:

1. System C validates the product code.
2. System C creates a work order.
3. System C reads BOM items for the product.
4. System C calculates required raw materials.
5. Work order status becomes `BOM_AVAILABLE` if BOM exists.

### Retrieve Work Order Status

`GET /api/integration/work-orders/{workOrderNo}/status`

### Update Work Order Status

`PUT /api/integration/work-orders/{workOrderNo}/status`

Example request:

```json
{
  "status": "COMPLETED",
  "remarks": "Manufacturing completed successfully"
}
```

## Work Order / BOM relationship

```text
Work Order = what product needs to be manufactured
Product = finished product master
BOM Item = material requirement per unit of product
Raw Material = component/material master
Work Order Material = calculated required material quantity for the work order
```

Formula:

```text
required material quantity = work order quantity x BOM quantity per unit
```

Example:

```text
Work order quantity: 5
Product: PRD-001
BOM:
- MAT-001 x 2 per unit
- MAT-002 x 1 per unit

Calculated requirement:
- MAT-001 = 10
- MAT-002 = 5
```

## Important DB tables

| Table | Purpose |
|---|---|
| products | Finished product master |
| raw_materials | Raw material/component master |
| bom_items | Materials needed per product unit |
| work_orders | Manufacturing work order received from ISM 2 |
| work_order_materials | Calculated material requirements for each work order |
| integration_logs | API request/response tracking for demo visibility |

Hibernate/JPA creates these tables automatically in MySQL when the application starts because `spring.jpa.hibernate.ddl-auto=update` is enabled for this demo.

## Swagger API test flow

1. Open `http://localhost:8083/swagger-ui.html`
2. Go to **ISM 2 Integration API**
3. Run `POST /api/integration/work-orders`
4. Use product code `PRD-001`
5. Open dashboard/work orders page
6. View calculated required materials
7. Mark work order as completed
8. Run `GET /api/integration/work-orders/{workOrderNo}/status`

## Logging

Logs are written to:

```text
logs/system-c-demo.log
```

Rolling log files are kept for up to 30 rollover files.
