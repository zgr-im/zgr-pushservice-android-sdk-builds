# PushService

## Введение
Для внедрения sdk в мобильное приложение необходимо настроить интеграцию с одной или несколькими системами доставки пуш сообщений (Google FCM, Huawei PushKit, RuStore), добавить библиотеку pushservice, вызвать методы инициализации библиотеки PushService (в том числе регистрации токена, телефона пользователя или его внешнего идентификатора - externalUserId).

## Системные требования
- Минимальная версия поддерживаемой OS: Android 6.0 (SDK 23)
- Для работы sdk требуется доступ к Интернету

## Интеграция Firebase с приложением
Необходимо создать проект в Firebase Console, добавить зависимости Firebase и конфигурационный файл.
Подробно процесс описан здесь: https://firebase.google.com/docs/android/setup?hl=ru

## Интеграция Huawei PushKit с приложением
Необходимо создать проект в Huawei Console, добавить зависимости PushKit и конфигурационный файл.
Подробно процесс описан здесь: https://developer.huawei.com/consumer/en/codelab/HMSPushKit/

## Интеграция RuStore с приложением
Необходимо создать проект в Huawei Console, добавить зависимости RuStore и конфигурационный файл.
Подробно процесс описан здесь: https://help.rustore.ru/rustore/for_developers/developer-documentation/sdk_push-notifications/RuStore-SDK-push-notifications/general_SDK-push-notifications

## Добавление библиотеки PushService
- добавить в Gradle файл проекта репозиторий зависимостей библиотеки pushservice 
- добавить в Gradle файл приложения зависимость com.zagruzka:pushservice 
- добавить в Gradle файл приложения зависимости для работы com.zagruzka:pushservice 
- добавить файл ZGRConfig.json в директорию assets проекта с описанием соединения к вашему backend 

## Инициализация библиотеки в виде примера описана в методе doToken файла AppModel.kt

## Методы библиотеки описаны при помощи JavaDocs в файле NotificationSdk.kt, а использование методов можно посмотреть в демо приложении PushServiceSample.

## Внедрение в нативную часть Android приложения на Flutter осуществляется также как в нативное приложение на Kotlin или Java.





