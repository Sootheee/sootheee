import { create } from 'zustand';

// 상태 타입 정의
interface NavState {
  theme: 'light' | 'dark';
  setTheme: (theme: 'light' | 'dark') => void;
}

const useCommonStore = create<NavState>((set) => ({
  theme: 'light',
  setTheme: (theme) => set({ theme: theme }),
}));

export default useCommonStore;
