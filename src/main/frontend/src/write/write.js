import React, { useEffect } from 'react';
import PhotoFrame from '../common/PhotoFrame';
import PPStyledButton from '../common/PPStyledButton';
import PPStyledInput from '../common/PPStyledInput';
import styles from './write.module.css'
import BackgroundTemplate from '../common/BackgroundTemplates';
import subBackgroundImage from './res/assets/sub_bg.png';
import { useSearchParams } from 'react-router-dom';
import { useState, useRef } from 'react';
import imageCompression from "browser-image-compression";
import { get, post, put, putNoBaseUrl } from '../common/ServerInteractManager';

var isWorking = false;
var userId = null;
var postId = null;
var uploadUrl = null;
var thumbnailUrl = null;

function ImageUploadableFrame({ ...otherComponents }) {
    const fileInput = useRef(null);
    const [image, setImage] = useState(null);

    const handleFileSelect = (event) => {
        actionImgCompress(event.target.files[0]);
    }

    const handleLabelClick = () => {
        fileInput.current.click();
    };

    const actionImgCompress = async (fileSrc) => {
        const originOptions = {
            maxWidthOrHeight: 1920,
            useWebWorker: true,
        };
        const thumbOptions = {
            maxWidthOrHeight: 480,
            useWebWorker: true,
        };
        try {
            isWorking = true;
            const compressedImg = await imageCompression(fileSrc, originOptions);
            const thumbnail = await imageCompression(fileSrc, thumbOptions);
            setImage(URL.createObjectURL(thumbnail));

            const reader = new FileReader();
            reader.readAsDataURL(compressedImg);
            reader.onloadend = async () =>{
                await putNoBaseUrl(uploadUrl, compressedImg);
            };

            const reader2 = new FileReader();
            reader2.readAsDataURL(thumbnail);
            reader2.onloadend = async () =>{
                await putNoBaseUrl(thumbnailUrl, thumbnail);
                isWorking = false;
            };

        } catch (error) {
            console.log(error);
        }
    };

    return (
        <div style={ { zIndex: `1` }}>
            <input
                type="file"
                style={{ display: 'none' }}
                onChange={handleFileSelect}
                ref={fileInput}
            />
            <PhotoFrame htmlFor="fileInput" src={image} onClick={() => handleLabelClick()} {...otherComponents}/>
        </div>
    )
}

async function submit(nickname, title, comments) {
    const data = {
        "postId": postId,
        "title": title,
        "comments": comments,
        "senderName": nickname,
        "receiverUserId": userId,
        "readYn": false,
        "openYn": false,
    }
    await post('api/post', data);
    window.location.href = `/posts/done?postId=${postId}&hostId=${userId}`;
}

export default function WritePage() {
    const [searchParams] = useSearchParams();

    const [nickname, setNickname] = useState("");
    const [title, setTitle] = useState("");
    const [comments, setComments] = useState("");

    useEffect(() => {
        async function getImageUploadUrl() {
            const wrappedPostId= await post('api/post/image');
            postId = "" + wrappedPostId.data;
            const wrappedUploadUrl = await get('api/post/image/' + postId + '?type=origin');
            uploadUrl = wrappedUploadUrl.data;
            const wrappedThumbnailUrl = await get('api/post/image/' + postId + '?type=thumbnail');
            thumbnailUrl = wrappedThumbnailUrl.data;
        }
        userId = searchParams.get("userId");
        getImageUploadUrl();
    }, []);

    return (
        <>
        <BackgroundTemplate color={"#FFF2E9"}>
            <img className={styles.sub_background} src={subBackgroundImage} />
            <ImageUploadableFrame className={styles.frame} frameType='default'/>
            <PPStyledInput 
                className={styles.nickname} 
                title="너를 표현할 별명이 있을까?" 
                placeholder="텍스트 입력"
                textValueState={[nickname, setNickname]}
                maxLength={8}
            />
            <PPStyledInput 
                className={styles.title} 
                title="사진의 제목을 붙여줘!" 
                placeholder="텍스트 입력"
                textValueState={[title, setTitle]}
                maxLength={20}
            />
            <PPStyledInput 
                className={styles.comments} 
                title="사진과 함께 보낼 코멘트는?" 
                placeholder="텍스트 입력"
                textValueState={[comments, setComments]}
                maxLength={200}
            />
            <PPStyledButton 
                className={styles.send}
                type="blue"
                text="친구에게 전송하기"
                onClick={() => submit(nickname, title, comments)}
            />
        </BackgroundTemplate>
        </>
    );
}
