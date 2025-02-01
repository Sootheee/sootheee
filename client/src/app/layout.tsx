import type { Metadata } from 'next';
import './globals.css';
import { METADATA_TEXT } from '@/constant/texts/common';
import Nav from '@/app/Nav';

export const metadata: Metadata = {
  title: METADATA_TEXT.TITLE,
  description: METADATA_TEXT.DESCRIPTION,
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className="flex flex-col h-svh w-full max-w-screen-md mx-auto">
        <main className="grow w-full h-full">{children}</main>
        <Nav />
      </body>
    </html>
  );
}
