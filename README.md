# java-explore-with-me
### [Ссылка на pull request](https://github.com/airkng/java-explore-with-me/pull/6)
### Web-приложение для создания, поиска и бронирования интересных событий.
#### Описание:
Приложение состоит из двух микросервисов: сервис статистики и основной сервис. Запуск осуществляется командой 'docker-compose up'
### Сервис статистики:
Располагается на порте 9090 и отвечает за сбор статистики по просмотрам событий. Если пользователь/незарегистрированный пользователь
просматривает событие, то кол-во просмотров увеличивается. Получение статистики может осуществляться как только по уникальным ip-адресам, так и без них.
### Основной сервис:
Располагается на порте 8080 и отвечает за основные функции: регистрация, удаление, изменение пользователя, создание, изменение, удаление события, а также категории события.
Также есть возможность создания заявки на участие в событии. Основной сервис имеет возможность администрирования: есть функция публикации события, изменение категорий, отмена бронирования и другие.
### Дополнительная фича:
Комментарии к событиям