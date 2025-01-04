'use client';
import { palette } from '@/styles/common';
import React from 'react';
import styled from 'styled-components';

type Props = {
  text: string;
  disabled?: boolean;
};
export default function Button({ text, disabled }: Props) {
  return <Btn disabled={disabled}>{text}</Btn>;
}

const Btn = styled.button`
  width: 100%;
  padding: 16px;

  font-size: 18px;
  line-height: 22px;

  font-weight: 700;
  font-family: var(--font-suit);

  border-radius: 8px;
  background-color: ${palette.softBlack100};

  &:disabled {
    background-color: ${palette.softBlack40};
  }
`;
