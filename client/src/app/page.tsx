'use client';
import dynamic from 'next/dynamic';

const CalendarClient = dynamic(() => import('@/component/Calendar'), {
  ssr: false,
});

import React, { useEffect, useState } from 'react';
import styled from 'styled-components';

export default function page() {
  const [initialDate, setInitialDate] = useState<Date | null>(null);

  useEffect(() => {
    setInitialDate(new Date());
  }, []);
  return (
    <div style={{ width: '100%', height: '100%', backgroundColor: '#fffcf1' }}>
      {initialDate && <CalendarClient initialDate={initialDate} />}
    </div>
  );
}
