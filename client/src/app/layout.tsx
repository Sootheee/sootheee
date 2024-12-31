import type { Metadata } from 'next';
import localFont from 'next/font/local';
import '@/styles/globals.css';
import { METADATA } from '../constant/texts/common';
import styled from 'styled-components';

const Suit = localFont({
  src: './fonts/SUIT-Variable.woff2',
  variable: '--font-suit',
  weight: '100 900',
});

export const metadata: Metadata = {
  title: METADATA.TITLE,
  description: METADATA.DESCRIPTION,
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body className={Suit.variable}>{children}</body>
    </html>
  );
}
