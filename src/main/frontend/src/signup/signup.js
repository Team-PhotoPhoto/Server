import React, { useState, useRef } from 'react';

import styles from './signup.module.css';
import BackgroundTemplate from '../common/BackgroundTemplates';
import backgroundImage from './res/assets/background.png';
import PPStyledButton from '../common/PPStyledButton';
import PPStyledInput from '../common/PPStyledInput';
import { getCurrentUserInfo } from '../common/SessionManager';
import { put } from '../common/ServerInteractManager';
import { getRandomNickname } from '../common/Utils';

async function submitNickname(nickname) {
    const currentUserInfo = await getCurrentUserInfo();
    console.log(currentUserInfo);
    console.log(nickname);
    const result = await put('/api/profile',
        {
            "nickname": nickname,
            "wallType": "BROWN",
            "frameType": "ZIGZAG",
            "emailNoti": "",
            "noti": true
        },
    )
    if (result != null) {
        window.location.href = "/";
    }
}

export default function Signup() {
    const inputNicknameState = useState(getRandomNickname());
    const inputRef = useRef(null);

    return (
        <>
            <BackgroundTemplate backgroundImage={backgroundImage}>
                <div className={styles.title}>
                    <span style={{ color: "#DB60C1" }}>반</span>
                    <span style={{ color: "#FF7A00" }}>가</span>
                    <span style={{ color: "#41B8DD" }}>워</span>
                    <span style={{ color: "#8127AC" }}>요</span>
                    <span style={{ color: "#FF7A00" }}>!</span>
                </div>
                <PPStyledInput
                    title="내 닉네임"
                    maxLength={8}
                    placeholder=""
                    className={styles.nickname_set_area}
                    inputRef={inputRef}
                    textValueState={inputNicknameState}
                />
                <p className={styles.discription}>
                    이제 갤러리를 만들 수 있어.<br/>
                    친구들에게 나를 표현할 이름을 적어줘!
                </p>
                <p className={styles.notice_changable}>
                    *<span style={{ textDecoration: `underline` }}>내 프로필 수정</span>에서 언제든지 변경 가능해
                </p>
                <PPStyledButton
                    type='orange'
                    className={styles.create_gallery}
                    text="내 갤러리 만들기"
                    onClick={() => submitNickname(inputRef.current.value)}
                />
            </BackgroundTemplate>
        </>
    )
}