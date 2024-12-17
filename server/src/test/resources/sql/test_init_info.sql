INSERT INTO condition_type
VALUES (1, 'positive', 10),
       (2, 'normal', 5),
       (3, 'negative', 1);

INSERT INTO conditions
VALUES (1, '최상의', 1, 15),
       (2, '활기찬', 1, 14),
       (3, '상쾌한', 1, 13),
       (4, '가벼운', 1, 12),
       (5, '생기 있는', 1, 11),
       (6, '안정적인', 2, 10),
       (7, '괜찮은', 2, 9),
       (8, '무난한', 2, 8),
       (9, '차분한', 2, 7),
       (10, '나른한', 2, 6),
       (11, '무기력한', 3, 5),
       (12, '피곤한', 3, 4),
       (13, '어지러운', 3, 3),
       (14, '지친', 3, 2),
       (15, '아픈', 3, 1);

INSERT INTO weather
VALUES (1, 'sunny'),
       (2, 'cloudy'),
       (3, 'overcast'),
       (4, 'rainy'),
       (5, 'thunder'),
       (6, 'snow');

INSERT INTO del_reason
VALUES (1, 'A'),
       (2, 'B'),
       (3, 'C'),
       (4, 'D'),
       (5, '기타');

INSERT INTO member
VALUES (11, 'abc@def.com', '사용자0', 'N', 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'KAKAOTALK', '111111', 'USER');

INSERT INTO dairy
VALUES (11, '2024-10-01', 11, 1, 2.0, NULL, NULL, NULL, NULL, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (12, '2024-10-02', 11, 1, 5.5, NULL, NULL, '땡큐', '배운', 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (13, '2024-10-03', 11, 1, 1.0, NULL, NULL, '감사', '공부', 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (14, '2024-10-04', 11, 1, 9.5, NULL, NULL, NULL, NULL, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (15, '2024-10-05', 11, 1, 4.5, NULL, NULL, 'oh', 'no', 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO dairy_condition
VALUES (101, 11, 1, 0, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (102, 11, 7, 1, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (103, 11, 2, 2, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (104, 11, 10, 3, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (105, 12, 8, 0, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (106, 12, 1, 1, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (107, 12, 2, 2, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (108, 12, 7, 3, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (109, 13, 13, 0, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (110, 13, 1, 1, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (111, 13, 7, 2, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (112, 13, 2, 3, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (113, 14, 2, 0, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (114, 14, 1, 1, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (115, 14, 7, 2, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (116, 14, 6, 3, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (117, 15, 9, 0, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (118, 15, 1, 1, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (119, 15, 7, 2, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (120, 15, 2, 3, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());