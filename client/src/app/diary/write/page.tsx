'use client';

import ImageButton from '@/app/diary/ImageButton';
import ImageTextButton from '@/app/diary/ImageTextButton';
import TextArea from '@/app/diary/TextArea';
import { conditionImages } from '@/data/condition';
import { weatherImages } from '@/data/weather';
import { useSearchParams } from 'next/navigation';

export default function DiaryWritePage() {
  const searchParams = useSearchParams();
  const date = searchParams.get('date');

  return (
    <div className="w-full h-full">
      <div className="flex justify-between items-center h-[64px] px-5">
        <div className="text-base font-bold">date</div>
        <button className="bg-none border-none text-[#333] text-sm cursor-pointer">
          저장
        </button>
      </div>

      <div className="text-center p-8 bg-[#fcf8e499] flex flex-col gap-12">
        <div className="font-light text-[22px] leading-[30px]">
          <em>이름</em> 멘트 멘트
        </div>
        {/* <ScoreSlider /> */}
      </div>

      <div className="flex flex-col p-10 gap-6">
        <h2 className="text-[19px] leading-[28px] mt-5 mb-2 font-light">
          오늘 날씨랑 컨디션은 어땠나요?
        </h2>
        <div className="flex flex-col gap-3">
          <div className="text-[#333] font-bold text-base leading-[22px]">
            날씨
          </div>
          <div className="flex gap-2 items-center">
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
          <div className="text-[#333] font-bold text-base leading-[22px] flex gap-1">
            컨디션
            <div className="opacity-40 font-light text-sm leading-[22px]">
              (중복 선택 가능)
            </div>
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

      <div className="flex flex-col p-10 gap-6">
        <TextArea
          title="오늘 어떤 일들이 있었는지 요약해주세요."
          placeholder="최대 500자까지 작성 가능"
          value=""
          onChangeValue={() => {}}
        />
        <TextArea
          title="바랐던 방향성"
          size="m"
          placeholder="최대 200자까지 작성 가능"
          value=""
          onChangeValue={() => {}}
        />

        <TextArea
          title="감사한 일"
          size="s"
          placeholder="최대 200자까지 작성 가능"
          value=""
          onChangeValue={() => {}}
        />

        <TextArea
          title="배운 일"
          size="s"
          placeholder="최대 200자까지 작성 가능"
          value=""
          onChangeValue={() => {}}
        />
      </div>
    </div>
  );
}
