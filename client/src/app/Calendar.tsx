'use client';
import React, { useState, useEffect, useCallback } from 'react';
import styled from 'styled-components';
import { getDaysInMonth, format, startOfMonth } from 'date-fns';
import { ko } from 'date-fns/locale';

interface CalendarProps {
  initialDate?: Date;
}

const Calendar: React.FC<CalendarProps> = ({ initialDate }) => {
  const [today, setToday] = useState<Date | null>(initialDate || null);
  const [currentDate, setCurrentDate] = useState(initialDate || new Date());

  useEffect(() => {
    setToday(new Date());
  }, []);

  const daysInMonth = getDaysInMonth(currentDate);
  const firstDayOfMonth = startOfMonth(currentDate);

  const firstDayOfWeek = firstDayOfMonth.getDay();

  const days = ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'];
  const allDays = Array.from({ length: firstDayOfWeek }, () => null).concat(
    Array.from({ length: daysInMonth }, (_, i) => i + 1)
  );

  const handleDayClick = useCallback(
    (day: number | null) => {
      if (day) {
        const date = new Date(
          currentDate.getFullYear(),
          currentDate.getMonth(),
          day
        );
        console.log(format(date, 'yyyy-MM-dd', { locale: ko }));
      }
    },
    [currentDate]
  );

  const getIsToday = useCallback(
    (day: number | null) => {
      return (
        today &&
        day === today?.getDate() &&
        currentDate.getMonth() === today?.getMonth() &&
        currentDate.getFullYear() === today?.getFullYear()
      );
    },
    [currentDate, today]
  );

  return (
    <CalendarContainer>
      <DaysHeader>
        {days.map((day, index) => (
          <DayLabel key={index} $isSunday={index === 0}>
            {day}
          </DayLabel>
        ))}
      </DaysHeader>
      <DaysGrid>
        {allDays.map((day, index) => {
          const dayDate = new Date(
            currentDate.getFullYear(),
            currentDate.getMonth(),
            day || 1
          );
          const isSunday = dayDate.getDay() === 0;
          return (
            <DayButton
              key={index}
              onClick={() => handleDayClick(day)}
              $isToday={getIsToday(day)}
              $isSunday={isSunday}
            >
              {day}
            </DayButton>
          );
        })}
      </DaysGrid>
    </CalendarContainer>
  );
};

export default Calendar;

const CalendarContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
`;

const DaysHeader = styled.div`
  width: 100%;
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 5px;
  margin-bottom: 10px;
`;

const DayLabel = styled.span<{ $isSunday?: boolean }>`
  font-weight: 700;
  font-size: 11px;
  line-height: 14px;
  text-align: center;
  color: ${(props) => (props.$isSunday ? '#ff5959' : '#333')};
`;

const DaysGrid = styled.div`
  width: 100%;
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 5px;
`;

const DayButton = styled.button<{ $isToday: boolean; $isSunday?: boolean }>`
  min-width: 40px;
  width: 100%;
  min-height: 68px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
  font-size: 15px;
  line-height: 20px;
  border: none;
  border-radius: 50%;
  background-color: transparent;
  color: ${(props) => (props.$isSunday ? '#ff5959' : '#333')};
  cursor: pointer;
  text-align: center;
  &:hover {
    /* background-color: #f0f0f0; */
  }
  ${(props) =>
    props.$isToday &&
    `
    // background-color: #f0f0f0;
    position:relative;
    &::after {
      content: '';
      position: absolute;
      width: 5px;
      height: 5px;
      border-radius: 50%;
      background-color: #333;
      bottom:5px;
      left:50%;
      transform: translateX(-50%);
    }
`}
`;
