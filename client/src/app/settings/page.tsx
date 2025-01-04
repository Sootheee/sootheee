'use client';
import React from 'react';
import styled from 'styled-components';
import SettingButton from './SettingButton';
import { settingsMenu } from '@/data/settings';

export default function Home() {
  return (
    <Container>
      <TextWrapper>
        <Text>Name</Text>
        <SubText>E-mail</SubText>
      </TextWrapper>
      <Wrapper>
        {settingsMenu.map((settings) => (
          <SettingButton
            key={settings.id}
            id={settings.id}
            title={settings.title}
            imagePath={settings.imagePath}
          />
        ))}
      </Wrapper>
    </Container>
  );
}

const Container = styled.div`
  width: 100%;
  height: 100%;
  background-color: #fcf8e4;
  padding: 0 24px;
`;

const TextWrapper = styled.div`
  padding: 84px 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const Text = styled.div`
  color: #333333;
  font-weight: 300;
  font-size: 20px;
  line-height: 28px;
`;

const SubText = styled.div`
  color: #33333366;
  font-weight: 500;
  font-size: 16px;
  line-height: 24px;
`;

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 12px;
`;
