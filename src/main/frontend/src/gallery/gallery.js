import React from 'react';

import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import styles from './gallery.module.css'
import BackgroundTemplate from '../common/BackgroundTemplates';
import PPStyledButton from '../common/PPStyledButton';
import tempUserImage from './res/assets/temp_user_image.png'
import shareIconImage from './res/assets/share_ico.png'
import letterBoxIconImage from './res/assets/temp_letter_box.png'

import defaultFrame from './res/assets/frame_default.png'
import yellowFrame from './res/assets/frame_yellow.png'
import blueFrame from './res/assets/frame_blue.png'
import pinkFrame from './res/assets/frame_pink.png'
import blackFrame from './res/assets/frame_black.png'
import { getCurrentUserInfo } from '../common/SessionManager';
import { put, get } from '../common/ServerInteractManager';

class PostItem {
    id;
    thumbnail;
    constructor(id, thumbnail) {
        this.id = id;
        this.thumbnail = thumbnail;
    }
}

function getFrameByType(type) {
    switch(type) {
        case 'GOLD':
            return yellowFrame
        case 'BLUE':
            return blueFrame
        case 'LAVENDER':
            return pinkFrame
        case 'BLACK':
            return blackFrame
        default:
            return defaultFrame
    }
}

function Frame({ src, frameImage, ...otherComponents }) {
    if (src === undefined) return <></>
    return (
        <div style={{
            backgroundImage: `url(${frameImage})`, 
            backgroundSize: `100% 100%`,
            display: `grid`,
            placeItems: `center`
        }} {...otherComponents}>
            <img src={src} className={styles.image} />
        </div>
    )
}

function FrameSet({ postList, frameImage, onClickFrame }) {
    return (
        <div className={styles.frame_set}>
            <div className={styles.frame_section1}>
                { postList[0] != null && <Frame className={styles.frame1} src={postList[0].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[0].id)}/> }
                { postList[1] != null && <Frame className={styles.frame2} src={postList[1].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[1].id)}/> }
                { postList[2] != null && <Frame className={styles.frame3} src={postList[2].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[2].id)}/> }
                { postList[3] != null && <Frame className={styles.frame4} src={postList[3].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[3].id)}/> }
            </div>
            <div className={styles.frame_section2}>
                { postList[4] != null && <Frame className={styles.frame5} src={postList[4].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[4].id)}/> }
                { postList[5] != null && <Frame className={styles.frame6} src={postList[5].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[5].id)}/> }
                { postList[6] != null && <Frame className={styles.frame7} src={postList[6].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[6].id)}/> }
                { postList[7] != null && <Frame className={styles.frame8} src={postList[7].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[7].id)}/> }
                { postList[8] != null && <Frame className={styles.frame9} src={postList[8].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[8].id)}/> }
                { postList[9] != null && <Frame className={styles.frame10} src={postList[9].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[9].id)}/> }
                { postList[10] != null && <Frame className={styles.frame11} src={postList[10].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[10].id)}/> }
            </div>
            <div className={styles.frame_section3}>
                { postList[11] != null && <Frame className={styles.frame12} src={postList[11].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[11].id)}/> }
                { postList[12] != null && <Frame className={styles.frame13} src={postList[12].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[12].id)}/> }
                { postList[13] != null && <Frame className={styles.frame14} src={postList[13].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[13].id)}/> }
                { postList[14] != null && <Frame className={styles.frame15} src={postList[14].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[14].id)}/> }
            </div>
            <div className={styles.frame_section4}>
                { postList[15] != null && <Frame className={styles.frame16} src={postList[15].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[15].id)}/> }
                { postList[16] != null && <Frame className={styles.frame17} src={postList[16].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[16].id)}/> }
                { postList[17] != null && <Frame className={styles.frame18} src={postList[17].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[17].id)}/> }
                { postList[18] != null && <Frame className={styles.frame19} src={postList[18].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[18].id)}/> }
                { postList[19] != null && <Frame className={styles.frame20} src={postList[19].thumbnail} frameImage={frameImage} onClick={() => onClickFrame(postList[19].id)}/> }
            </div>
        </div>
    )
}

function GalleryArea({ postList, frameType, onClickFrame }) {
    const postCount = postList.length;
    const frameSetCount = Math.ceil(postCount / 20);
    const frameImage = getFrameByType(frameType)
    let frameSetComponents = [];

    for (let i = 0; i < frameSetCount; i++) {
        let splitedPostList = postList.slice(i * 20, Math.min((i + 1) * 20, postCount));
        frameSetComponents.push(<FrameSet key={i} postList={splitedPostList} frameImage={frameImage} onClickFrame={onClickFrame} />)
    }
    return <div className={styles.gallery}>
        {frameSetComponents}
    </div>
}

async function getUserData(userId) {
    const userInfo = userId === 'me'? await getCurrentUserInfo() : (await get(`api/profile/${userId}`)).data;
    console.log(userInfo);
    const newUserId = userInfo.userId;
    const nickname = userInfo.nickname;
    const backgroundType = userInfo.wallType;
    const frameType = userInfo.frameType;
    const profileImage = userInfo.imageUrl == null ? tempUserImage : userInfo.imageUrl;
    return { newUserId, nickname, backgroundType, frameType, profileImage }
}

function GalleryPage() {
    var userId = useParams().userId;
    const profileLink = `/profile/${userId}`;
    const loginLink = `/`;
    const onboardingPostLink = `/posts/onboarding/?userId=${userId}`;

    const [isOwner, setIsOwner] = useState(false);
    const [postList, setPostList] = useState([]);
    const [nickname, setNickname] = useState('');
    const [backgroundType, setBackgroundType] = useState('ZIGZAG');
    const [frameType, setFrameType] = useState('BROWN');
    const [profileImage, setProfileImage] = useState(tempUserImage);
    const [isDarkTheme, setIsDarkTheme] = useState(false);

    useEffect(() => {
        getUserData(userId).then(({ newUserId, nickname, backgroundType, frameType }) => { 
            if (nickname != null) {
                setNickname(nickname);
            }
            if (backgroundType != null) {
                setBackgroundType(backgroundType);
                setIsDarkTheme(backgroundType === 'SQUARE' || backgroundType === 'LINE');
            }
            if (frameType != null) {
                setFrameType(frameType);
            }
            setProfileImage('https://photophoto-user-img.s3.ap-northeast-2.amazonaws.com/' + userId + '.png');
            setIsOwner(newUserId == userId);
        });

        get(`api/gallery/${userId}`).then((response) => {
            var tmp = [];
            response.data.map((post) => {
                var tmp2 = new PostItem(post.postId, post.thumbnailUrl);
                tmp.push(tmp2);
            });
            setPostList(tmp);
            console.log(tmp);
        });
    }, [userId]);
    
    return (
        <>
        <BackgroundTemplate backgroundType={backgroundType} bottomType="bar">
            <div className={styles.header}>
                <img className={styles.profile_image} src={profileImage}/>
                <div className={styles.title_area}>
                    <span className={styles.gallery_title}>
                        <span style={{ color: "#FF7A00" }}>{nickname}</span><span style={{ color: isDarkTheme ? "#FFFFFF" : "#343434" }}>의 갤러리</span>
                    </span>
                    { isOwner && <a href={profileLink} className={styles.edit_profile}>내 프로필 수정</a> }
                    { !isOwner && <a href={loginLink} className={styles.edit_profile}>내 갤러리 로그인</a> }
                </div>
                { isOwner &&
                    <>
                        <img className={styles.share_icon} src={shareIconImage}/>
                        <div onClick={() => window.location.href = "/inbox/"}>
                            <img className={styles.letter_box} src={letterBoxIconImage}/>
                            {/* <div className={styles.letter_count}>1</div> */}
                        </div>
                    </>
                }
            </div>
            <GalleryArea postList={postList} frameType={frameType} onClickFrame={(postId) => window.location.href = `/post/${postId}?wallType=${backgroundType}&frameType=${frameType}`}/>
            <div className={styles.footer}>
                <PPStyledButton 
                    className={styles.write_post_button}
                    type="blue"
                    text="포토 방명록 작성하기"
                    onClick={() => {window.location.href = onboardingPostLink;}}
                />
                { !isOwner &&
                    <>
                        <p className={styles.encourage_write_letter} style={{ color: isDarkTheme ? "#FFFFFF" : "#0D1D54" }}>친구에게 포토 방명록을 보내 봐!</p>
                        <p className={styles.create_gallery}>나도 갤러리 만들기</p>
                    </>
                }
            </div>
        </BackgroundTemplate>
        </>
    )
}

export default GalleryPage;