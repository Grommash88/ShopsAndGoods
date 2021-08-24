# ShopsAndGoods

 Система управления товарами в магазинах, поддерживает команды:

    -Добавления магазина.
    -Удаление магазина.
    -Добавление товара в общую базу товаров.
    -Удаление товара из общей базы товаров.
    -Выставление на продажу в магазине.
    -Вывод в консоль свродной статистики.
    -Завершение работы.
Команды сопоставимы со строкой определенного формата, введенной
пользователем с клавиатуры, реализация в классе
com.grommash88.app.util.command_handler.Commands.

Обработка команд реализована в классе 
com.grommash88.app.util.command_handler.CommandHandler.

Модели Магазина и продукта описаны в классах Market и Product соответственно. 
Парсинг строк в объекты и объектов в сущности реализован в классе com.grommash88.app.util.Parser. 

Подключение к MongoDB, создание клиента и структура в классе com.grommash88.app.database.MongoDB,
параметры подключения в классе com.grommash88.app.database.props.Props и файле 
src/main/resources/application.properties.
Получение и сохранение данных, в базу, реализовано в классе com.grommash88.app.repository.RepositiryImpl 
имплементирующем интерфейсы com.grommash88.app.repository.parent.Repository и java.lang.AutoCloseable

Вывод в консоль сообщений пользователю и логирование исключений в файл logs/exception.log
реализованы в классе com.grommash88.app.util.logger.AppLogger, настройки логирования в
файле src/main/resources/log4j2.xml, шаблоны сообщений в классе com.grommash88.app.util.logger.Messages.

Для корректной работы база должна-быть запущена на порте localhost:27017, в противном 
случае укажите в файле src/main/resources/application.properties ваши настройки,
поля username и password необходимо заполнить в соответствии с настройками вашей БД,
моя БД при подключении их не требует.


Product management system in stores, supports commands:

    -Adding the store.
    -Deleting the store.
    -Adding a product to the general database of products.
    -Deleting a product from the general database of products.
    -Exhibition for sale in the store.
    -Output to the console svrody statistics.
    -Completion of work.
Commands are comparable to a specific format string entered
keyboard with keyboard, class implementation
com.grommash88.app.util.command_handler.Commands.

Command processing is implemented in the class
com.grommash88.app.util.command_handler.CommandHandler.

Store and Product Models in Market and respectively.
Parsing strings into objects and objects in essence is implemented in the com.grommash88.app.util.Parser class.

MongoDB connection, client creation and structure in com.grommash88.app.database.MongoDB class,
connection parameters in com.grommash88.app.database.props.Props class and file
src / main / resources / application.properties.
Receiving and saving data to the database is implemented in the com.grommash88.app.repository.RepositiryImpl class
implementing the com.grommash88.app.repository.parent.Repository and java.lang.AutoCloseable interfaces

Displaying messages to the user in the console and logging exceptions to the logs / exception.log file
implemented in the com.grommash88.app.util.logger.AppLogger class, logging settings in
src / main / resources / log4j2.xml file, message templates in com.grommash88.app.util.logger.Messages class.

For correct operation, the database must be running on port localhost: 27017, otherwise
In case, specify your settings in the src / main / resources / application.properties file,
the username and password fields must be filled in according to the settings of your database,
my database does not require them when connecting.