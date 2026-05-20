# 📊 MOEX Bond Service

REST-сервис на Spring Boot для получения данных по облигациям с Московской биржи. Принимает список тикеров, запрашивает корпоративные и государственные облигации через MOEX API, парсит XML-ответ и возвращает результат в JSON.

## 🚀 Возможности

- **Единый endpoint для двух типов облигаций** — корпоративные (TQCB) и государственные (TQOB) в одном запросе
- **Фильтрация по тикерам** — принимает список интересующих тикеров, возвращает только нужные
- **Кэширование** — данные с MOEX кэшируются, чтобы не превысить лимиты запросов к бирже
- **Унифицированный ответ** — все облигации приводятся к единой модели `Stock`
- **Защита от XXE** — парсер XML настроен с ограничениями безопасности

## 🛠 Стек технологий

- Java 17
- Spring Boot
- Spring Cache
- Feign Client
- DOM XML Parser
- Maven

## 📋 API

### Получить облигации по тикерам

```http
POST /bonds/getBondsByTickers
```
## 🔄 Как это работает

1. Контроллер принимает POST-запрос со списком тикеров
2. `BondService` запрашивает оба источника через Feign-клиенты:
   - Корпоративные облигации: `iss.moex.com/.../boards/TQCB/securities.xml`
   - Государственные облигации: `iss.moex.com/.../boards/TQOB/securities.xml`
3. Каждый ответ парсится через `MoexXmlParserImpl` с извлечением атрибутов `SECID`, `PREVPRICE`, `SHORTNAME`
4. Результаты объединяются и фильтруются по запрошенным тикерам
5. Отфильтрованные облигации маппятся в модель `Stock` через Builder
6. Возвращается `StocksDto` со списком найденных облигаций

## 🗂 Структура проекта

| Компонент | Назначение |
|---|---|
| `BondMoexController` | REST-контроллер, принимает запросы |
| `BondService` | Бизнес-логика: вызов клиентов, парсинг, фильтрация |
| `CorporateBondsClient` | Feign-клиент для корпоративных облигаций |
| `GovBondsClient` | Feign-клиент для государственных облигаций |
| `MoexXmlParserImpl` | Парсер XML-ответа MOEX (DOM) |
| `BondParsingException` | Кастомное исключение для ошибок парсинга |
| `LimitRequestsException` | Кастомное исключение при пустом ответе MOEX |

## ⚙️ Кэширование

- `@Cacheable("corps")` — кэширует список корпоративных облигаций
- `@Cacheable("govs")` — кэширует список государственных облигаций

Кэш хранится в памяти приложения (ConcurrentHashMap). При повторном запросе тех же тикеров данные берутся из кэша, без обращения к MOEX.

## 🔒 Безопасность парсинга

Парсер настроен с защитой от XXE-атак:

```java
dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
```
## ⚙️ Конфигурация

В `application.yml` настраиваются URL-адреса MOEX:

```yaml
moex:
  bonds:
    corporate:
      url: "https://iss.moex.com/iss/engines/stock/markets/bonds/boards/TQCB/securities.xml?..."
    government:
      url: "https://iss.moex.com/iss/engines/stock/markets/bonds/boards/TQOB/securities.xml?..."
```
Уровень логирования Feign-запросов:

```yaml
logging:
  level:
    ru.dasha.MoexService.moexclient: debug
```
## 🚀 Запуск
```bash
mvn spring-boot:run
```
Сервис запустится на порту 8080 (настраивается в application.yml).
