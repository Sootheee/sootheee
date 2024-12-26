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