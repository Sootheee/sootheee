import type { Metadata } from 'next';
import { Open_Sans } from 'next/font/google';
import './globals.css';
import { METADATA } from '@/constant/texts/common';
import Footer from './Footer';

const sans = Open_Sans({ subsets: ['latin'] });

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
    <html lang="en" className={sans.className}>
      <body className="flex flex-col w-full max-w-screen-2xl mx-auto">
        <main className="grow">{children}</main>
        <Footer />
      </body>
    </html>
  );
}
