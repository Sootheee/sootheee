SELECT dairy.dairy_id, dairy.date, dairy.score
    FROM Dairy dairy
    WHERE dairy.dairy_id = 1111
        AND YEAR(dairy.date) = 2024
        AND MONTH(dairy.date) = 10;

drop table member, dairy, dairy_condition, weather, conditions, condition_type;