import React from 'react';
import styled from 'styled-components';

type Props = {
  id: number;
  title: string;
  imagePath: string;
};
export default function SettingButton({ id, title, imagePath }: Props) {
  return (
    <Button>
      <Wrapper>
        <Icon $src={imagePath} />
        <Text>{title}</Text>
      </Wrapper>
    </Button>
  );
}

const Button = styled.button`
  width: 100%;
  height: 48px;
  background-color: white;
  border-radius: 8px;
  padding: 12px 16px;

  display: flex;
`;

const Wrapper = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
`;

const Icon = styled.div<{ $src: string }>`
  width: 24px;
  height: 24px;
  background-image: url(${({ $src }) => $src});
  background-size: 24px 24px;
  background-repeat: no-repeat;
  background-position: center;
`;

const Text = styled.span`
  font-weight: 500;
  font-size: 16px;
  line-height: 24px;
`;
