'use client';

import styled from 'styled-components';
import ImageButton from '@/component/ImageButton';
import ImageTextButton from '@/component/ImageTextButton';

import { weatherImages } from '@/data/weather';
import { conditionImages } from '@/data/condition';
import ScoreSlider from './ScoreSlider';
import TextArea from '@/component/TextArea';

export default function Home() {
  return (
    <DiaryWrapper>
      <Header>
        <Date>date</Date>
        <SaveButton>저장</SaveButton>
      </Header>

      <ScoreSection>
        <Text>
          <em>이름</em> 멘트 멘트
        </Text>
        <ScoreSlider />
      </ScoreSection>

      <Wrapper>
        <SectionTitle>오늘 날씨랑 컨디션은 어땠나요?</SectionTitle>
        <WeatherWrapper>
          <SectionText>날씨</SectionText>
          <div>
            {weatherImages.map((weather) => (
              <ImageButton
                key={weather.id}
                id={weather.id}
                imagePath={weather.imagePath}
                onClick={() => {}}
              />
            ))}
          </div>
        </WeatherWrapper>

        <ConditionSection>
          <SectionText>
            컨디션 <div>(중복 선택 가능)</div>
          </SectionText>
          <div>
            {conditionImages.map((condition) => (
              <ImageTextButton
                key={condition.id}
                id={condition.id}
                name={condition.name}
                imagePath={condition.imagePath}
              />
            ))}
          </div>
        </ConditionSection>
      </Wrapper>

      <Wrapper>
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
      </Wrapper>
    </DiaryWrapper>
  );
}

const DiaryWrapper = styled.div`
  width: 100%;
  height: 100%;
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 64px;
  padding: 0 20px;
`;

const Text = styled.div`
  font-weight: 300;
  font-size: 22px;
  line-height: 30px;
  > em {
    font-style: normal;
    font-weight: 700;
  }
`;

const SectionText = styled.div`
  color: #333333;
  font-weight: 700;
  font-size: 16px;
  line-height: 22px;
  > div {
    opacity: 0.4;
    font-weight: 300;
    font-size: 14px;
    line-height: 22px;
  }
`;

const Date = styled.div`
  font-size: 16px;
  font-weight: bold;
`;

const SaveButton = styled.button`
  background: none;
  border: none;
  color: #333;
  font-size: 14px;
  cursor: pointer;
`;

const SectionTitle = styled.h2`
  font-size: 19px;
  line-height: 28px;
  margin: 20px 0 10px;
  font-weight: 300;
`;

const ScoreSection = styled.div`
  text-align: center;
  padding: 32px 24px;
  background-color: #fcf8e499;
  display: flex;
  flex-direction: column;
  gap: 48px;
`;

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  padding: 40px 24px;
  gap: 24px;
`;

const WeatherWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 12px;

  > div {
    display: flex;
    gap: 8px;
    align-items: center;
  }
`;

const ConditionSection = styled.div`
  display: flex;
  flex-direction: column;
  gap: 12px;
  > div {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }
`;
