import Slider from '@/component/Slider';
import { scoreImages } from '@/data/score';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';

export default function ScoreSlider() {
  const [score, setScore] = useState<number>(5);

  useEffect(() => {
    console.log(score);
  }, [score]);

  const handleSliderChange = (newValue: number) => {
    setScore(newValue);
  };

  return (
    <Container>
      <Wrapper>
        <Image
          src={scoreImages[score].imagePath}
          alt={`${scoreImages[score].id}-image`}
        />
        <Text>{score}점</Text>
      </Wrapper>

      <Slider initialValue={score} onChange={handleSliderChange} />
    </Container>
  );
}

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 36px;
`;

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
`;
const Image = styled.img`
  width: 112px;
  height: 112px;
`;
const Text = styled.div`
  font-weight: 600;
  font-size: 24px;
  line-height: 32px;
`;
