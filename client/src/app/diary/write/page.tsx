'use client';

import { useSearchParams } from 'next/navigation';
import Selections from './Selections';
import Texts from './Texts';
import Slider from './Slider';
import { DIARY } from '@/constant/texts/diary';
import Header from './Header';

export default function DiaryWritePage() {
  const searchParams = useSearchParams();
  const date = searchParams.get('date') || '';

  return (
    <div className="w-full h-full bg-white overflow-auto">
      <Header date={date} />

      <div className="p-8 flex flex-col gap-12 bg-background-light">
        <div
          className="whitespace-pre-wrap font-light text-[22px] leading-[30px]"
          dangerouslySetInnerHTML={{ __html: DIARY.DESCRIPTION('name') }}
        ></div>
        <Slider />
      </div>

      <div className="flex flex-col p-6 pt-10 pb-10 gap-10">
        <Selections />
        <Texts />
      </div>
    </div>
  );
}
