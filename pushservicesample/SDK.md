# PushService
## Введение
Для внедрения sdk в мобильное приложение, необходимо для мобильного приложения настроить интеграцию с Firebase, добавить библиотеку и настроить библиотеку PushService и добавить вызов методов регистрации токена и телефона пользователя или его внешнего идентификатора (externalUserId).

## Системные требования
- Минимальная версия поддерживаемой OS: Android 5.0 (SDK 21)
- Для работы sdk требуется доступ к Интернету

## Интеграция Firebase в проект
Необходимо создать проект в Firebase Console, добавить зависимости Firebase и конфигурационный файл.
Подробно процесс описан здесь: https://firebase.google.com/docs/android/setup?authuser=0

## Интеграция Huawei PushKit в проект
Необходимо создать проект в Huawei Console, добавить зависимости PushKit и конфигурационный файл.
Подробно процесс описан здесь: https://developer.huawei.com/consumer/en/codelab/HMSPushKit/

## Добавление библиотеки PushService
- скопировать pushservice.aar в директорию /<APP_NAME>/libs/
- добавить файл ZGRConfig.json в директорию assets проекта. В этом файле должны содержаться корректные идентификаторы проекта
- Задать иконку уведомления для отображения в статус баре методом `NotificationSdk.getInstance(context).setNotificationIconResId(R.drawable.ic_notification_icon)`. Требования к формату иконки описаны тут: http://marpol.i234.me/android_sdk_doc//guide/practices/ui_guidelines/icon_design_status_bar.html 

## Регистрация токена устройства (Firebase InstanceId)
При старте приложения требуется сохранить текущий push-токен и текущее состояние настройки оповещений в ОС, для этого надо вызвать:
```
NotificationSdk.getInstance(context).registerPushToken(
    { //Successful register token }, 
    { //Error while register token }
)
```
## Регистрация номера телефона или идентификатора пользователя
Для регистрации номера телефона или внешнего идентификатора пользователя необходимо использовать метод SDK - updateUserProfile.
Телефон или текстовый идентификатор обязателен для заполнения.
```
NotificationSdk.getInstance(context).updateUserProfile(
    "71234567890", // начинается с «7», затем 10 цифр без пробелов, скобок и дефисов
    "id-45F445KA", // текстовый идентификатор пользователя.
    { //Successful save data }, 
    { //Error while save data }
)
```
Так же существует альтернативный метод - personalize. Он предназначен для сохранения externalUserId
```
NotificationSdk.getInstance(context).personalize(
    "id-45F445KA", // текстовый идентификатор пользователя. Обязателен для заполнения
    { //Successful save data }, 
    { //Error while save data }
)
```
Для сохранения только номера телефона можно так же использовать метод saveUser
```
NotificationSdk.getInstance(context).saveUser(
    "71234567890", // начинается с «7», затем 10 цифр без пробелов, скобок и дефисов
    { //Successful save data }, 
    { //Error while save data }
)
```
## Работа с информацией о текущей инсталяции
Для получения информации о инсталяции используется метод `getInstallationInfo`
```
NotificationSdk.getInstance(context).getInstallationInfo(
    { //Successful fetch data }, 
    { //Error while fetch data }
)
```
Алиасом для данного метода выступает метод `fetchInstallation`
```
NotificationSdk.getInstance(context).fetchInstallation(
    { //Successful fetch data }, 
    { //Error while fetch data }
)
```
Для сохранения информации о инсталяции используется метод `updateInstallationInfo`
```
NotificationSdk.getInstance(context).updateInstallationInfo(
    pushEnabled,
    settings,
    primary,
    { //Successful fetch data }, 
    { //Error while fetch data }
)
```
Алиасом для данного метода выступает метод `saveInstallation`
```
NotificationSdk.getInstance(context).saveInstallation(
    pushEnabled,
    settings,
    primary,
    { //Successful fetch data }, 
    { //Error while fetch data }
)
```

## Сохранение истории Push-уведомлений на устройстве
Если требуется сохранять историю push-уведомлений на устройстве, необходимо активировать данную функцию при инициализации SDK.
Для активации необходимо вызвать метод `setLocalDatabaseEnabled(true)`. После чего все получаемые приложением push-уведомления будут сохраняться в локальную БД.
Для работы с историей push-уведомлений необходимо использовать следующие методы:

`getNotificationsFromHistory` - метод используется для получения порции сохранённых push-уведомлений из локальной БД.
Пример:
- `limit` - Int, размер порции данных.
- `startFromId` - Long, идентификатор начиная с которого (не включая) выбрать порцию уведомлений
- `dateFrom` - Date, фильтр по дате. Выбрать сообщения начиная с этой даты.
- `dateTo` - Date, фильтр по дате. Выбрать сообщения заканчивая этой датой.
- `sortAsc` - Boolean, сортировать уведомления по дате в порядке возрастания - true, в порядке убывания - false.
```

NotificationSdk.getInstance(context)
    .getNotificationsFromHistory(limit, startFromId, dateFrom, dateTo, sortAsc, {
        if (it.isNotEmpty()) {
            it.forEach{
                ...work with it:NotificationDto...
            }
        }
    })
```

`deleteNotificationFromHistory` - метод используется для удаления одного push-уведомления из локальной БД. В качестве параметра передаётся идентификатор push-уведомления в локальной БД.

`deleteAllNotificationsFromHistory` - метод используется для удаления всех push-уведомлений из локальной БД.

Для получения уведомлений о получении push сообщений можно создать BroadcastReceiver следующим образом:
LocalBroadcastManager.getInstance(this).registerReceiver(object : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
    
        val messageId = intent.getStringExtra("messageId")
        
    }}, IntentFilter ("im.zgr.pushservice.message"))
    

## Настраиваемые параметры sdk
Для настройки параметров sdk необходимо вызывать методы `NotificationSdk.getInstance(context).set…`

- `setLogsEnabled(true)` - Флаг включающий логирование. Рекомендуется использовать для отладки, но не забыть выключить его при релизной сборки.
- `setNotificationChannelName(«Имя канала»)` - Для версий Android Oreo и выше sdk создаст “Notification channel”, который будет отображаться в настройках приложений. Название канала можно задать данным методом.
- `setNotificationImportance(importance)` - Также через sdk можно указать «важность» сообщений, т.е. то, как уведомления по умолчанию будут отображаться для пользователя (будут ли звуковые оповещения, будет ли всплывающее окно или уведомление будет тихо добавлено в статус бар). Доступные значения (аналогичны значениям `NotificationManager.Importance`, описание тут: https://developer.android.com/training/notify-user/channels#importance):
    * NotificationImportance.Default
    * NotificationImportance.Min
    * NotificationImportance.Low,
    * NotificationImportance.High
- `setLocalDatabaseEnabled(true)` - Включить сохранение истории Push-уведомлений на устройстве (см. соответствующий раздел).

**Внимание!** Все методы SDK (включая регистрацию токена/телефона) необходимо вызывать из основного потока MainThread (https://developer.android.com/guide/components/processes-and-threads?hl=ru#Threads)

## Обработка нажатий на кнопки из push-уведомлений
Для того, чтобы иметь возможность обрабатывать нажатие кнопок из Push-уведомлений, необходимо создать сервис, в качестве intent-фильтра указать список названий action, которые необходимо обработать, пример из AndroidManifest.xml:
```
<service
        android:name=".MyService"
        android:enabled="true"
        android:exported="true">
    <intent-filter>
        <action android:name="link"/>
        <action android:name="open-app"/>
        <action android:name="clear"/>
    </intent-filter>
</service>
```
Т.о. MyService будет получать события нажатия на кнопки с action равным "link", "open-app", "clear". 
В onStartCommand, в атрибуте Intent: intent.action будет название действия, intent так же будет содержать Parcelable-extra-параметры:

* currentNotification: im.zgr.pushservice.domain.dto.NotificationDto - объект содержит в себе информацию о текущем оповещении
* currentAction: im.zgr.pushservice.domain.dto.ActionDto - объект содержит в себе информацию о текущем действии над оповещением

## Обработка нажатия на Push-уведомление
При нажатии на Push-уведомление происходит открытие основного activity приложения, в intent передаются extra-параметры:

* currentNotification: im.zgr.pushservice.domain.dto.NotificationDto - объект содержит в себе информацию о текущем оповещении
