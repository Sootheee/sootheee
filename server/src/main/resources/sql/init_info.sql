INSERT INTO condition_type
VALUES ('COTY001', 'positive', 10),
       ('COTY002', 'normal', 5),
       ('COTY003', 'negative', 1);

INSERT INTO conditions
VALUES ('COND001', '최상의', 'COTY001', 15),
       ('COND002', '활기찬', 'COTY001', 14),
       ('COND003', '상쾌한', 'COTY001', 13),
       ('COND004', '가벼운', 'COTY001', 12),
       ('COND005', '생기 있는', 'COTY001', 11),
       ('COND006', '안정적인', 'COTY002', 10),
       ('COND007', '괜찮은', 'COTY002', 9),
       ('COND008', '무난한', 'COTY002', 8),
       ('COND009', '차분한', 'COTY002', 7),
       ('COND010', '나른한', 'COTY002', 6),
       ('COND011', '무기력한', 'COTY003', 5),
       ('COND012', '피곤한', 'COTY003', 4),
       ('COND013', '어지러운', 'COTY003', 3),
       ('COND014', '지친', 'COTY003', 2),
       ('COND015', '아픈', 'COTY003', 1);

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