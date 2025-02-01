'use client';

import React, { useState, useCallback } from 'react';
import Image from 'next/image';
import { scoreImages } from '@/data/score';

export default function Slider() {
  const [score, setScore] = useState(5);

  const handleScoreChange = useCallback(
    (event: React.ChangeEvent<HTMLInputElement>) => {
      setScore(Number(event.target.value));
    },
    []
  );

  return (
    <div className="flex flex-col items-center p-5">
      <Image
        src={scoreImages[score].imagePath}
        alt="mood"
        width={112}
        height={112}
        priority={false}
      />
      <span className="text-2xl mt-6">{score}점</span>
      <input
        type="range"
        min={0}
        max={10}
        value={score}
        onChange={handleScoreChange}
        className="mt-9 w-full appearance-none h-2 rounded-full cursor-pointer
        focus:outline-none focus:ring-0

        [&::-webkit-slider-thumb]:appearance-none [&::-webkit-slider-thumb]:w-[26px] [&::-webkit-slider-thumb]:h-[26px] [&::-webkit-slider-thumb]:bg-white [&::-webkit-slider-thumb]:rounded-full [&::-webkit-slider-thumb]:border-2 [&::-webkit-slider-thumb]:border-gray [&::-webkit-slider-thumb]:hover:border-gray  [&::-webkit-slider-thumb]:shadow-[0px_4px_8px_0px_#0000001F]

        [&::-moz-range-thumb]:appearance-none [&::-moz-range-thumb]:w-[26px] [&::-moz-range-thumb]:h-[26px] [&::-moz-range-thumb]:bg-white [&::-moz-range-thumb]:rounded-full [&::-moz-range-thumb]:border-2 [&::-moz-range-thumb]:border-gray [&::-moz-range-thumb]:hover:border-gray [&::-moz-range-thumb]:shadow-[0px_4px_8px_0px_#0000001F]
        
        [&::-ms-thumb]:appearance-none [&::-ms-thumb]:w-[26px] [&::-ms-thumb]:h-[26px] [&::-ms-thumb]:bg-white [&::-ms-thumb]:rounded-full [&::-ms-thumb]:border-2 [&::-ms-thumb]:border-gray [&::-ms-thumb]:hover:border-gray [&::-ms-thumb]:shadow-[0px_4px_8px_0px_#0000001F]
         "
        style={{
          background: `linear-gradient(to right, var(--score-06) ${
            (score / 10) * 100
          }%, var(--warm-gray)  ${(score / 10) * 100}%)`,
        }}
      />
    </div>
  );
}
