'use client';
import Calendar from '@/app/Calendar';
import DateBar from './DateBar';
import { useState } from 'react';

export default function Home() {
  const [currentDate, setCurrentDate] = useState(new Date());

  return (
    <section className="flex flex-col">
      <DateBar currentDate={currentDate} setCurrentDate={setCurrentDate} />
      <Calendar currentDate={currentDate} setCurrentDate={setCurrentDate} />
    </section>
  );
}
