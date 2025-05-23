# Mobile

Используйте данный репозиторий для работы над задачей.


## Проверка на плагиат

Двойная система проверки включает: 

Собственное решение организаторов: мы используем алгоритмы для анализа вашего кода, проверяя, что все работы являются оригинальными.

Затем работы участников олимпиады будут проверяться на неправомерные заимствования через сервис Codechecker, продукт компании [«Антиплагиат»](https://antiplagiat.ru/).

Рекомендации по использованию стороннего кода: если вы решите использовать код, найденный в открытых источниках, очень важно указать источник этого кода. Это позволит легко проверить и подтвердить подлинность вашей работы, а также развеять любые возможные сомнения. 

Мы призываем всех участников придерживаться принципов академической честности и подходить к соревнованиям с открытым и честным настроем. Помните, что цель олимпиады — не только показать ваши навыки, но и развиваться как надежные и честные специалисты в будущем.


##  Обоснование принятых решений:

## Использование библиотек:

   # 1.  Обоснование выбора Jetpack Compose
## Проблема
В проекте требуется создать пользовательский интерфейс для мобильного приложения на платформе Android. 
Ранее для создания UI использовалась классическая XML-разметка с `View` и `ViewGroup`, но этот подход может быть трудоемким и трудным в поддержке на больших проектах.
С каждым обновлением Android добавляется новый функционал, и традиционная система UI в XML становится все более сложной для работы с новыми требованиями.

## Существующие решения

### 1. **XML + Views:**
- **Плюсы:**
  - Проверенный подход с широким сообществом.
  - Хорошо подходит для простых UI.
- **Минусы:**
  - Код становится сложным для поддержки при увеличении функционала.
  - Требует больше шаблонного кода для работы с динамическими UI элементами (например, RecyclerView, сложные списки и формы).
  - Интеграция с другими библиотеками может быть трудной и трудоемкой.

### 2. **Jetpack Compose:**
- **Плюсы:**
  - Более современный и декларативный подход, что позволяет описывать UI с помощью Kotlin-кода.
  - Уменьшает количество шаблонного кода (например, не нужно использовать XML разметку и `findViewById`).
  - Легче поддерживать и расширять, особенно для сложных UI.
  - Прекрасная интеграция с Kotlin, что позволяет использовать все преимущества этого языка, включая его функциональные возможности.
  - Сильная поддержка от Google и активное развитие.
  - Более высокая производительность для сложных и динамичных UI (например, меньше затрат на рендеринг).
- **Минусы:**
  - Более крутая кривая обучения, особенно для команд, привыкших к XML.
  - Все еще не такая зрелая как XML-разметка с Views, поэтому могут быть проблемы с производительностью или ограниченные возможности в некоторых случаях.
  - Некоторые устаревшие библиотеки или инструменты могут не поддерживать Compose прямо из коробки.

## Выбор и обоснование

Я выбрал **Jetpack Compose** по нескольким причинам:

1. **Производительность и оптимизация:**  
Jetpack Compose предоставляет высокую производительность, особенно для сложных пользовательских интерфейсов. 
Он использует "Recomposition", что позволяет обновлять только те части интерфейса, которые изменяются, что снижает накладные расходы на рендеринг и улучшает отзывчивость приложения.

2. **Декларативный подход:**  
Jetpack Compose позволяет декларативно описывать UI, что значительно упрощает код.
Вместо того чтобы манипулировать состоянием вручную (например, через `View.setVisibility` или `RecyclerView`), мы просто описываем, как UI должен выглядеть в данный момент, и Compose сам управляет его состоянием.

3. **Меньше шаблонного кода:**  
В Compose используется только Kotlin, и разметка не требует XML.
Это сокращает количество кода и улучшает его читаемость. Код становится более чистым и легко поддерживаемым, что важно при разработке крупных приложений.

4. **Интеграция с Kotlin:**  
Jetpack Compose тесно интегрирован с Kotlin и использует его возможности для повышения продуктивности разработки. Kotlin более современен и удобен по сравнению с Java, а также предлагает богатый набор библиотек и инструментов, что ускоряет разработку.

5. **Активное развитие и поддержка:**  
Compose активно поддерживается и развива­ется Google, что гарантирует долгосрочную поддержку и улучшения. 
Учитывая, что это новый подход для Android UI, ожидается, что в будущем он станет основным инструментом для создания интерфейсов на платформе Android.

## Потенциальные риски и открытые вопросы

- **Поддержка сторонних библиотек:** Хотя Compose поддерживает множество сторонних библиотек, может возникнуть ситуация, когда старые библиотеки или инструменты не поддерживают Compose или требуют дополнительных усилий для интеграции.
- **Постоянные изменения:** Так как Jetpack Compose это относительно новая технология, она постоянно меняется и некоторые функции могут изменяться или устаревать, что может привести к переписыванию старого кода.

    # 2. Обоснование выбора Dagger

## Проблема

В проекте требуется управление зависимостями между компонентами, что важно для масштабируемости и поддерживаемости кода.
Без внедрения зависимостей код может стать трудным для тестирования и расширения.

## Существующие решения

1. **Ручное внедрение зависимостей**  
   - Плюсы: Простой подход, контроль над зависимостями.  
   - Минусы: Трудности с масштабируемостью, сложности в тестировании, риск создания жесткой связи между классами.

2. **Другие фреймворки DI (например, Koin)**  
   - Плюсы: Хорошая интеграция с Kotlin, гибкость.  
   - Минусы: Меньшая производительность, ограниченная поддержка в больших проектах.

## Выбор и обоснование

Я выбрал **Dagger** по нескольким причинам:

1. **Производительность**: Использует compile-time DI, что ускоряет выполнение и минимизирует накладные расходы.
2. **Масштабируемость**: Идеален для крупных проектов, поддерживает сложные зависимости.
3. **Тестируемость**: Упрощает тестирование, так как зависимости внедряются через конструкторы.
4. **Гибкость и контроль**: Позволяет контролировать создание объектов и области их существования.
5. **Активная поддержка**: Dagger поддерживается Google и имеет большое сообщество.

## Потенциальные риски

- **Количество кода**: Может потребовать создания множества классов для управления зависимостями.

    # 3.  Обоснование выбора Retrofit и OkHttp

## Проблема

Необходимость эффективного взаимодействия с API с минимальными усилиями на настройку, обработку ошибок и сериализацию/десериализацию данных.

## Выбор и обоснование

- **Retrofit**: Упрощает работу с REST API, автоматизируя создание запросов и обработку данных. Интеграция с сериализаторами (Gson, Moshi) и поддержка асинхронных запросов через корутины.
- **OkHttp**: Обеспечивает эффективное управление HTTP-соединениями, повторные попытки запросов, кэширование и поддержку WebSocket. Является основой для Retrofit.

## Преимущества

1. **Простота использования**: Retrofit минимизирует boilerplate-код, а OkHttp управляет соединениями и их производительностью.
2. **Сериализация и десериализация**: Легко работает с JSON-данными через интеграцию с библиотеками сериализации.
3. **Производительность**: Обе библиотеки обеспечивают высокую производительность и надежность.
4. **Активная поддержка**: Регулярные обновления и хорошая документация.

    # 4. Обоснование выбора Room

## Проблема

Необходимость работы с локальной базой данных для хранения данных, таких как пользовательская информация, настройки и другие данные, которые не требуют постоянного подключения к серверу.

## Выбор и обоснование

- **Room**: Абстракция над SQLite, предоставляющая удобный API для работы с базой данных в Android. Предоставляет поддержку аннотированных классов, автоматическую генерацию SQL-запросов и проверку на этапе компиляции.

## Преимущества

1. **Упрощение работы с базой данных**: Room скрывает детали реализации работы с SQLite, делая код проще и удобнее.
2. **Интеграция с LiveData и ViewModel**: Легко интегрируется с компонентами архитектуры Android, такими как LiveData и ViewModel, для удобного обновления UI в ответ на изменения данных.
3. **Производительность**: Room автоматически оптимизирует запросы, улучшая производительность по сравнению с прямым использованием SQLite.
4. **Безопасность**: Проверка SQL-запросов на этапе компиляции, предотвращение возможных ошибок в запросах.
5. **Поддержка миграций**: Удобная система миграций базы данных, что упрощает управление изменениями схемы данных.


### Архитектура:
Мною была выбрана многомодульная архитектура, так как она:
- **во-первых**: Упрощает тестирование, поскольку модули могут быть протестированы изолированно.
- **во-вторых**: Легче масштабировать проект, так как новые модули могут добавляться без сильного влияния на другие. 
- **в-третьих**: Возможность повторного использования модулей в разных проектах.


#### Рефлизованные Фичи:

### 1. Главный экран
Реализован главный экран с возможностью просматривать ленту новостей и акции 10 кампаний, при нажатии на карточку новости пользователя перебросит на полную версию новости.
Также реализован PullToRefresh и шиммер.
![Главная](photo_2025-02-22_16-32-13.jpg)
Так выглядит экран с поной новостью, иконка в виде самолета означает поделиться в заметки, как только пользователь нажмет на нее она сразу перекинет его на экран создания заметки
![Полная версия новости](photo_2025-02-22_16-32-13.jpg)
Это реализация поиска по новостям и тикерам 
![Поиск](photo_2025-02-22_16-45-15.jpg)

### 2. Экран с финансами
Реализован экран с финансами, где пользователь может ставить цели и отслеживать свой прогресс

![Финансы](photo_2025-02-22_16-48-19.jpg)
![Добавление цели](photo_2025-02-22_16-48-14.jpg)
![Добавление операции](photo_2025-02-22_16-51-14.jpg)

### 3. заметки
реализован экран заметок где пользователь может создать заметку и сохранить ее, таже добавлена вкладка избранное, чтобы пользователь имел доступ к избраннаым заметкам

![Лента заметок](photo_2025-02-22_16-55-43.jpg)
![Экран добавления заметки](photo_2025-02-22_16-55-49.jpg)
![избранное](photo_2025-02-22_16-57-08.jpg)