import useCommonStore from '@/store';
import Image from 'next/image';

type Props = {
  date: string;
};
export default function Header({ date }: Props) {
  const { theme } = useCommonStore();
  const isDark = theme === 'dark';

  // 날짜를 "YYYY.MM.DD" 형식으로 포맷하는 함수
  const formatDate = () => {
    let now = new Date();

    if (date) {
      const dateRegex = /^\d{4}-\d{2}-\d{2}$/;
      if (dateRegex.test(date)) {
        now = new Date(date);
      }
    }

    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    return `${year}.${month}.${day}`;
  };
  const handleGoBack = () => {
    window.history.back();
  };

  return (
    <div
      className={`flex justify-between items-center h-[64px] px-5 bg-foreground `}
    >
      <button
        onClick={handleGoBack}
        className="bg-none border-none cursor-pointer p-0"
      >
        <Image
          src={
            isDark
              ? '/images/common/backward-arrow-dark.svg'
              : '/images/common/backward-arrow-light.svg'
          }
          alt="backward arrow"
          className="h-6 w-6"
          width={24}
          height={24}
        />
      </button>

      <div className="text-lg font-bold">{formatDate()}</div>

      <button className="bg-none border-none text-sm cursor-pointer">
        저장
      </button>
    </div>
  );
}
