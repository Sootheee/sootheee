import React from 'react';
import styled from 'styled-components';

type Props = {
  id: number;
  imagePath: string;
  onClick: () => void;
};

export default function ImageButton({ id, imagePath, onClick }: Props) {
  return <Button $src={imagePath} onClick={onClick} />;
}

const Button = styled.button<{ $src: string }>`
  width: 40px;
  height: 40px;
  background-color: #f6f6f6;
  border-radius: 8px;

  background-image: url(${({ $src }) => $src});
  background-size: 24px 24px;
  background-repeat: no-repeat;
  background-position: center;
`;
