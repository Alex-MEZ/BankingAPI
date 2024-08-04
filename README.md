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
```

2. Создайте базу данных PostgreSQL:

```sql
CREATE DATABASE bankingapi;
```

3. Настройте файл `application.properties` в папке `src/main/resources`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bankingapi
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

4. Скомпилируйте и запустите проект:

```bash
mvn clean install
mvn spring-boot:run
```

### Использование

API предоставляет следующие эндпоинты:

#### Управление счетами:

- **GET /api/accounts** - Получение списка всех счетов
- **POST /api/accounts** - Создание нового счета

#### Управление транзакциями:

- **POST /api/accounts/{id}/deposit** - Пополнение счета
- **POST /api/accounts/{id}/withdraw** - Снятие средств со счета
- **POST /api/accounts/transfer** - Перевод средств между счетами
- **GET /api/accounts/{id}/transactions** - Получение списка транзакций по счету

### Валидация

Для обеспечения безопасности и корректности данных в проекте реализованы следующие механизмы валидации:

- Проверка, что сумма транзакции больше нуля для всех операций (пополнение, снятие, перевод).
- Проверка, что на счете достаточно средств для снятия и перевода.
- Проверка корректности PIN-кода при снятии и переводе средств.

### Логика в сервисах

Логика работы с транзакциями и счетами разнесена по сервисам `AccountService` и `TransactionService`. Основные операции разделены на отдельные методы для улучшения читаемости и поддержки кода.

#### AccountService

- **createAccount(CreateAccountRequest request)** - Создание нового счета
- **getAllAccounts()** - Получение списка всех счетов
- **getAccountById(Long id)** - Получение информации о счете по ID

#### TransactionService

- **deposit(Long id, TransactionRequest request)** - Пополнение счета
- **withdraw(Long id, TransactionRequest request)** - Снятие средств со счета
- **transfer(TransferRequest request)** - Перевод средств между счетами
- **getTransactions(Long accountId)** - Получение списка транзакций по счету
