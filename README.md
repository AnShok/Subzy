# <img src="https://github.com/AnShok/Subzy/raw/main/app/src/main/res/drawable/app_logo_transparent.png" alt="Subzy Logo" width="50"/>  Subzy

![Kotlin](https://img.shields.io/badge/Kotlin-blue) 
![MVVM](https://img.shields.io/badge/Architecture-MVVM-brightgreen) 
![Gradle](https://img.shields.io/badge/Gradle-7.2.0-blue) 
![Min API Level](https://img.shields.io/badge/Min%20API-21-brightgreen) 
![Target API Level](https://img.shields.io/badge/Target%20API-31-blue)

## Описание

**Subzy** — это приложение для управления подписками. Оно позволяет пользователям добавлять, изменять и удалять подписки, а также отслеживать предстоящие платежи и получать уведомления о сроках их оплаты.

## Основной функционал

- 📅 **Отслеживание подписок**: Позволяет добавлять информацию о различных подписках, включая название, периодичность и сумму.
- 🔔 **Уведомления**: Настраиваемые уведомления о предстоящих платежах.
- 📊 **История платежей**: Ведение журнала платежей с возможностью просмотра прошлых платежей.

## Технологический стек

- 🏛 **Архитектура**: MVVM
- 🗄 **База данных**: Room (SQLite)
- 🌐 **Работа с сетью**: Retrofit (если применимо)
- 🧭 **Навигация**: Jetpack Navigation
- 🔗 **Инъекция зависимостей**: Koin
- 📅 **Уведомления**: WorkManager для планирования уведомлений

## Установка и запуск

1. Клонируйте репозиторий:
    ```bash
    git clone https://github.com/AnShok/Subzy.git
    ```
2. Откройте проект в Android Studio.
3. Соберите проект и запустите его на эмуляторе или реальном устройстве.

## API

Приложение может использовать сторонние API для синхронизации данных о подписках (если применимо). В текущей версии синхронизация данных реализована локально через Room.

