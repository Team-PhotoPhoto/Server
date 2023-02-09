import React from 'react';

import styles from './message_box.module.css';
import BackgroundTemplate from '../common/BackgroundTemplates';
import PPStyledButton from '../common/PPStyledButton';
import { get } from '../common/ServerInteractManager';
import { useEffect, useState } from 'react';

function Message({sender, photoTitle, sentDate, readStatus, ...otherComponents}) {
    return (
        <div className={styles.message} {...otherComponents}>
            <div className={styles.left_area}>
            <p className={styles.sender}>{sender}</p>
            <p className={styles.photo_title}>{photoTitle}</p>
            <p className={styles.sent_date}>{sentDate}</p>
            </div>
            <div className={styles.right_area}>
                <span className={styles.read_status}>{readStatus}</span>
                <span className={styles.arrow_icon}>></span>
            </div>
        </div>
    )
}

export default function MessageBoxPage() {
    var isEmpty = false;
    const [inbox, setInbox] = useState([]);

    useEffect(() => {
        async function getInbox() {
            const result = await get('/api/inbox/?page=0&size=9999');
            if (result.data.length === 0) {
                isEmpty = true;
            } else {
                setInbox(result.data);
            }
        }
        getInbox();
    }, [])

    var content;
    if (isEmpty) {
        content = 
            <>
                <p className={styles.empty_description}>아직 도착한 방명록이 없네...!</p>
                <PPStyledButton
                    className={styles.share_link_button}
                    type='orange'
                    text='내 링크 공유하기'
                />
            </>
    }
    else {
        content = 
            <>
            {inbox.map((message) => {
                return (
                    <Message 
                        key={message.postId}
                        sender={message.senderName}
                        photoTitle={message.title}
                        sentDate={message.createdDate.split('T')[0]}
                        readStatus={message.readYn ? "읽음" : "읽지 않음"}
                        onClick={() => window.location.href = "/post/" + message.postId}
                    />
                )
            }, [])}
            </>
    }
    
    return (
        <>
        <BackgroundTemplate color='#EFFDFF'>
            <div className={styles.header}>
                <span className={styles.title}>수신함</span>
                {/* <button className={styles.upload_photo} onClick={() => window.location.href = onboardingPostLink}>포토 올리기</button> */}
            </div>
            {content}
        </BackgroundTemplate>
        </>
    ) 
}
