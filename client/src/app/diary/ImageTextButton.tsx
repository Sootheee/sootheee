import React from 'react';

type Props = {
  id: number;
  name: string;
  imagePath: string;
};

export default function ImageTextButton({ id, imagePath, name }: Props) {
  return (
    <button className="bg-gray h-9 rounded-md text-sm leading-[18px] px-3.5 min-w-[84px] flex items-center justify-center gap-1.5">
      <div
        className="w-4 h-4 bg-center bg-no-repeat bg-contain"
        style={{ backgroundImage: `url(${imagePath})` }}
      />
      <span className="font-normal text-sm leading-[18px]">{name}</span>
    </button>
  );
}
