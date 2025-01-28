import React from 'react';
import Calendar from './Calendar';
import DateBar from '../component/DateBar';

export default function page() {
  return (
    <div className="w-full h-full bg-[#FCF8E4]">
      <DateBar />
      <Calendar />
    </div>
  );
}
