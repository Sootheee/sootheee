import type { Metadata } from 'next';
import { Open_Sans } from 'next/font/google';
import './globals.css';
import { METADATA_TEXT } from '@/constant/texts/common';
import Nav from '@/app/Nav';

const sans = Open_Sans({ subsets: ['latin'] });

export const metadata: Metadata = {
  title: METADATA_TEXT.TITLE,
  description: METADATA_TEXT.DESCRIPTION,
};

// TODO
// nav가 포함된 헤더 만들기
// 골격부터 반응형이 가능하도록 만들기
export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className={sans.className}>
      <body className="flex flex-col h-svh w-full max-w-screen-md mx-auto">
        <main className="grow w-full h-full overflow-auto">{children}</main>
        <Nav />
      </body>
    </html>
  );
}
