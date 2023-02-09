import React from 'react';

import styles from './welcome.module.css';
import { useSearchParams } from 'react-router-dom';
import BackgroundTemplate from '../common/BackgroundTemplates';
import backgroundImage from './res/assets/background.png';
import logo from './res/assets/logo.png';
import illust from './res/assets/illust.png'
import helpIcon from './res/assets/help_ico.png';
import kakaoLoginButton from './res/assets/kakao_login.png';
import naverLoginButton from './res/assets/naver_login.png';
import googleLoginButton from './res/assets/google_login.png';
import { serverDomain } from '../common/ServerInteractManager';
import { saveAccessToken, saveRefreshToken, getCurrentUserInfo } from '../common/SessionManager';
import { useEffect, useState } from 'react';

export default function WelcomePage() {
    const location = useSearchParams()[0];
    const accessToken = location.get("accessToken");
    const refreshToken = location.get("refreshToken");

    const kakaoLoginUrl = serverDomain + '/oauth2/authorization/kakao';
    const naverLoginUrl = serverDomain + '/oauth2/authorization/naver';
    const googleLoginUrl = serverDomain + '/oauth2/authorization/google';

    const [popupState, setPopupState] = useState(false);

    if (accessToken != null || refreshToken != null) {
        saveAccessToken(accessToken);
        saveRefreshToken(refreshToken);
    }
    
    useEffect(() => {
        async function checkRole() {
            const userInfo = await getCurrentUserInfo();
            if (userInfo != null) {
                console.log(userInfo.data);
                if (userInfo["role"] === "JOIN") {
                    window.location.href = "/signup/";
                }
                if (userInfo["role"] === "USER") {
                    window.location.href = "/gallery/" + userInfo.userId;
                }
            }
        }
        checkRole();
    }, [])
    
    return (
        <>
        <BackgroundTemplate backgroundImage={backgroundImage}>
            <img src={logo} className={styles.title}/>
            <img className={styles.main_illust} src={illust} />
            <div className={styles.bottom_section}>
                <div className={styles.discription}>
                    <p>갤러리를 만들어, 친구들에게 포토 방명록을 받을 수 있어.<br/>
                    즐거운 추억들을 친구들과 함께 공유해 보자!</p>
                </div>
                <button className={styles.help_button} onClick={() => setPopupState(true)}>
                    <img src={helpIcon} className={styles.help_ico}/>
                    도움말
                </button>
                <div className={styles.login_area}>
                    <div className={styles.sns_login_section}>
                        <p>SNS 간편 로그인</p>
                        <a href={kakaoLoginUrl}>
                            <img className={styles.sns_login_button} src={kakaoLoginButton} />
                        </a>
                        <a href={naverLoginUrl}>
                            <img className={styles.sns_login_button} src={naverLoginButton} />
                        </a>
                        <a href={googleLoginUrl}>
                            <img className={styles.sns_login_button} src={googleLoginButton} />
                        </a>
                    </div>
                </div>
                <div className={styles.informations}>
                    <span>개인정보 처리방침</span> |
                    <span> 이용약관</span>
                </div>
            </div>
        </BackgroundTemplate>
        </>
    );
}
