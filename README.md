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

## Инициализация библиотеки 
Описана в виде примера в методе doToken файла AppModel.kt

## Методы библиотеки 
Описаны при помощи JavaDocs в файле NotificationSdk.kt, а использование методов можно посмотреть в демо приложении PushServiceSample.

## Интеграция PushService SDK в приложение Flutter
1.	Найдите программный код вашего приложения Flutter для Android (из одноименного подкаталога ”Android”), далее – приложение Flutter для Android;
2.	Добавить поддержку PushService SDK в приложение Flutter для Android (Cм коммит ”Добавление PushService SDK” демонстрационного приложения для Flutter) как в обычное приложение Android (см: https://github.com/zgr-im/zgr-pushservice-android-sdk-builds). При этом может потребоваться изменить имя пакета приложения Flutter для Android на то имя, которое задано в вашем google-services.json. 
3.	Откройте приложение Flutter для Android как отдельный проект Android и выполните синхронизацию gradle, после этого вам станут доступны классы библиотеки PushService SDK для Android;
4.	Унаследуйте главную MainActivity приложения Flutter для Android от класса im.zgr.pushservice.NotificationActivityFlutter, который в составе PushService SDK (добавленной в рамках пункта 2).
5.	Далее используйте PushService SDK как в обычном Android приложении (например, зарегистрируйте токен на сервере из вашего класса MainActivity) и / или вызывайте методы PushService SDK из вашего приложения Flutter через стандартную технологию взаимодействия между кодом Android (Java/Котлин) и кодом Flutter.

## Интеграция PushService SDK в приложение React Native
Осуществляется аналогично тому как интегрируется PushService SDK в приложение Flutter, за исключением того, что на этапе 4 вместо im.zgr.pushservice.NotificationActivityFlutter испольуется im.zgr.pushservice.NotificationActivityReact.






