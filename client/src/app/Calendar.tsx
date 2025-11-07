'use client';

import React, {
  Dispatch,
  SetStateAction,
  useCallback,
  useEffect,
  useMemo,
} from 'react';
import { getDaysInMonth, startOfMonth, format } from 'date-fns';
import { ko } from 'date-fns/locale';
import clsx from 'clsx';
import { DAYS } from '@/constant/texts/home';
import { useRouter } from 'next/navigation';

type Props = {
  currentDate: Date;
  setCurrentDate: Dispatch<SetStateAction<Date>>;
};
export default function Calendar({ currentDate, setCurrentDate }: Props) {
  const router = useRouter();
  const today = useMemo(() => new Date(), []);

  useEffect(() => {
    console.log(currentDate);
  }, [currentDate]);

  const daysInMonth = useMemo(() => getDaysInMonth(currentDate), [currentDate]);
  const firstDayOfMonth = useMemo(
    () => startOfMonth(currentDate),
    [currentDate]
  );

  const firstDayOfWeek = firstDayOfMonth.getDay();

  const allDays = useMemo(
    () =>
      Array.from({ length: firstDayOfWeek }, () => '').concat(
        Array.from({ length: daysInMonth }, (_, i) => `${i + 1}`),
        []
      ),
    [firstDayOfWeek, daysInMonth]
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
    <div className="w-full flex flex-col items-center gap-8">
      <div className="w-full grid grid-cols-7 gap-1 mb-2">
        {DAYS.map((day, index) => (
          <span
            key={index}
            className={clsx(
              'font-bold text-[11px] text-center',
              index === 0 && 'text-sunday-red'
            )}
          >
            {day}
          </span>
        ))}
      </div>
      <div className="w-full grid grid-cols-7">
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
                'min-w-[40px] w-full min-h-[68px] flex items-start justify-center font-medium text-[15px] border-none bg-transparent cursor-pointer text-center',
                isSunday && 'text-sunday-red',
                getIsToday(day) &&
                  'relative after:content-[""] after:absolute after:w-[4px] after:h-[4px] after:rounded-full after:bg-soft-black after:top-[22px] after:left-1/2 after:-translate-x-1/2'
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
