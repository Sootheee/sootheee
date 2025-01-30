'use client';

import React from 'react';
import Link from 'next/link';
import Image from 'next/image';
import { usePathname } from 'next/navigation';
import useCommonStore from '@/store';

export default function Nav() {
  const { theme } = useCommonStore();
  const pathname = usePathname();

  const getImagePath = (baseName: string, href: string) => {
    const isCurrentPage = pathname === href;
    const suffix =
      theme === 'dark'
        ? isCurrentPage
          ? 'light'
          : 'dark'
        : isCurrentPage
        ? 'dark'
        : 'default';
    return `images/common/${baseName}-${suffix}.svg`;
  };

  return (
    <nav className="flex justify-center align-center pb-8">
      <div className="flex gap-8">
        <Link href="/">
          <Image
            src={getImagePath('home', '/')}
            alt="home"
            width={24}
            height={24}
          />
        </Link>
        <Link href="/diary/write">
          <Image
            src={getImagePath('write', '/diary/write')}
            alt="write"
            width={24}
            height={24}
          />
        </Link>
        <Link href="/stats">
          <Image
            src={getImagePath('stats', '/stats')}
            alt="statistic"
            width={24}
            height={24}
          />
        </Link>
        <Link href="/settings">
          <Image
            src={getImagePath('setting', '/settings')}
            alt="setting"
            width={24}
            height={24}
          />
        </Link>
      </div>
    </nav>
  );
}
