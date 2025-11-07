import { conditionImages } from '@/data/condition';
import React from 'react';
import ImageTextButton from '../ImageTextButton';
import ImageButton from '../ImageButton';
import { weatherImages } from '@/data/weather';
import { DIARY } from '@/constant/texts/diary';

export default function Selections() {
  return (
    <div className="flex flex-col gap-6">
      <h2 className="text-[20px] leading-[28px] font-light">
        {DIARY.SELECTION_DESCRIPTION}
      </h2>

      <div className="flex flex-col gap-3">
        <div className="font-bold text-base leading-[22px]">
          {DIARY.WEATHER}
        </div>
        <div className="flex gap-3 items-center">
          {weatherImages.map((weather) => (
            <ImageButton
              key={weather.id}
              id={weather.id}
              imagePath={weather.imagePath}
              onClick={() => {}}
            />
          ))}
        </div>
      </div>

      <div className="flex flex-col gap-3">
        <div className="flex gap-[2px] font-bold text-base leading-[22px]">
          {DIARY.CONDITION}
          <span className="opacity-40 font-light text-sm leading-[22px]">
            (중복 선택 가능)
          </span>
        </div>
        <div className="flex flex-wrap gap-2">
          {conditionImages.map((condition) => (
            <ImageTextButton
              key={condition.id}
              id={condition.id}
              name={condition.name}
              imagePath={condition.imagePath}
            />
          ))}
        </div>
      </div>
    </div>
  );
}
