INSERT INTO condition_type
VALUES ('COTY001', 'positive', 10),
       ('COTY002', 'normal', 5),
       ('COTY003', 'negative', 1);

INSERT INTO conditions
VALUES ('COND001', '최상의', 1, 15),
       ('COND002', '활기찬', 1, 14),
       ('COND003', '상쾌한', 1, 13),
       ('COND004', '가벼운', 1, 12),
       ('COND005', '생기 있는', 1, 11),
       ('COND006', '안정적인', 2, 10),
       ('COND007', '괜찮은', 2, 9),
       ('COND008', '무난한', 2, 8),
       ('COND009', '차분한', 2, 7),
       ('COND010', '나른한', 2, 6),
       ('COND011', '무기력한', 3, 5),
       ('COND012', '피곤한', 3, 4),
       ('COND013', '어지러운', 3, 3),
       ('COND014', '지친', 3, 2),
       ('COND015', '아픈', 3, 1);

INSERT INTO weather
VALUES ('WEAT001', 'sunny'),
       ('WEAT002', 'cloudy'),
       ('WEAT003', 'overcast'),
       ('WEAT004', 'rainy'),
       ('WEAT005', 'thunder'),
       ('WEAT006', 'snow');

INSERT INTO del_reason
VALUES ('DERE001', 'A'),
       ('DERE002', 'B'),
       ('DERE003', 'C'),
       ('DERE004', 'D'),
       ('DERE005', '기타');

INSERT INTO member
VALUES (11, 'abc@def.com', '사용자0', 'N', 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'KAKAOTALK', '111111', 'USER');

INSERT INTO dairy
VALUES (11, '2024-10-01', 11, 1, 2.0, NULL, NULL, 'oh', 'no', 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (12, '2024-10-02', 11, 1, 5.5, NULL, NULL, '땡큐', '배운', 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (13, '2024-10-03', 11, 1, 1.0, NULL, NULL, '감사', '공부', 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (14, '2024-10-04', 11, 1, 9.5, NULL, NULL, NULL, NULL, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (15, '2024-10-05', 11, 1, 4.5, NULL, NULL, NULL, NULL, 'N', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

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