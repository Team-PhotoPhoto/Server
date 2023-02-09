import React, { useEffect, useState } from 'react';

import styles from './post_done.module.css'
import BackgroundTemplate from '../common/BackgroundTemplates';
import PhotoFrame from '../common/PhotoFrame';
import PPStyledButton from '../common/PPStyledButton';
import backgroundImage from './res/assets/background.png';
import titleImage from './res/assets/title.png';
import { useSearchParams } from 'react-router-dom';

function PostDonePage() {
    const searchParams = useSearchParams()[0];

    const [postId, setPostId] = useState('');
    const [hostId, setHostId] = useState('');
    const [imageSrc, setImageSrc] = useState('');

    useEffect(() => { 
        setPostId(searchParams.get('postId'));
        setHostId(searchParams.get('hostId'));
        setImageSrc(`https://photophoto-posts-img.s3.ap-northeast-2.amazonaws.com/thumbnail/${postId}.png`);
    }, [postId]);
    return (
        <>
        <BackgroundTemplate backgroundImage={backgroundImage}>
            <PhotoFrame frameType="default" src={imageSrc} className={styles.sent_photo} />
            <img src={titleImage} className={styles.title} />
            <p className={styles.description}>사진과 방명록은 친구에게 잘 보내졌어.</p>
            <PPStyledButton 
                className={styles.button}
                type="orange"
                text="메인 갤러리로"
                onClick={() => window.location.href = `/gallery/${hostId}`}
            />
        </BackgroundTemplate>
        </>
    )
}

export default PostDonePage;