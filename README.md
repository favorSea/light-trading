# Crypto Trading (Spring Boot + H2)

Run:
- mvn clean package
- java -jar target/crypto-trading-1.0.0.jar
H2 console: http://localhost:8080/h2-console (jdbc:h2:mem:crypto)
APIs:
- GET /api/price/latest?symbol=ETHUSDT
- POST /api/trade  { "symbol":"ETHUSDT","side":"BUY","quantity":0.1 }
- GET /api/wallet
- GET /api/history
