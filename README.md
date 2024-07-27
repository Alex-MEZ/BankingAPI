# BankingAPI

BankingAPI - это REST API для управления банковскими операциями. Проект создан с использованием Spring Boot, Hibernate, и PostgreSQL.

## Начало работы

Эти инструкции помогут вам запустить проект на вашем локальном компьютере для целей разработки и тестирования.

### Предварительные требования

Убедитесь, что у вас установлены следующие программные компоненты:

- Java 17
- Maven 3.9.8 или выше
- PostgreSQL 13 или выше

### Установка

1. Клонируйте репозиторий:

```bash
git clone https://github.com/Alex-MEZ/BankingAPI.git
cd BankingAPI


### Создайте базу данных PostgreSQL:

CREATE DATABASE bankingapi;

### Настройте файл application.properties в папке src/main/resources:

spring.datasource.url=jdbc:postgresql://localhost:5432/bankingapi
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

### Использование

API предоставляет следующие эндпоинты:

GET /api/accounts - Получение списка всех счетов
POST /api/accounts - Создание нового счета
GET /api/accounts/{id} - Получение информации о счете по ID
PUT /api/accounts/{id} - Обновление информации о счете по ID
DELETE /api/accounts/{id} - Удаление счета по ID