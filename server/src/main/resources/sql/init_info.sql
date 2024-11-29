INSERT INTO condition_type
VALUES (NULL, 'positive', 10),
       (NULL, 'normal', 5),
       (NULL, 'negative', 1);

INSERT INTO conditions
VALUES (NULL, '최상의', 1, 0),
       (NULL, '활기찬', 1, 0),
       (NULL, '상쾌한', 1, 0),
       (NULL, '가벼운', 1, 0),
       (NULL, '생기 있는', 1, 0),
       (NULL, '안정적인', 2, 0),
       (NULL, '괜찮은', 2, 0),
       (NULL, '무난한', 2, 0),
       (NULL, '차분한', 2, 0),
       (NULL, '나른한', 2, 0),
       (NULL, '무기력한', 3, 0),
       (NULL, '피곤한', 3, 0),
       (NULL, '어지러운', 3, 0),
       (NULL, '지친', 3, 0),
       (NULL, '아픈', 3, 0);

INSERT INTO weather
VALUES (NULL, 'sunny'),
       (NULL, 'cloudy'),
       (NULL, 'overcast'),
       (NULL, 'rainy'),
       (NULL, 'thunder'),
       (NULL, 'snow');
