INSERT INTO Genres (name)
VALUES ('Драма'),
       ('Комедия'),
       ('Боевик'),
       ('Триллер'),
       ('Фантастика'),
       ('Фэнтези'),
       ('Ужасы'),
       ('Мелодрама'),
       ('Детектив'),
       ('Приключения'),
       ('Анимация'),
       ('Документальный'),
       ('Криминал'),
       ('Исторический'),
       ('Вестерн'),
       ('Музыкальный'),
       ('Семейный'),
       ('Спорт'),
       ('Биография'),
       ('Военный'),
       ('Нуар'),
       ('Киберпанк'),
       ('Постапокалипсис'),
       ('Сказка'),
       ('Мистика'),
       ('Супергеройский');

INSERT INTO age_ratings (code, name, min_age, description) VALUES
                                                               ('0+', 'Для всех возрастов', 0, 'Без ограничений'),
                                                               ('6+', 'Для детей старше 6 лет', 6, 'Может содержать отдельные пугающие сцены'),
                                                               ('12+', 'Для зрителей старше 12 лет', 12, 'Может содержать умеренное насилие'),
                                                               ('16+', 'Для зрителей старше 16 лет', 16, 'Может содержать сцены насилия и эротику'),
                                                               ('18+', 'Только для взрослых', 18, 'Содержит материалы для взрослых');



INSERT INTO films (description, poster_path, rating, title, year, duration, age_rating_id, status) VALUES
                                                                                                       (N'Джон Уик вынужден снова выйти на тропу войны, когда Высший совет объявляет на него охоту. В четвертой части культового боевика герою предстоит сразиться с могущественными врагами по всему миру - от осакского континентального отеля до парижских катакомб. Фильм поражает зрелищными экшен-сценами и беспрецедентным уровнем хореографии боёв.', N'/uploads/posters/1744646137413_johnwik.jpg', 7.0, N'Джон Уик 4', 2023, 95, 5, 'NOW'),

                                                                                                       (N'Барби и Кен покидают идеальный мир Барбиленда и отправляются в реальный мир, где их ждут неожиданные открытия и важные жизненные уроки. Этот красочный и ироничный фильм от режиссера Греты Гервиг сочетает ностальгию по культовой кукле с остроумной сатирой на современное общество. Фильм стал культурным феноменом лета 2023 года.', N'/uploads/posters/1747843125746_barbi.jpg', 7.0, N'Барби', 2023, 85, 3, 'NOW'),

                                                                                                       (N'После смерти короля Т"Чаллы народ Ваканды сталкивается с новой угрозой - могущественным подводным царством Талокан. Шури, королева Рамонда и воины Ваканды должны объединиться, чтобы защитить свою страну. Фильм стал трогательным прощанием с Чедвиком Боузманом и важной вехой в истории кинематографической вселенной Marvel.', N'/uploads/posters/1744646110517_black_pantera.jpg', 7.0, N'Черная Пантера: Ваканда навсегда', 2022, 81, 3, 'NOW'),

                                                                                                       (N'Спустя более десяти лет после событий первого фильма, Джейк Салли и Нейтири ведут мирную жизнь на Пандоре, воспитывая детей. Но когда людям снова угрожает опасность, они вынуждены покинуть родные джунгли и искать убежища у племен рифов. Визуально ошеломляющий фильм устанавливает новые стандарты подводных съемок и CGI-технологий.', N'/uploads/posters/1744646100764_avatar.jpg', 7.0, N'Аватар: Путь воды', 2022, 83, 3, 'NOW'),

                                                                                                       (N'На второй год своей борьбы с преступностью Брюс Уэйн сталкивается с серией загадочных убийств, оставляемых таинственным Загадочником. Расследуя преступления, Бэтмен обнаруживает коррупцию, пронизывающую Готэм. Мрачный и стильный ремейк от режиссера Мэтта Ривза предлагает новый взгляд на легендарного героя.', N'/uploads/posters/1744646092574_batman.jpg', 7.0, N'Бэтмен', 2022, 80, 5, 'NOW'),

                                                                                                       (N'Пит "Мэверик" Митчелл, один из лучших пилотов ВМС, возвращается, чтобы обучать новое поколение летчиков для выполнения смертельно опасной миссии. Фильм сочетает ностальгию по классике 1986 года с потрясающими авиационными сценами, снятыми без CGI. Том Круз демонстрирует невероятную преданность своему культовому персонажу.', N'/uploads/posters/1744646085321_topgan.jpg', 8.0, N'Топ Ган: Мэверик', 2022, 65, 3, 'SOON'),

                                                                                                       (N'Молодой Пол Атрейдес отправляется на опасную планету Арракис - единственный источник ценной пряности. Когда его семья попадает в заговор, Пол должен выбрать между безопасным путем и судьбой, которая изменит галактику. Монументальная экранизация классического романа Фрэнка Герберта поражает масштабом и вниманием к деталям.', N'/uploads/posters/1744646075085_duna2021.jpg', 8.0, N'Дюна', 2021, 98, 4, 'SOON'),

                                                                                                       (N'Фильм рассказывает драматическую историю Роберта Оппенгеймера - отца атомной бомбы и одного из самых противоречивых ученых XX века. От первых дней Манхэттенского проекта до моральных терзаний после Хиросимы, картина исследует цену научного прогресса. Кристофер Нолан создает напряженный интеллектуальный триллер с блестящей игрой Киллиана Мерфи.', N'/uploads/posters/1747580330693_opengame.jpg', 8.0, N'Оппенгеймер', 2023, 56, 5, 'SOON'),

                                                                                                       (N'Любимые Стражи Галактики возвращаются в своем финальном приключении, чтобы защитить одного из своих и раскрыть тайны прошлого Ракеты. Фильм Джеймса Ганна сочетает фирменный юмор франшизы с неожиданно эмоциональной историей о семье, искуплении и жертве. Завершение трилогии стало одним из самых трогательных в истории MCU.', N'/uploads/posters/1747580721481_strage.jpg', 8.0, N'Стражи Галактики 3', 2023, 56, 3, 'SOON'),

                                                                                                       (N'Кот в сапогах обнаруживает, что его страсть к приключениям дорого обошлась: он истратил восемь из девяти своих жизней. Чтобы восстановить их, он отправляется на поиски мифического Последнего Желания. Этот анимационный шедевр сочетает стиль спагетти-вестерна с потрясающей визуальной стилистикой и неожиданно глубокой философией.', N'/uploads/posters/1747580672788_cat.jpg', 7.0, N'Кот в сапогах: Последнее желание', 2022, 68, 2, 'SOON');

INSERT INTO film_genres (film_id, genre_id) VALUES (1, 3);
INSERT INTO film_genres (film_id, genre_id) VALUES (2, 2);
INSERT INTO film_genres (film_id, genre_id) VALUES (3, 26);
INSERT INTO film_genres (film_id, genre_id) VALUES (4, 5);
INSERT INTO film_genres (film_id, genre_id) VALUES (5, 26);
INSERT INTO film_genres (film_id, genre_id) VALUES (5, 9);
INSERT INTO film_genres (film_id, genre_id) VALUES (6, 3);
INSERT INTO film_genres (film_id, genre_id) VALUES (6, 1);
INSERT INTO film_genres (film_id, genre_id) VALUES (7, 5);
INSERT INTO film_genres (film_id, genre_id) VALUES (7, 10);
INSERT INTO film_genres (film_id, genre_id) VALUES (8, 19);
INSERT INTO film_genres (film_id, genre_id) VALUES (8, 1);
INSERT INTO film_genres (film_id, genre_id) VALUES (9, 5);
INSERT INTO film_genres (film_id, genre_id) VALUES (9, 26);
INSERT INTO film_genres (film_id, genre_id) VALUES (9, 2);
INSERT INTO film_genres (film_id, genre_id) VALUES (10, 11);
INSERT INTO film_genres (film_id, genre_id) VALUES (10, 10);
INSERT INTO film_genres (film_id, genre_id) VALUES (10, 6);




INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-01', '10:00:00', '10:30:00', 2);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-01', '15:00:00', '15:45:00', 2);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-02', '11:30:00', '12:00:00', 2);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-03', '14:00:00', '14:30:00', 2);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-04', '16:15:00', '17:00:00', 2);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-05', '09:00:00', '09:30:00', 2);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-06', '13:45:00', '14:15:00', 2);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-07', '17:30:00', '18:00:00', 2);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-08', '12:00:00', '12:45:00', 2);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-09', '10:45:00', '11:15:00', 2);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-10', '15:30:00', '16:00:00', 2);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-11', '14:30:00', '15:15:00', 2);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-12', '11:00:00', '11:30:00', 2);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-13', '16:45:00', '17:15:00', 2);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-14', '13:00:00', '13:45:00', 2);

-- Технические перерывы для зала с id = 2
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-01', '09:30:00', '10:15:00', 3);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-02', '14:15:00', '14:45:00', 3);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-03', '12:30:00', '13:00:00', 3);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-04', '17:00:00', '17:30:00', 3);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-05', '10:30:00', '11:00:00', 3);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-06', '15:15:00', '15:45:00', 3);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-07', '13:30:00', '14:00:00', 3);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-08', '16:00:00', '16:30:00', 3);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-09', '11:45:00', '12:15:00', 3);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-10', '14:45:00', '15:15:00', 3);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-11', '12:00:00', '12:30:00', 3);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-12', '17:15:00', '17:45:00', 3);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-13', '10:00:00', '10:45:00', 3);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-14', '15:00:00', '15:30:00', 3);
INSERT INTO technical_break (date, start, [end], hall_id) VALUES ('2023-10-15', '13:15:00', '13:45:00', 3);

INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-26 10:20:00.000000', N'2025-05-26 09:00:00.000000', N'PUBLISHED', 5, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-26 12:15:00.000000', N'2025-05-26 10:40:00.000000', N'PUBLISHED', 1, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-26 14:00:00.000000', N'2025-05-26 12:35:00.000000', N'PUBLISHED', 2, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-26 15:40:00.000000', N'2025-05-26 14:20:00.000000', N'PUBLISHED', 5, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-26 17:35:00.000000', N'2025-05-26 16:00:00.000000', N'PUBLISHED', 1, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-26 19:20:00.000000', N'2025-05-26 17:55:00.000000', N'PUBLISHED', 2, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-26 21:00:00.000000', N'2025-05-26 19:40:00.000000', N'PUBLISHED', 5, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-26 22:55:00.000000', N'2025-05-26 21:20:00.000000', N'PUBLISHED', 1, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-26 10:25:00.000000', N'2025-05-26 09:00:00.000000', N'PUBLISHED', 4, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-26 12:05:00.000000', N'2025-05-26 10:45:00.000000', N'PUBLISHED', 5, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-26 13:50:00.000000', N'2025-05-26 12:25:00.000000', N'PUBLISHED', 3, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-26 15:45:00.000000', N'2025-05-26 14:10:00.000000', N'PUBLISHED', 1, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-26 17:30:00.000000', N'2025-05-26 16:05:00.000000', N'PUBLISHED', 4, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-26 19:15:00.000000', N'2025-05-26 17:50:00.000000', N'PUBLISHED', 2, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-26 21:10:00.000000', N'2025-05-26 19:35:00.000000', N'PUBLISHED', 1, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-26 22:55:00.000000', N'2025-05-26 21:30:00.000000', N'PUBLISHED', 4, 2);

INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-27 10:20:00.000000', N'2025-05-27 09:00:00.000000', N'PUBLISHED', 5, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-27 12:15:00.000000', N'2025-05-27 10:40:00.000000', N'PUBLISHED', 1, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-27 14:00:00.000000', N'2025-05-27 12:35:00.000000', N'PUBLISHED', 2, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-27 15:40:00.000000', N'2025-05-27 14:20:00.000000', N'PUBLISHED', 5, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-27 17:35:00.000000', N'2025-05-27 16:00:00.000000', N'PUBLISHED', 1, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-27 19:20:00.000000', N'2025-05-27 17:55:00.000000', N'PUBLISHED', 2, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-27 21:00:00.000000', N'2025-05-27 19:40:00.000000', N'PUBLISHED', 5, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-27 22:55:00.000000', N'2025-05-27 21:20:00.000000', N'PUBLISHED', 1, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-27 10:25:00.000000', N'2025-05-27 09:00:00.000000', N'PUBLISHED', 4, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-27 12:05:00.000000', N'2025-05-27 10:45:00.000000', N'PUBLISHED', 5, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-27 13:50:00.000000', N'2025-05-27 12:25:00.000000', N'PUBLISHED', 3, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-27 15:45:00.000000', N'2025-05-27 14:10:00.000000', N'PUBLISHED', 1, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-27 17:30:00.000000', N'2025-05-27 16:05:00.000000', N'PUBLISHED', 4, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-27 19:15:00.000000', N'2025-05-27 17:50:00.000000', N'PUBLISHED', 2, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-27 21:10:00.000000', N'2025-05-27 19:35:00.000000', N'PUBLISHED', 1, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-27 22:55:00.000000', N'2025-05-27 21:30:00.000000', N'PUBLISHED', 4, 2);

INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-28 10:20:00.000000', N'2025-05-28 09:00:00.000000', N'PUBLISHED', 5, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-28 12:15:00.000000', N'2025-05-28 10:40:00.000000', N'PUBLISHED', 1, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-28 14:00:00.000000', N'2025-05-28 12:35:00.000000', N'PUBLISHED', 2, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-28 15:40:00.000000', N'2025-05-28 14:20:00.000000', N'PUBLISHED', 5, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-28 17:35:00.000000', N'2025-05-28 16:00:00.000000', N'PUBLISHED', 1, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-28 19:20:00.000000', N'2025-05-28 17:55:00.000000', N'PUBLISHED', 2, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-28 21:00:00.000000', N'2025-05-28 19:40:00.000000', N'PUBLISHED', 5, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-28 22:55:00.000000', N'2025-05-28 21:20:00.000000', N'PUBLISHED', 1, 1);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-28 10:25:00.000000', N'2025-05-28 09:00:00.000000', N'PUBLISHED', 4, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-28 12:05:00.000000', N'2025-05-28 10:45:00.000000', N'PUBLISHED', 5, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-28 13:50:00.000000', N'2025-05-28 12:25:00.000000', N'PUBLISHED', 3, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-28 15:45:00.000000', N'2025-05-28 14:10:00.000000', N'PUBLISHED', 1, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-28 17:30:00.000000', N'2025-05-28 16:05:00.000000', N'PUBLISHED', 4, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-28 19:15:00.000000', N'2025-05-28 17:50:00.000000', N'PUBLISHED', 2, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-28 21:10:00.000000', N'2025-05-28 19:35:00.000000', N'PUBLISHED', 1, 2);
INSERT INTO Cinemas.dbo.sessions (end_time, start_time, status, film_id, hall_id) VALUES (N'2025-05-28 22:55:00.000000', N'2025-05-28 21:30:00.000000', N'PUBLISHED', 4, 2);