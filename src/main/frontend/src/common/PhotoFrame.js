import React from 'react';

import { getFrameByType } from "./LayoutResourceManager"

export default function PhotoFrame({ frameType, src, onClick, ...otherComponents }) {
    const frameImage = getFrameByType(frameType)
    return (
        <div style={{ 
            backgroundImage: `url(${frameImage})`, 
            backgroundSize: `100% 100%`,
            display: `grid`,
            placeItems: `center`
        }} {...otherComponents}>
            <div style={{
                width: `55.5%`,
                height: `55.5%`,
            }}>
                <img style={{
                    width: `100%`,
                    height: `100%`,
                    objectFit: `cover`, //contain?
                }} src={src} onClick={onClick} />
            </div>
        </div>
    )
}
