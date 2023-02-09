const dancerStyleNamePrefixs = [
    '어썸',
    '라이징',
    '레드',
    '파워풀',
    '제트',
    '프리티',
    '자이언트',
    '스트릿',
    '허니',
    '샤이닝',
    '크레이지',
    '매드',
    '플레이풀',
    '마젤토브',
    '신사동',
    '서울',
    '이달의',
    '프로',
    '프리미어',
]

const dancerStyelNameSuffixs = [
    '베이비',
    '몽키',
    '썬',
    '섀도우',
    '버블티',
    '펀치',
    '아비뇽',
    '토스트',
    '망나니',
    '드래곤',
    '젤리',
    '춘삼',
    '워커',
    '뽀삐',
    '몬스터',
    '파이터',
    '스타',
    '프린스',
    '퀸',
    '토네이도',
    '옹헤야',
    '우리가좍',
    '소년단',
    '견자단',
    '두팔'
]

export function getRandomNickname() {
    const prefix = dancerStyleNamePrefixs[Math.floor(Math.random() * dancerStyleNamePrefixs.length)];
    const suffix = dancerStyelNameSuffixs[Math.floor(Math.random() * dancerStyelNameSuffixs.length)];
    return prefix + " " + suffix;
}