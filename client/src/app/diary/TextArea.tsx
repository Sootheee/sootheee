import React from 'react';
import clsx from 'clsx';

type Size = 's' | 'm' | 'l';
type Props = {
  title: string;
  size?: Size;
  placeholder: string;
  value: string;
  onChangeValue: () => void;
};
export default function TextArea({
  title,
  size = 'l',
  placeholder,
  value,
  onChangeValue,
}: Props) {
  const textAreaHeight = clsx({
    'h-[54px]': size === 's',
    'h-[76px]': size === 'm',
    'h-[200px]': size === 'l',
  });

  return (
    <div className="flex flex-col gap-3">
      <p className="font-light text-[20px] leading-[28px]">{title}</p>
      <div className={`p-4 w-full bg-[#f6f6f6] rounded-md ${textAreaHeight}`}>
        <textarea
          className="resize-none w-full h-full bg-none border-none text-sm bg-transparent placeholder:text-soft-black-weak"
          placeholder={placeholder}
          value={value}
          onChange={onChangeValue}
        />
      </div>
    </div>
  );
}
