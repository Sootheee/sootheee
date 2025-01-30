'use client';

import React, { useCallback, Dispatch, SetStateAction } from 'react';
import { format, subMonths, addMonths } from 'date-fns';
import { ko } from 'date-fns/locale';
import Image from 'next/image';

type Props = {
  currentDate: Date;
  setCurrentDate: Dispatch<SetStateAction<Date>>;
};
export default function DateBar({ currentDate, setCurrentDate }: Props) {
  const handlePrevMonth = useCallback(() => {
    setCurrentDate((prevDate) => subMonths(prevDate, 1));
  }, []);

  const handleNextMonth = useCallback(() => {
    setCurrentDate((prevDate) => addMonths(prevDate, 1));
  }, []);

  return (
    <div className="flex items-center justify-center p-[77px] gap-[30px]">
      <button onClick={handlePrevMonth} className="text-medium-gray">
        <Image
          src="images/common/arrow-default.svg"
          alt="arrow-left"
          width={20}
          height={20}
        />
      </button>
      <span className="text-[28px] font-bold">
        {format(currentDate, 'yyyy.MM', { locale: ko })}
      </span>
      <button onClick={handleNextMonth} className="text-medium-gray">
        <Image
          src="images/common/arrow-default.svg"
          alt="arrow-right"
          width={20}
          height={20}
          className="transform scale-x-[-1]"
        />
      </button>
    </div>
  );
}
