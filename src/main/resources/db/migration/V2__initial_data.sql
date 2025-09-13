-- Заполнение ролей
INSERT INTO roles (name) VALUES
                             ('ROLE_CANDIDATE'),
                             ('ROLE_INSTRUCTOR'),
                             ('ROLE_ADMIN');

-- Заполнение специализаций (примеры для авиации)
INSERT INTO specializations (code, name, description) VALUES
                                                          ('PILOT_COMM', 'Пилот коммерческой авиации', 'Тестирование для пилотов коммерческих авиалиний'),
                                                          ('PILOT_PRIV', 'Частный пилот', 'Тестирование для частных пилотов'),
                                                          ('TECH_AVION', 'Авионика', 'Тестирование для специалистов по авионикe'),
                                                          ('TECH_ENG', 'Авиационный техник', 'Тестирование для авиационных техников');

-- Создание администратора (пароль: admin123)
INSERT INTO users (username, password, email, first_name, last_name) VALUES
    ('admin', '$2a$10$Tn7hn..pNfpYnuRGWxBM6eJPW4o75d5GTnrsyiS5m..4wy5TPPlSm', 'admin@aviationtest.ru', 'Иван', 'Петров');

-- Назначение роли администратора
INSERT INTO user_roles (user_id, role_id)
VALUES ((SELECT id FROM users WHERE username = 'admin'),
        (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'));