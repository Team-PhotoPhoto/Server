import React from 'react';

import styles from './onboarding-write.module.css'
import PPStyledButton from '../common/PPStyledButton';
import BackgroundTemplate from '../common/BackgroundTemplates';
import backgroundImage from './res/assets/background.png';
import subBackgroundImage from './res/assets/sub_bg.png';
import postboxImage from './res/assets/postbox.png';
import titleImage from './res/assets/title.png';
import arrowImage from './res/assets/arrow.png';
import { useSearchParams } from 'react-router-dom';

function OnboardingWrite() {
    const [searchParams] = useSearchParams();
    const userId = searchParams.get("userId");
    const writePostLink = `/posts/write/?userId=${userId}`;

    return (
        <>
        <BackgroundTemplate backgroundImage={backgroundImage}>
            <img className={styles.sub_background} src={subBackgroundImage}/>
            <img className={styles.title} src={titleImage}/>
            <div className={styles.suggestion_list}> 
                <div className={styles.dotted_text}>
                    <img
                        style= {{ height: "50%"}} 
                        src={arrowImage}/>
                    <span style={
                        { 
                            marginLeft: "1em",
                            fontSize: "1.5em",
                            color: "#005865",
                        }
                    }>같이 찍은 인생네컷 📸</span>
                </div>
                <div className={styles.dotted_text}>
                    <img
                        style= {{ height: "50%"}} 
                        src={arrowImage}/>
                    <span style={
                        { 
                            marginLeft: "1em",
                            fontSize: "1.5em",
                            color: "#005865",
                        }
                    }>내가 찍어준 인생샷 😉</span>
                </div>
                <div className={styles.dotted_text}>
                    <img
                        style= {{ height: "50%"}} 
                        src={arrowImage}/>
                    <span style={
                        { 
                            marginLeft: "1em",
                            fontSize: "1.5em",
                            color: "#005865",
                        }
                    }>함께 뿌신 최고 맛집 🥘</span>
                </div>
                <div className={styles.dotted_text}>
                    <img
                        style= {{ height: "50%"}} 
                        src={arrowImage}/>
                    <span style={
                        { 
                            marginLeft: "1em",
                            fontSize: "1.5em",
                            color: "#005865",
                        }
                    }>너의 닮은꼴 동물 친구 🐼</span>
                </div>
            </div>
            <div className={styles.gradient_shadow}/>
            <img className={styles.postbox} src={postboxImage} />
            <p className={styles.discription}>친구에게 보내고 싶은 사진을 올려,<br/>함께 추억을 공유해 봐!</p>
            <PPStyledButton 
                className={styles.upload_photo}
                type="blue"
                text="사진 불러오기"
                onClick={() => {window.location.href = writePostLink;}}
            />
        </BackgroundTemplate>
        </>
    );
}

export default OnboardingWrite;