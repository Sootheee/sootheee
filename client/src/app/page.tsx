'use client';
import dynamic from 'next/dynamic';

const CalendarClient = dynamic(() => import('@/app/Calendar'), {
  ssr: false,
});

import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import DateBar from './DateBar';

export default function page() {
  const [initialDate, setInitialDate] = useState<Date | null>(null);

  useEffect(() => {
    setInitialDate(new Date());
  }, []);
  return (
    <div style={{ width: '100%', height: '100%', backgroundColor: '#FCF8E4' }}>
      <DateBar />
      {initialDate && <CalendarClient initialDate={initialDate} />}
    </div>
  );
}
