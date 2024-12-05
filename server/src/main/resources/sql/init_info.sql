INSERT INTO condition_type
VALUES (NULL, 'positive', 10),
       (NULL, 'normal', 5),
       (NULL, 'negative', 1);

INSERT INTO conditions
VALUES (NULL, '최상의', 1, 15),
       (NULL, '활기찬', 1, 14),
       (NULL, '상쾌한', 1, 13),
       (NULL, '가벼운', 1, 12),
       (NULL, '생기 있는', 1, 11),
       (NULL, '안정적인', 2, 10),
       (NULL, '괜찮은', 2, 9),
       (NULL, '무난한', 2, 8),
       (NULL, '차분한', 2, 7),
       (NULL, '나른한', 2, 6),
       (NULL, '무기력한', 3, 5),
       (NULL, '피곤한', 3, 4),
       (NULL, '어지러운', 3, 3),
       (NULL, '지친', 3, 2),
       (NULL, '아픈', 3, 1);

INSERT INTO weather
VALUES (NULL, 'sunny'),
       (NULL, 'cloudy'),
       (NULL, 'overcast'),
       (NULL, 'rainy'),
       (NULL, 'thunder'),
       (NULL, 'snow');

INSERT INTO del_reason
VALUES (NULL, 'A'),
       (NULL, 'B'),
       (NULL, 'C'),
       (NULL, 'D'),
       (NULL, '기타');