## Погодное приложение.
Разработка приложения велась с целью изучать основы Android и Kotlin.  
Часть коммитов относятся к тестированию и попытке добавить новые изученные функции.  
Использовал архитектурный подход MVVM (ViewModel и LiveData).  
В приложении использовал/тестировал/изучал:: 
- ViewBinding.
- Extension функции.
- PendingIntent.
- HttpURLConnection.
- OkHttp.
- Retrofit.
- Room.
- Handler,looper,View.post и тд
- Service и BroadcastReceiver. 
- FirebaseMessagingService
- Permissions(запрос разрешений у пользователя)
- Content Provider , ContentResolver 
- Geolocation (LocationManager и Geocoder)
- Bild types и flavors:  несколько сборок 
- ViewPager 
- Coil и Picasso

Функционал (кратко).  
- Подключаемся к Яндекс API с помощью Retrofit получаем данные и показываем в Recycler.
- Почасовой прогноз и прогноз на неделю.
- Определить местоположение и узнать погоду
- Хранение истории запросов и поиск по истории запросов с возможность узнать погоду по выбранной записи. 
- Запросить контакты с телефона и позвонить.
- Принять уведомление от FireBase и открыть приложение по уведомлению
- Так же есть 2 версии сборки free и paid. Разница в paid версии есть геолокация. 
- Экраны:   
Главный экран со списком городов (список статический).  
Экран выбранного города с текущей погодой и прогнозом на неделю.  
Экран с историей поиска, история хранится в БД ROOM.  

---
### Фото
[Главный экран список городов](https://github.com/EgorVeber/KotlinMVVM/blob/master/1.png?raw=true)  
[Экран выбранного город1](https://github.com/EgorVeber/KotlinMVVM/blob/master/2.png?raw=true)  
[Экран выбранного город2](https://github.com/EgorVeber/KotlinMVVM/blob/master/3.png?raw=true)  
[Экран Истории](https://github.com/EgorVeber/KotlinMVVM/blob/master/6.png?raw=true)  
[Диалог с доп даннымми](https://github.com/EgorVeber/KotlinMVVM/blob/master/4.png?raw=true)  
[Диалог с геолокацией](https://github.com/EgorVeber/KotlinMVVM/blob/master/5.png?raw=true)  
[Уведомление](https://github.com/EgorVeber/KotlinMVVM/blob/master/7.png?raw=true)
