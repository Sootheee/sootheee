import Calendar from '@/app/Calendar';
import Image from 'next/image';
import DateBar from './DateBar';

export default function Home() {
  return (
    <section className="">
      <DateBar />
      <Calendar />
    </section>
  );
}
