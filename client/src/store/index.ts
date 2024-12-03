import { create } from 'zustand';
import { persist } from 'zustand/middleware';

// NOTE: Examples
interface CounterState {
  count: number;
  increase: () => void;
  decrease: () => void;
  reset: () => void;
}

const useStore = create<CounterState>()(
  persist(
    (set) => ({
      count: 0, // 초기값
      increase: () => set((state) => ({ count: state.count + 1 })), // 증가 함수
      decrease: () => set((state) => ({ count: state.count - 1 })), // 감소 함수
      reset: () => set({ count: 0 }), // 리셋 함수
    }),
    {
      name: 'counter-storage', // 로컬 스토리지 키 이름
      //   storage: localStorage, // 저장소 설정
    }
  )
);

export default useStore;
