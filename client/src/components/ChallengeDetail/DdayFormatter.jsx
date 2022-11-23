import { useState, useEffect } from 'react';

export default function DdayFormatter(props) {
  const setDate = new Date(props.endDate);
  const now = new Date();
  //끝나는 날 까지 남은 시간
  const dis = setDate.getTime() - now.getTime();
  //1000=1초
  const min = 1000 * 60;
  //시간 계산
  const h = Math.floor(dis / (min * 60 * 24));
  const m = Math.floor((dis % (min * 60 * 24)) / (min * 60));
  const d = Math.floor((dis % (min * 60)) / min);
  const s = Math.floor((dis % min) / 1000);

  const [day, setDay] = useState(h);
  const [hour, setHour] = useState(m);
  const [minutes, setMinutes] = useState(d);
  const [seconds, setSeconds] = useState(s);
  // 시간이 넘어갈때마다 일, 시간, 분 바꿔주기
  useEffect(() => {
    const countdown = setInterval(() => {
      if (parseInt(seconds) > 0) setSeconds(parseInt(seconds) - 1);

      if (parseInt(seconds) === 0) {
        if (parseInt(minutes) === 0) {
          if (parseInt(hour) === 0) {
            if (parseInt(day) === 0) {
              clearInterval(countdown);
            } else {
              setDay(parseInt(day) - 1);
              setHour(23);
              setMinutes(59);
              setSeconds(59);
            }
          } else {
            setHour(parseInt(hour) - 1);
            setMinutes(59);
            setSeconds(59);
          }
        } else {
          setMinutes(parseInt(minutes) - 1);
          setSeconds(59);
        }
      }
    }, 1000);
    return () => clearInterval(countdown);
  }, [day, hour, minutes, seconds]);

  return (
    <div>{`D-DAY까지 ${day}일 ${hour}시간 ${minutes}분 ${seconds}초`}</div>
  );
}
