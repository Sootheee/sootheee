import { NextConfig } from 'next';

const nextConfig: NextConfig = {
  // 1. 타입스크립트 설정 오류 무시 (빌드 중 오류가 발생해도 계속 진행)
  typescript: {
    ignoreBuildErrors: true,
  },

  // 2. 이미지 최적화 설정 (도메인을 추가하여 외부 이미지를 불러올 수 있음)
  images: {
    domains: ['example.com'], // 필요한 이미지 도메인 추가
  },

  // 3. PWA 설정 (서비스 워커를 통해 오프라인 지원)
  pwa: {
    dest: 'public', // 서비스 워커 파일을 저장할 위치
    disable: process.env.NODE_ENV === 'development', // 개발 환경에서는 비활성화
  },

  // 4. 환경 변수 설정 (클라이언트에서 사용되는 환경 변수)
  env: {
    NEXT_PUBLIC_API_URL: process.env.NEXT_PUBLIC_API_URL,
  },

  // 5. 웹팩 설정 (모듈 규칙 및 로더 추가)
  webpack: (config, { isServer }) => {
    if (!isServer) {
      config.resolve.fallback = {
        fs: false, // 클라이언트에서 Node.js 모듈을 사용할 수 없도록 설정
      };
    }
    return config;
  },
};

export default nextConfig;
