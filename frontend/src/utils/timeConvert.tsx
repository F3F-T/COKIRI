/**
 * Spring LocalDateTime -> 방금전, 몇분전 등으로 변환하는 함수
 *
 * https://js-joda.github.io/js-joda/manual/LocalDateTime.html 에서 LocalDateTime을 import한 후에 second를 받아와서 변환했음
 * 백에서 넘겨준 createdTime ("2023-01-22T14:31:04") 를 매개변수에 넣어준 후 사용하면 됨
 *
 * ex)
 * const timeDiffer = timeConvert(post.createdTime);
 * console.log(timeDiffer);
 */

import React, {useState, useEffect, useMemo, useCallback} from 'react';
import {ChronoUnit, LocalDateTime} from 'js-joda'
export const timeConvert = (localDateTime) => {
    const targetDate = LocalDateTime.parse(localDateTime);
    const nowDate = LocalDateTime.now();

    const seconds = targetDate.until(nowDate,ChronoUnit.SECONDS);

    if (seconds < 60)
        return `방금 전`

    const minutes = seconds / 60
    if (minutes < 60)
        return `${Math.floor(minutes)}분 전`

    const hours = minutes / 60
    if (hours < 24)
        return `${Math.floor(hours)}시간 전`

    const days = hours / 24
    if (days < 7)
        return `${Math.floor(days)}일 전`

    const weeks = days / 7
    if (weeks < 5)
        return `${Math.floor(weeks)}주 전`

    const months = days / 30
    if (months < 12)
        return `${Math.floor(months)}개월 전`

    const years = days / 365
        return `${Math.floor(years)}년 전`

}




export default timeConvert;