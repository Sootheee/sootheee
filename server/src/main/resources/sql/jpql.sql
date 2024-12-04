SELECT dairy.dairy_id, dairy.date, dairy.score
    FROM Dairy dairy
    WHERE dairy.dairy_id = 1111
        AND YEAR(dairy.date) = 2024
        AND MONTH(dairy.date) = 10;

drop table member, dairy, dairy_condition, weather, conditions, condition_type;



INSERT INTO member VALUES (NULL, 'abc@abc.com', '작성자1', 'N', 'N', '2024-11-01', '2024-11-11', 'KAKAOTALK', '3333','USER');
INSERT INTO dairy VALUES (NULL, '2024-10-10', 1, 3, 2.4, 'dkdkd', '', '', '', 'N', '2024-11-01', '2024-11-11'),
                         (NULL, '2024-10-11', 1, 3, 6.5, 'agc', '', '', '', 'N', '2024-11-01', '2024-11-11'),
                         (NULL, '2024-10-12', 1, 3, 8.8, 'efe', '', '', '', 'N', '2024-11-01', '2024-11-11');
INSERT INTO dairy_condition VALUES (NULL, 1, 3, 0, 'N', '2024-11-01', '2024-11-01'),
                                   (NULL, 1, 1, 1, 'N', '2024-11-01', '2024-11-01'),
                                   (NULL, 1, 2, 2, 'N', '2024-11-01', '2024-11-01'),
                                   (NULL, 1, 12, 3, 'N', '2024-11-01', '2024-11-01'),
                                   (NULL, 2, 5, 0, 'N', '2024-11-01', '2024-11-01'),
                                   (NULL, 2, 3, 1, 'N', '2024-11-01', '2024-11-01'),
                                   (NULL, 2, 1, 2, 'N', '2024-11-01', '2024-11-01'),
                                   (NULL, 2, 10, 3, 'N', '2024-11-01', '2024-11-01'),
                                   (NULL, 3, 3, 0, 'N', '2024-11-01', '2024-11-01'),
                                   (NULL, 3, 4, 1, 'N', '2024-11-01', '2024-11-01'),
                                   (NULL, 3, 11, 2, 'N', '2024-11-01', '2024-11-01');
/* summaryDairiesInMonth */
SELECT COUNT(d.dairy_id), AVG(d.score)
    FROM dairy d
    WHERE d.member_id = 1
        AND YEAR(d.date) = 2024
        AND MONTH(d.date) = 10
        AND d.is_delete like 'N';

/* findMostOneCondIdInMonth */
SELECT dc.cond_id,
       COUNT(dc.cond_id)
    FROM dairy_condition dc
    INNER JOIN dairy d ON d.dairy_id = dc.dairy_id
    WHERE d.member_id = 1
        AND YEAR(d.date) = 2024
        AND MONTH(d.date) = 10
        AND d.is_delete like 'N'
    GROUP BY dc.cond_id
    ORDER BY COUNT(dc.cond_id) DESC;