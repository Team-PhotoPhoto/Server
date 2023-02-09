import React from 'react';

import { useEffect, useRef, useState } from 'react';
import styles from './profile.module.css'
import BackgroundTemplate from '../common/BackgroundTemplates';
import PPStyledInput from '../common/PPStyledInput';
import PPStyledButton from '../common/PPStyledButton';
import styled from 'styled-components';
import Switch from './Switch';
import imageCompression from "browser-image-compression";
import { getAllTiles, getAllFrameMocks } from '../common/LayoutResourceManager';
import { getCurrentUserInfo } from '../common/SessionManager';
import { get, getNoBaseUrl, put, putNoBaseUrl } from '../common/ServerInteractManager';

const FlexDiv = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
`   

function SelectableImage({ isSelected, ...otherComponents}) {
    let style = {
        width: `9.5vw`,
        height: `9.5vw`,
        backgroundColor: `white`,
        border: `1px solid #CFCFCF`,
        outline: isSelected ? `4px solid #FF7A00` : `none`,
        outlineOffset: isSelected ? `-4px` : `0`,
        borderRadius: `8px`,
    } 

    return <img style={style} {...otherComponents}/>
}

function ImageUploadButton({ setImage, ...otherComponents }) {
    const fileInput = useRef(null);

    const handleFileSelect = (event) => {
        actionImgCompress(event.target.files[0]);
    }

    const handleLabelClick = () => {
        fileInput.current.click();
    };

    const actionImgCompress = async (fileSrc) => {
        const options = {
            maxSizeMB: 0.2,
            maxWidthOrHeight: 480,
            useWebWorker: true,
        };
        try {
            isWorking = true;
            const compressedFile = await imageCompression(fileSrc, options);
            setImage(URL.createObjectURL(compressedFile));

            const reader = new FileReader();
            reader.readAsDataURL(fileSrc);
            reader.onloadend = async () =>{
                const uploadUrl = await getProfileImageUploadUrl();
                const result = await putNoBaseUrl(uploadUrl, compressedFile);

                isWorking = false;
            };

        } catch (error) {
            console.log(error);
        }
    };

    return (
        <div { ...otherComponents }>
            <input
                type="file"
                style={{ display: 'none' }}
                onChange={handleFileSelect}
                ref={fileInput}
            />
            <label htmlFor="fileInput" className="button" onClick={() => handleLabelClick()}>
                이미지 선택
            </label>
        </div>
    )
}

async function getProfileImageUploadUrl() {
    const response = await get('/api/profile/me/image');
    return response.data;
}

async function getProfileImage(userId) {
    const response = await getNoBaseUrl('https://photophoto-user-img.s3.ap-northeast-2.amazonaws.com/' + userId + '.png');
    return response.data;
}

async function updateProfile(userId, nickname, backgroundType, frameType, email, emailNotiStatus) {
    while (isWorking) {
        //wait until image compression is done
        setTimeout(() => {}, 1000);
    }

    console.log(nickname, backgroundType, frameType, emailNotiStatus, email)
    const result = await put('/api/profile', {
        nickname: nickname,
        wallType: backgroundType,
        frameType: frameType,
        noti: emailNotiStatus,
        emailNoti: email,
    });

    if (result != null) {
        window.location.href = '/gallery/' + userId;
    }
}

let isWorking = false;

async function getUserData() {
    const userInfo = await getCurrentUserInfo();
    const userId = userInfo.userId;
    const nickname = userInfo.nickname;
    const backgroundType = userInfo.wallType;
    const frameType = userInfo.frameType;
    const email = userInfo.emailNoti;
    const emailNotiStatus = userInfo.noti;
    return { userId, nickname, backgroundType, frameType, email, emailNotiStatus}
}

function ProfilePage() {
    const wallpapers = getAllTiles();
    const frames = getAllFrameMocks();
    const [userId, setUserId] = useState('');
    const [profileImage, setProfileImage] = useState('');
    const [nickname, setNickname] = useState('');
    const [selectedWallpaper, setSelectedWallpaper] = useState('ZIGZAG');
    const [selectedFrame, setSelectedFrame] = useState("BROWN");
    const [useEmailNoti, setUseEmailNoti] = useState(true);
    const [email, setEmail] = useState('');

    useEffect(() => {
        getUserData().then(({ userId, nickname, backgroundType, frameType, email, emailNotiStatus }) => {
            getProfileImage(userId).then((image) => {
                const profileImage = 'https://photophoto-user-img.s3.ap-northeast-2.amazonaws.com/' + userId + '.png'
                setProfileImage(profileImage);
            });
            setUserId(userId)
            setNickname(nickname);
            setSelectedWallpaper(backgroundType);
            setSelectedFrame(frameType);
            setUseEmailNoti(emailNotiStatus);
            setEmail(email);
        });
    }, []);

    const fontColor = selectedWallpaper === 'SQUARE' || selectedWallpaper === 'LINE' ? '#FFFFFF' : '#000000';

    return (
        <>
        <BackgroundTemplate backgroundType={selectedWallpaper} bottomType="bottom-sheet">
            <div className={styles.column_box}>
                <div className={styles.profile_area}>
                    <img className={styles.profile_image} src={profileImage}/>
                    <ImageUploadButton className={styles.profile_image_description} setImage={setProfileImage}/>
                </div>
                <PPStyledInput 
                    className={styles.nickname_set_area}
                    maxLength={8}
                    title="내 닉네임"
                    textValueState={[nickname, setNickname]}
                    color={fontColor}
                />
                <div className={styles.wallpaper_selector}>
                    <span style={{ color: fontColor }}>내 갤러리 벽지</span><br/>
                    <div className={styles.selector_box}>
                        <SelectableImage 
                            src={wallpapers.ZIGZAG}
                            isSelected={selectedWallpaper === "ZIGZAG"}
                            onClick={() => setSelectedWallpaper("ZIGZAG")}
                        />
                        <SelectableImage 
                            src={wallpapers.SQUARE}
                            isSelected={selectedWallpaper === "SQUARE"}
                            onClick={() => setSelectedWallpaper("SQUARE")}
                        />
                        <SelectableImage 
                            src={wallpapers.STRIPE}
                            isSelected={selectedWallpaper === "STRIPE"}
                            onClick={() => setSelectedWallpaper("STRIPE")}
                        />
                        <SelectableImage 
                            src={wallpapers.FLOWER}
                            isSelected={selectedWallpaper === "FLOWER"}
                            onClick={() => setSelectedWallpaper("FLOWER")}
                        />
                        <SelectableImage 
                            src={wallpapers.LINE}
                            isSelected={selectedWallpaper === "LINE"}
                            onClick={() => setSelectedWallpaper("LINE")}
                        />
                    </div>
                </div>
                <div className={styles.frame_selector}>
                    <span style={{ color: fontColor }}>내 액자 프레임</span><br/>
                    <div className={styles.selector_box}>
                        <SelectableImage 
                            src={frames.BROWN}
                            isSelected={selectedFrame === "BROWN"}
                            onClick={() => setSelectedFrame("BROWN")}
                        />
                        <SelectableImage 
                            src={frames.GOLD}
                            isSelected={selectedFrame === "GOLD"}
                            onClick={() => setSelectedFrame("GOLD")}
                        />
                        <SelectableImage 
                            src={frames.BLUE}
                            isSelected={selectedFrame === "BLUE"}
                            onClick={() => setSelectedFrame("BLUE")}
                        />
                        <SelectableImage 
                            src={frames.LAVENDER}
                            isSelected={selectedFrame === "LAVENDER"}
                            onClick={() => setSelectedFrame("LAVENDER")}
                        />
                        <SelectableImage 
                            src={frames.BLACK}
                            isSelected={selectedFrame === "BLACK"}
                            onClick={() => setSelectedFrame("BLACK")}
                        />
                    </div>
                </div>
                <div className={styles.noti_email_section}>
                    <FlexDiv style={{ color: fontColor }}>
                    알림 수신받을 이메일
                    <Switch 
                        className={styles.noti_email_toggle} 
                        isOn={useEmailNoti} 
                        handleToggle={() => setUseEmailNoti(!useEmailNoti)}
                    />
                    </FlexDiv>
                    <FlexDiv style={{marginTop: `2vh`}}>
                        <input 
                            className={styles.noti_email_input}
                            onChange={(e) => setEmail(e.target.value)}
                            value={email}
                        />
                    </FlexDiv>
                </div>
                <div className={styles.informations} style={{ color: fontColor }}>
                    <span>계정 로그아웃</span>
                    <span>서비스 탈퇴</span>
                </div>
                <PPStyledButton 
                    className={styles.save_profile_button} 
                    firstColor="#FFDAB8"
                    secondColor="#FFAC5F"
                    foreColor="#502600"
                    text="프로필 저장하기"
                    onClick={() => {updateProfile(userId, nickname, selectedWallpaper, selectedFrame, email, useEmailNoti)}}
                />
                <div className={styles.share_link}>
                    친구에게 사이트 초대 링크 공유
                </div>
            </div>
        </BackgroundTemplate>
        </>
    )
}

export default ProfilePage;