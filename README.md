# Ice Cream Shop - Kafka Spring Boot Application

## Descriere
Aplicație Spring Boot pentru gestionarea comenzilor de înghețată folosind Apache Kafka pentru messaging și PostgreSQL pentru persistența datelor.

## Tehnologii utilizate
- **Spring Boot 3.5.4** - Framework principal
- **Apache Kafka** - Message broker pentru evenimente asincrone
- **PostgreSQL** - Baza de date relațională
- **Spring Data JPA** - Persistence layer
- **Docker Compose** - Container orchestration
- **Maven** - Build tool

## Structura proiectului
```
src/main/java/com/example/icecreamshop/
├── dto/                    # Data Transfer Objects
│   ├── OrderDto.java
│   └── OrderItemDto.java
├── entity/                 # JPA Entities
│   ├── Order.java
│   ├── OrderItem.java
│   └── OrderStatus.java
├── repository/             # Data Access Layer
│   └── OrderRepository.java
├── service/                # Business Logic
│   ├── OrderService.java
│   └── OrderProcessingService.java
├── controller/             # REST Controllers
│   └── OrderController.java
├── consumer/               # Kafka Consumers
│   └── OrderEventConsumer.java
└── config/                 # Configuration
    └── KafkaProducerConfig.java
```

## Setup și Instalare

### 1. Clonează repository-ul
```bash
git clone <repository-url>
cd ice-cream-shop
```

### 2. Pornește serviciile Docker
```bash
docker-compose up -d
```

Serviciile care vor porni:
- **Kafka** - port 9092
- **Zookeeper** - port 2181  
- **PostgreSQL** - port 5432
- **Kafka UI** - port 8080

### 3. Verifică serviciile
```bash
docker-compose ps
```

### 4. Pornește aplicația Spring Boot
```bash
./mvnw spring-boot:run
```

Aplicația va porni pe portul **8081**.

## API Endpoints

### Health Check
```bash
GET http://localhost:8081/api/orders/health
```

### Creează o comandă nouă
```bash
POST http://localhost:8081/api/orders
Content-Type: application/json

{
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "items": [
    {
      "flavor": "Vanilla",
      "size": "Large",
      "quantity": 2,
      "pricePerUnit": 5.99
    },
    {
      "flavor": "Chocolate",
      "size": "Medium",
      "quantity": 1,
      "pricePerUnit": 4.99
    }
  ],
  "totalAmount": 16.97
}
```

### Obține toate comenzile
```bash
GET http://localhost:8081/api/orders
```

### Obține o comandă după ID
```bash
GET http://localhost:8081/api/orders/{orderId}
```

### Actualizează statusul unei comenzi
```bash
PUT http://localhost:8081/api/orders/{orderId}/status?status=PROCESSING
```

Statusuri disponibile: `PENDING`, `PROCESSING`, `COMPLETED`, `CANCELLED`

## Testare cu cURL

```bash
# Creează o comandă
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "John Doe",
    "customerEmail": "john@example.com",
    "items": [
      {
        "flavor": "Vanilla",
        "size": "Large",
        "quantity": 2,
        "pricePerUnit": 5.99
      }
    ],
    "totalAmount": 11.98
  }'

# Obține toate comenzile
curl http://localhost:8081/api/orders

# Health check
curl http://localhost:8081/api/orders/health
```

## Monitorizare și Debugging

### Kafka UI
Accesează [http://localhost:8080](http://localhost:8080) pentru a vedea:
- Topics Kafka
- Messages și offseturi
- Consumer groups

### PostgreSQL
Conectează-te la baza de date:
```bash
docker exec -it postgres psql -U user -d icecreamshop

# Verifică tabelele
\dt
SELECT * FROM orders;
SELECT * FROM order_items;
```

### Logs aplicație
Aplicația folosește logging la nivel DEBUG pentru pachetul `com.example.icecreamshop`. Logs-urile vor afișa:
- Comenzi create și procesate
- Evenimente Kafka primite
- Operații pe baza de date

## Arhitectura aplicației

### Flow principal:
1. **Client** → POST `/api/orders` → **OrderController**
2. **OrderController** → **OrderService** → salvează în **PostgreSQL**
3. **OrderService** → publică mesaj în **Kafka** topic `order-events`
4. **OrderEventConsumer** → consumă mesajul din Kafka
5. **OrderProcessingService** → procesează comanda (business logic)

### Event-driven architecture:
- Fiecare comandă creată/actualizată generează un eveniment Kafka
- Consumer-ul procesează evenimentele asincron
- Permite scalabilitate și decuplare

## Dezvoltare

### Rulare în modul dezvoltare
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Teste
```bash
./mvnw test
```

### Package aplicația
```bash
./mvnw clean package
java -jar target/ice-cream-shop-0.0.1-SNAPSHOT.jar
```

## Troubleshooting

### Docker nu pornește
- Verifică că Docker Desktop rulează
- Verifică că porturile nu sunt ocupate

### Kafka connection failed  
- Verifică că toate containerele rulează: `docker-compose ps`
- Restart serviciile: `docker-compose restart`

### Aplicația nu se conectează la PostgreSQL
- Verifică că PostgreSQL container rulează
- Verifică configurația din `application.yml`

### Reset complet
```bash
docker-compose down -v
docker-compose up -d
```

## Extensii viitoare
- Order status updates prin email notifications
- Inventory management system
- Customer management
- Metrics și monitoring cu Prometheus/Grafana
- API documentation cu Swagger/OpenAPI
- Integration tests cu Testcontainers 