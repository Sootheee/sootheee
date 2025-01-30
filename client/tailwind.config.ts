import type { Config } from 'tailwindcss';

const config: Config = {
  content: [
    './src/components/**/*.{js,ts,jsx,tsx,mdx}',
    './src/app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        // CSS 변수 기반으로 색상 정의
        background: 'var(--background)',
        'background-light': 'var(--background-light)',
        foreground: 'var(--foreground)',
        'text-primary': 'var(--text-primary)',
        'text-secondary': 'var(--text-secondary)',
        'button-bg': 'var(--button-bg)',

        'soft-black': 'var(--soft-black)',
        'soft-black-weak': 'var(--soft-black-weak)',
        white: 'var(--white)',
        'white-weak': 'var(--white-weak)',
        'sunday-red': 'var(--sunday-red)',
        gray: 'var(--gray)',
        'forest-green': 'var(--forest-green)',
        'line-gray': 'var(--line-gray)',
        'warm-gray': 'var(--warm-gray)',

        'deep-gray': 'var(--deep-gray)',
        'white-weakest': 'var(--white-weakest)',
        'medium-gray': 'var(--medium-gray)',
      },
    },
  },
  plugins: [],
};

export default config;
