import React from 'react';
import styled from 'styled-components';

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
  return (
    <Container>
      <Title>{title}</Title>
      <TextWrapper $size={size}>
        <Textarea
          placeholder={placeholder}
          value={value}
          onChange={onChangeValue}
        />
      </TextWrapper>
    </Container>
  );
}

const Container = styled.div`
  display: flex;
  flex-direction: column;
  gap: 12px;
`;

const Title = styled.p`
  font-weight: 300;
  font-size: 20px;
  line-height: 28px;
`;

const TextWrapper = styled.div<{ $size: Size }>`
  padding: 16px;
  width: 100%;
  height: ${({ $size }) =>
    `${
      $size === 's' ? 54 : $size === 'm' ? 76 : $size === 'l' ? 200 : 150
    }px`}; // 기본값 설정  padding: 10px;
  background-color: #f6f6f6;
  border-radius: 8px;
`;

const Textarea = styled.textarea`
  resize: none;
  width: 100%;
  height: 100%;
  background: none;
  border: none;
  font-size: 14px;
  &::placeholder {
    color: #33333366;
  }
`;
