'use client';
import React, { useState, useEffect, useCallback } from 'react';
import { getDaysInMonth, format, startOfMonth } from 'date-fns';
import { ko } from 'date-fns/locale';
import { DAYS } from '../constant/texts/home';
import clsx from 'clsx';

interface CalendarProps {
  initialDate?: Date;
}

export default function Calendar({ initialDate }: CalendarProps) {
  const [today, setToday] = useState<Date | null>(initialDate || null);
  const [currentDate, setCurrentDate] = useState(initialDate || new Date());

  useEffect(() => {
    setToday(new Date());
  }, []);

  const daysInMonth = getDaysInMonth(currentDate);
  const firstDayOfMonth = startOfMonth(currentDate);

  const firstDayOfWeek = firstDayOfMonth.getDay();

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
    <div className="w-full flex flex-col items-center p-5">
      <div className="w-full grid grid-cols-7 gap-1 mb-2">
        {DAYS.map((day, index) => (
          <span
            key={index}
            className={`font-bold text-xs text-center ${
              index === 0 ? 'text-[#ff5959]' : 'text-[#333]'
            }`}
          >
            {day}
          </span>
        ))}
      </div>
      <div className="w-full grid grid-cols-7 gap-1">
        {allDays.map((day, index) => {
          const dayDate = new Date(
            currentDate.getFullYear(),
            currentDate.getMonth(),
            day || 1
          );
          const isSunday = dayDate.getDay() === 0;
          return (
            <button
              key={index}
              onClick={() => handleDayClick(day)}
              className={clsx(
                'min-w-[40px] w-full min-h-[68px] flex items-center justify-center font-medium text-sm border-none rounded-full bg-transparent cursor-pointer text-center',
                isSunday ? 'text-[#ff5959]' : 'text-[#333]',
                getIsToday(day) &&
                  'relative after:content-[""] after:absolute after:w-[5px] after:h-[5px] after:rounded-full after:bg-[#333] after:bottom-[5px] after:left-1/2 after:-translate-x-1/2',
                'hover:bg-[#f0f0f0]'
              )}
            >
              {day}
            </button>
          );
        })}
      </div>
    </div>
  );
}
