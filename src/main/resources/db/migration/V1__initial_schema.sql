-- Создание таблицы ролей
CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE
);

-- Создание таблицы специализаций
CREATE TABLE specializations (
                                 id BIGSERIAL PRIMARY KEY,
                                 code VARCHAR(20) NOT NULL UNIQUE,
                                 name VARCHAR(100) NOT NULL,
                                 description TEXT
);

-- Создание таблицы пользователей
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       enabled BOOLEAN DEFAULT TRUE
);

-- Связь пользователей с ролями (many-to-many)
CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL REFERENCES users(id),
                            role_id BIGINT NOT NULL REFERENCES roles(id),
                            PRIMARY KEY (user_id, role_id)
);

-- Связь пользователей со специализациями (many-to-many)
CREATE TABLE user_specializations (
                                      user_id BIGINT NOT NULL REFERENCES users(id),
                                      specialization_id BIGINT NOT NULL REFERENCES specializations(id),
                                      PRIMARY KEY (user_id, specialization_id)
);

-- Таблица тестов
CREATE TABLE tests (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(200) NOT NULL,
                       description TEXT,
                       duration_minutes INT,
                       is_visible BOOLEAN DEFAULT TRUE,
                       created_by BIGINT REFERENCES users(id),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Связь тестов со специализациями (many-to-many)
CREATE TABLE test_specializations (
                                      test_id BIGINT NOT NULL REFERENCES tests(id),
                                      specialization_id BIGINT NOT NULL REFERENCES specializations(id),
                                      PRIMARY KEY (test_id, specialization_id)
);

-- Таблица вопросов
CREATE TABLE questions (
                           id BIGSERIAL PRIMARY KEY,
                           test_id BIGINT NOT NULL REFERENCES tests(id),
                           text TEXT NOT NULL,
                           question_type VARCHAR(20) DEFAULT 'SINGLE_CHOICE',
                           display_order INT
);

-- Таблица ответов
CREATE TABLE answers (
                         id BIGSERIAL PRIMARY KEY,
                         question_id BIGINT NOT NULL REFERENCES questions(id),
                         text TEXT NOT NULL,
                         is_correct BOOLEAN DEFAULT FALSE
);

-- Таблица результатов тестирования
CREATE TABLE test_results (
                              id BIGSERIAL PRIMARY KEY,
                              user_id BIGINT NOT NULL REFERENCES users(id),
                              test_id BIGINT NOT NULL REFERENCES tests(id),
                              score INT NOT NULL,
                              max_score INT NOT NULL,
                              started_at TIMESTAMP,
                              completed_at TIMESTAMP
);

-- Таблица учебных материалов
CREATE TABLE study_materials (
                                 id BIGSERIAL PRIMARY KEY,
                                 title VARCHAR(200) NOT NULL,
                                 content TEXT,
                                 specialization_id BIGINT REFERENCES specializations(id),
                                 created_by BIGINT REFERENCES users(id),
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 is_visible BOOLEAN DEFAULT TRUE
);