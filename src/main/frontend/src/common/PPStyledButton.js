import React from 'react';

export default function PPStyledButton({ type, firstColor: firstColor_, secondColor: secondColor_, foreColor: foreColor_, text, ...otherProps }) {
    let firstColor, secondColor, foreColor;
    if (type === 'blue') {
        firstColor = '#DBF6FA';
        secondColor = '#C5F7FF';
        foreColor = '#005865';
    } else if (type === 'orange') {
        firstColor="#FFDAB8"
        secondColor="#FFAC5F"
        foreColor="#502600"
    } else {
        firstColor = firstColor_;
        secondColor = secondColor_;
        foreColor = foreColor_;
    }

    return (
        <button
            style={{
                height: '2.5em',
                background: `linear-gradient(180deg, ${firstColor}, ${secondColor})`,
                border: `0.08em solid ${foreColor}`,
                borderRadius: '0.4em',

                fontFamily: 'neo-dunggeunmo',
                fontSize: '5.5vw',
                color: foreColor,
            }}
            {...otherProps}
        >{text}</button>
    );
}