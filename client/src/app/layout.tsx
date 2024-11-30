import type { Metadata } from 'next';
import localFont from 'next/font/local';
import '@/styles/globals.css';
import { METADATA } from '../constant/texts/common';

const SuitRegular = localFont({
  src: './fonts/SUIT-Regular.woff2',
  variable: '--font-suit-regular',
  weight: '900',
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
      <body className={`${SuitRegular.variable}`}>{children}</body>
    </html>
  );
}
