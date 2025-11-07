'use client';

import React, { useRef, useEffect, useState, useCallback } from 'react';
import styled from 'styled-components';

interface StyledSliderProps {
  initialValue?: number;
  onChange: (value: number) => void;
}

const SliderContainer = styled.div`
  position: relative;
  width: 100%;
  height: 0.5rem;
  border-radius: 9999px;
  background-color: #e5e7eb;
`;

const SliderFill = styled.div<{ $percentage: number }>`
  position: absolute;
  height: 100%;
  border-radius: 9999px;
  background-color: #fde047;
  width: ${(props) => `${props.$percentage}%`};
`;

const SliderHandle = styled.div<{ $percentage: number }>`
  position: absolute;
  width: 1.5rem;
  height: 1.5rem;
  border-radius: 9999px;
  background-color: #fff;
  border: 2px solid #d1d5db;
  transform: translateY(-50%);
  cursor: pointer;
  box-shadow: 0 1px 3px 0 rgb(0 0 0 / 0.1), 0 1px 2px -1px rgb(0 0 0 / 0.1);
  left: ${(props) => `${props.$percentage}%`};
  top: 50%;
  transform: translate(-50%, -50%);
  &::after {
    content: '';
    position: absolute;
    width: 1.5rem;
    height: 1.5rem;
    border-radius: 9999px;
    background-color: #fff;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    box-shadow: 0 1px 3px 0 rgb(0 0 0 / 0.1), 0 1px 2px -1px rgb(0 0 0 / 0.1);
  }
`;

const StyledSlider: React.FC<StyledSliderProps> = ({
  initialValue = 0,
  onChange,
}) => {
  const sliderContainerRef = useRef<HTMLDivElement>(null);
  const sliderFillRef = useRef<HTMLDivElement>(null);
  const sliderHandleRef = useRef<HTMLDivElement>(null);
  const calculatePercentage = useCallback((value: number): number => {
    return (value / 10) * 100;
  }, []);
  const [percentage, setPercentage] = useState(
    calculatePercentage(initialValue)
  );

  const calculateValue = useCallback((percentage: number): number => {
    return Math.round(percentage / 10);
  }, []);

  useEffect(() => {
    const sliderContainer = sliderContainerRef.current;
    const sliderFill = sliderFillRef.current;
    const sliderHandle = sliderHandleRef.current;

    if (!sliderContainer || !sliderFill || !sliderHandle) return;

    let isDragging = false;

    function onMouseMove(event: MouseEvent) {
      if (!sliderContainer || !isDragging) return;
      const rect = sliderContainer.getBoundingClientRect();
      let x = event.clientX - rect.left;
      x = Math.max(0, Math.min(x, rect.width));
      let newPercentage = (x / rect.width) * 100;

      const newValue = calculateValue(newPercentage);
      newPercentage = calculatePercentage(newValue);
      setPercentage(newPercentage);

      if (onChange) onChange(newValue);
    }

    function handleMouseDown(event: MouseEvent) {
      isDragging = true;
      event.preventDefault();
    }

    function handleMouseUp() {
      isDragging = false;
    }

    sliderHandle.addEventListener('mousedown', handleMouseDown);
    document.addEventListener('mousemove', onMouseMove);
    document.addEventListener('mouseup', handleMouseUp);

    sliderFill.style.width = `${percentage}%`;
    sliderHandle.style.left = `${percentage}%`;

    return () => {
      sliderHandle.removeEventListener('mousedown', handleMouseDown);
      document.removeEventListener('mousemove', onMouseMove);
      document.removeEventListener('mouseup', handleMouseUp);
    };
  }, [calculatePercentage, calculateValue, onChange, percentage]);

  return (
    <SliderContainer ref={sliderContainerRef}>
      <SliderFill $percentage={percentage} ref={sliderFillRef} />
      <SliderHandle $percentage={percentage} ref={sliderHandleRef} />
    </SliderContainer>
  );
};

export default StyledSlider;
