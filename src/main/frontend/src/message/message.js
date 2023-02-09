import React, { useState, useEffect } from 'react';

import styles from './message.module.css';
import BackgroundTemplate from '../common/BackgroundTemplates';
import PPStyledButton from '../common/PPStyledButton';
import blueMessageSheet from './res/assets/sheet_blue.png';
import orangeMessageSheet from './res/assets/sheet_orange.png';
import deleteIconImage from './res/assets/ic_delete.png';
import downloadIconImage from './res/assets/ic_download.png';
import PhotoFrame from '../common/PhotoFrame';
import { useParams } from 'react-router-dom';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { get, put, del } from '../common/ServerInteractManager';

function MessageSheet({ children, sheetImage, ...otherComponents }) {
    return (
        <div style={{ 
            backgroundImage: `url(${sheetImage})`, 
            backgroundSize: `100% 100%`, 
        }} {...otherComponents}>{children}</div>
    )
}

async function postThisMessage(postId) {
    await put(`/api/post/${postId}`)
}

async function deleteThisMessage(postId) {
    await del(`/api/post/${postId}`)
}

function MessagePage() {
    const nav = useNavigate()
    const postId = useParams().postId;
    const searchParams = useSearchParams()[0];
    const [wallType, setWallType] = useState('')
    const [frameType, setFrameType] = useState('')

    const [title, setTitle] = useState('');
    const [imageSrc, setImageSrc] = useState('');
    const [comments, setComments] = useState('');
    const [createdDate, setCreatedDate] = useState('');
    const [senderName, setSenderName] = useState('');
    const [isOpened, setIsOpened] = useState(false);

    var sheetImage = (wallType == 'LINE' || wallType == 'STRIPE' ? blueMessageSheet : orangeMessageSheet);

    useEffect(() => {
        setWallType(searchParams.get('wallType'));
        setFrameType(searchParams.get('frameType'));

        get(`/api/post/${postId}`).then((response) => {
            const result = response.data;
            setTitle(result.title);
            setComments(result.comments);
            setCreatedDate(result.createdDate.split('T')[0]);
            setSenderName(result.senderName);
            setImageSrc(`https://photophoto-posts-img.s3.ap-northeast-2.amazonaws.com/thumbnail/${postId}.png`);
            setIsOpened(result.openYn);
        });
    }, []);

    return (
        <>
        <BackgroundTemplate backgroundType={wallType}>
            <div className={styles.header}>
                <span className={styles.title}>{title}</span>
                <div className={styles.icons}>
                <img src={deleteIconImage} className={styles.delete_icon} onClick={() => {
                    deleteThisMessage(postId);
                    nav(-1);
                }}/>
                <img src={downloadIconImage} className={styles.download_icon}/>
                </div>
            </div>
            <PhotoFrame frameType={frameType} src={imageSrc} className={styles.photo_frame} onClick={() => window.open(`https://photophoto-posts-img.s3.ap-northeast-2.amazonaws.com/origin/${postId}.png`)}/>
            <MessageSheet className={styles.sheet} sheetImage={sheetImage}>
                <div className={styles.inner_sheet}>
                    <p className={styles.body}>{comments}</p>
                    <p className={styles.sent_date}>{createdDate}</p>
                    <p className={styles.sender}>{senderName} 씀</p>
                </div>
            </MessageSheet>
            { !isOpened && 
                <PPStyledButton type="blue" className={styles.to_gallary} text="갤러리에 걸기" onClick={() => {
                    postThisMessage(postId)
                    nav(-1);
                }} /> 
            }
        </BackgroundTemplate>
        </>
    )
}

export default MessagePage;