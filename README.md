# Eco Zubr Bot

Telegram бот на MAX Long Poll API с Java Spring Boot и PostgreSQL.

## Инструкция по запуску

### 1. Клонируйте репозиторий

\`\`\`bash
git clone https://github.com/ZhenShenITIS/eco-zubr-bot.git
cd eco-zubr-bot
\`\`\`

### 2. Создайте .env файл в корневой папке проекта

\`\`\`bash
cp .env.example .env
\`\`\`

Отредактируйте \`.env\` и добавьте:
- \`MAX_TOKEN\` — ваш токен MAX API
- \`YANDEX_GEOCODER_TOKEN\` — ваш токен Яндекс Геокодера
  Следующие аттрибуты можно оставить как есть(по умолчанию докер создаст базу данных eco_zybr20, пользователя userp, с паролем 12345. см. Dockerfile, init-db.sql. Если оставить следующие поля по умолчанию, то отдельно создавать бд не потребуется)
- \DB_USER=userp (Пользователь бд)
- DB_PASSWORD=12345 (Пароль пользователя)
- DB_NAME=eco_zybr20 (имя бд)
- DB_HOST=postgres (хост бд)
- DB_PORT=5432 (порт бд)

### Как получить токен YANDEX_GEOCODER_TOKEN:
1) перейти по ссылке: https://developer.tech.yandex.ru/
2) Нажать подключить api и выбрать Javascript Api и HTTP Геокодер
3) Заполнить форму и нажать Отправить
4) Скопировать предоставленный api токен и добавить в файл .env


### 3. Собрать и запустить контейнеры

docker-compose up -d --build

### 4. Смотреть логи

docker-compose logs -f app

Бот запустится и подключится к PostgreSQL.

### 5.1 Остановить

docker-compose down

### 5.2 Остановить и удалить бд:

docker-compose down -v