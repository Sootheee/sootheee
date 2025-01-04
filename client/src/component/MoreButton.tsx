import { palette } from '@/styles/common';
import React from 'react';
import styled from 'styled-components';

export default function MoreButton() {
  return (
    <Button>
      <Text></Text>
      <Icon></Icon>
    </Button>
  );
}

const Button = styled.button`
  display: flex;
  gap: 2px;

  color: ${palette.softBlack100};
  font-size: 13px;
  line-height: 18px;
`;

const Text = styled.span``;
const Icon = styled.span``;
