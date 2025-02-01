import React from 'react';

type Props = {
  id: number;
  imagePath: string;
  onClick: () => void;
};

export default function ImageButton({ id, imagePath, onClick }: Props) {
  return (
    <button
      className={'w-10 h-10 bg-gray rounded-md bg-center bg-no-repeat bg-auto'}
      onClick={onClick}
      style={{ backgroundImage: `url(${imagePath})` }}
    />
  );
}
