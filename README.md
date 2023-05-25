# Команда 2х2
Инструкции по запуску 
1. Склонировать текущий репозиторий chat-api (git clone https://github.com/HeatDead/chat-api.git)
2. Перейти в проект chat-api  (cd chat-api)
3. Выполнить команду docker-compose up 
4. Запустить spring приложение - строго после docker-compose up  т.к  spring конфигурирует keycloak и созадет базовых юзеров
mvn install
java -jar target/web-socket-project-0.0.1-SNAPSHOT.jar
(если не с работает запустить в intelij idea) 
5. После этого перейти в другой каталог  cd ..
6.  Склонировать React-frontend chat-app   https://github.com/Lolsogod/chat-app.git
7.   Перейдя в каталог  cd chat-app выполнить последовательно команды:
 npm i
 npm run dev
Перейти на localhost:3000
( все инструкции выше проверялись в macos, ubuntu 22.04, fedora ни у одного участника команды нет на пк windows  что бы проверить в ней)


# Дефолтные пользователи
 1.  login admin pass admin   - admin  здесь в качестве l1 агента тех поддержки 
 2.  login user pass user
 3.  4 агента тех поддержки по каждому продукту пароль у всех manager
    credit_manager  manager
    expense_manager manager
    factoring_manager manager
    guarantees_manager manager

Зайти и задать вопрос
либо за user:user, либо создать нового 
