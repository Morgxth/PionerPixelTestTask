# 💸 Payment Microservice

Spring Boot микросервис для управления пользователями, их балансами и денежными переводами. Поддерживает регистрацию, авторизацию по JWT, переводы между пользователями и кэширование баланса.

---

## 🚀 Технологии

- Java 17+
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Redis (кэширование)
- Docker / Docker Compose
- SLF4J + Logback (логирование)
- Maven

---

## 📦 Функциональность

### 🔐 Аутентификация

- Логин (выдача JWT токена)
- Защита всех маршрутов, кроме `/auth/**` и `/api/test/all`

### 👥 Работа с пользователями

- Получение списка всех пользователей
- Просмотр информации о себе (`/users/me`)
- Проверка текущего баланса

### 💰 Денежные переводы

- Перевод средств от одного пользователя другому
- Валидация: нельзя перевести самому себе, баланс не должен уйти в минус

### 🚀 Кэширование

- Баланс пользователя кэшируется в Redis
- При изменении баланса кэш инвалидируется

---

## 🧪 Примеры API

### Регистрация

POST /auth/register
```json
{
"username": "ivan",
"password": "password123"
}
```

### Логин

POST /auth/login
```json
{
"username": "ivan",
"password": "password123"
}
```

**Ответ:**
```json
{ "token": "eyJhbGciOiJIUzI1NiIs..." }
```
Перевод средств

POST /transfer
Authorization: Bearer <jwt>
```json
{
  "receiverId": 2,
  "amount": 100
}
```

Весь список запросов можно просмотреть в консоли Swagger'a по адресу 
🐳 Запуск через Docker
```bash
docker-compose up --build
```

Приложение будет доступно по адресу: http://localhost:8080
