import React from 'react';
import clsx from 'clsx';

type Props = {
  id: number;
  imagePath: string;
  onClick: () => void;
};

export default function ImageButton({ id, imagePath, onClick }: Props) {
  return (
    <button
      className={clsx(
        'w-10 h-10 bg-custom-gray rounded-md bg-center bg-no-repeat bg-contain'
      )}
      onClick={onClick}
      style={{ backgroundImage: `url(${imagePath})` }}
    />
  );
}
