-- ===== СОЗДАНИЕ ПОЛЬЗОВАТЕЛЯ =====
CREATE USER userp WITH PASSWORD '12345';

-- ===== СОЗДАНИЕ БАЗЫ ДАННЫХ =====
CREATE DATABASE eco_zybr20 OWNER userp;

-- ===== ВЫДАЧА ПРИВИЛЕГИЙ НА БД =====
GRANT ALL PRIVILEGES ON DATABASE eco_zybr20 TO userp;

-- ===== ПЕРЕКЛЮЧЕНИЕ НА НОВУЮ БД =====
\c eco_zybr20;

-- ===== ВЫДАЧА ПРИВИЛЕГИЙ НА ТАБЛИЦЫ =====
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO userp;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO userp;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO userp;

-- ===== ПРИВИЛЕГИИ ПО УМОЛЧАНИЮ ДЛЯ НОВЫХ ТАБЛИЦ =====
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON TABLES TO userp;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON SEQUENCES TO userp;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON FUNCTIONS TO userp;

