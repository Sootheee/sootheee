import React from 'react';
import styled from 'styled-components';
type Props = {
  id: number;
  name: string;
  imagePath: string;
};
export default function ImageTextButton({ id, imagePath, name }: Props) {
  return (
    <Button>
      <Icon $src={imagePath} />
      <Text>{name}</Text>
    </Button>
  );
}

const Button = styled.button`
  background: #f6f6f6;
  height: 36px;
  border-radius: 8px;
  font-size: 13px;
  line-height: 18px;
  padding: 0 14px;
  min-width: 84px;

  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
`;

const Icon = styled.div<{ $src: string }>`
  width: 16px;
  height: 16px;
  background-image: url(${({ $src }) => $src});
  background-size: 16px 16px;
  background-repeat: no-repeat;
  background-position: center;
`;

const Text = styled.span`
  font-weight: 400;
  font-size: 14px;
  line-height: 18px;
`;
