import React from 'react';
import Calendar from './Calendar';
import DateBar from './DateBar';

export default function page() {
  return (
    <div style={{ width: '100%', height: '100%', backgroundColor: '#FCF8E4' }}>
      <DateBar />
      <Calendar />
    </div>
  );
}
