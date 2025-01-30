'use client';

import React, { useCallback, useMemo, useState } from 'react';
import { getDaysInMonth, startOfMonth, format } from 'date-fns';
import { ko } from 'date-fns/locale';
import clsx from 'clsx';
import { DAYS } from '@/constant/texts/home';
import { useRouter } from 'next/navigation';

export default function Calendar() {
  const router = useRouter();
  const [currentDate, setCurrentDate] = useState(new Date());
  const today = useMemo(() => new Date(), []);

  const daysInMonth = getDaysInMonth(currentDate);
  const firstDayOfMonth = startOfMonth(currentDate);

  const firstDayOfWeek = firstDayOfMonth.getDay();

  const allDays = useMemo(
    () =>
      Array.from({ length: firstDayOfWeek }, () => '').concat(
        Array.from({ length: daysInMonth }, (_, i) => `${i + 1}`),
        []
      ),
    []
  );

  const handleDayClick = useCallback(
    (day: string) => {
      if (day) {
        const date = new Date(
          currentDate.getFullYear(),
          currentDate.getMonth(),
          Number(day)
        );
        const formattedDate = format(date, 'yyyy-MM-dd', { locale: ko });
        router.push(`/diary/write?date=${formattedDate}`);
      }
    },
    [currentDate, router]
  );

  const getIsToday = useCallback(
    (day: string) =>
      today &&
      Number(day) === today?.getDate() &&
      currentDate.getMonth() === today?.getMonth() &&
      currentDate.getFullYear() === today?.getFullYear(),
    [currentDate, today]
  );

  return (
    <div className="w-full flex flex-col items-center p-5">
      <div className="w-full grid grid-cols-7 gap-1 mb-2">
        {DAYS.map((day, index) => (
          <span
            key={index}
            className={clsx(
              'font-bold text-xs text-center',
              index === 0 && 'text-sunday-red'
            )}
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
            Number(day) || 1
          );
          const isSunday = dayDate.getDay() === 0;
          return (
            <button
              key={index}
              onClick={() => handleDayClick(day)}
              className={clsx(
                'min-w-[40px] w-full min-h-[68px] flex items-center justify-center font-medium text-sm border-none rounded-full bg-transparent cursor-pointer text-center',
                isSunday && 'text-sunday-red',
                getIsToday(day) &&
                  'relative after:content-[""] after:absolute after:w-[5px] after:h-[5px] after:rounded-full after:bg-soft-black after:bottom-[5px] after:left-1/2 after:-translate-x-1/2'
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
