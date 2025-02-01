import React from 'react';
import TextArea from '../TextArea';
import { DIARY } from '@/constant/texts/diary';

export default function Texts() {
  return (
    <div className="flex flex-col gap-[28px]">
      <TextArea
        title={DIARY.TEXT_DESCRIPTION}
        placeholder="최대 500자까지 작성 가능"
        value=""
        onChangeValue={() => {}}
      />
      <TextArea
        title={DIARY.DIRECTION}
        size="m"
        placeholder="최대 200자까지 작성 가능"
        value=""
        onChangeValue={() => {}}
      />
      <TextArea
        title={DIARY.GRATITUDE}
        size="s"
        placeholder="최대 200자까지 작성 가능"
        value=""
        onChangeValue={() => {}}
      />
      <TextArea
        title={DIARY.LEARNING}
        size="s"
        placeholder="최대 200자까지 작성 가능"
        value=""
        onChangeValue={() => {}}
      />
    </div>
  );
}
