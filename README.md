## Котики REST API
#### Сервис по учету котиков и их владельцев.

Существующая информация о котиках:
- Имя
- Дата рождения
- Порода
- Цвет
- Хозяин
- Список котиков, с которыми дружит этот котик

Существующая информация о хозяевах:
- Имя
- Фамилия
- Дата рождения
- Список котиков

Сервис реализовывает архитектуру **Controller-Service-Dao**.
Приложение написано с использованием **SpringBoot**

Вся информация хранится в БД **PostgreSQL**. Для связи с БД используется **Hibernate**.

Проект собирается с помощью **Gradle**.

При модульном тестировании используется [JUnit](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api) и [Mockito](https://mvnrepository.com/artifact/org.mockito/mockito-core).
Для интеграционных тестов используется *MockMvc* и in-memory база данных *H2*


