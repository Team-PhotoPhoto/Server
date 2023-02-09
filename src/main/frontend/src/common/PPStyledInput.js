import React from 'react';

import { useState } from 'react';
import { useRef } from 'react';

const textAreaStyle = {
    boxSizing: `border-box`,
    width: `100%`, 
    minHeight: "8vw",
    marginTop: `1vh`,
    padding: `2vw`,
    background: `#FFFFFF`,
    outline: `none`,
    border: `2px solid #8A8A8A`,
    borderRadius: `2vw`,

    resize: `none`,
    maxLines: `20`,
    verticalAlign: `middle`,
    fontFamily: `"neo-dunggeunmo"`,
    fontSize: `4vw`,
    color: `#343434`,
}

const textAreaStyleFocus = {
    border: "2px solid #FF7A00"
}

const InputField = ({ maxLength, inputRef, onChange, ...otherProps }) => {
    const [isFocused, setIsFocused] = useState(false);

    if (maxLength > 20) {
        return <textarea 
                    ref={inputRef}
                    style={isFocused ? {...textAreaStyle, ...textAreaStyleFocus} : textAreaStyle}
                    maxLength={maxLength} 
                    onChange={
                        (e) => {
                            console.log(Math.max(`2.2`, e.target.scrollHeight))
                            e.target.style.height = Math.max(66, e.target.scrollHeight) + "px";
                            onChange(e);
                        }
                    }
                    onFocus={() => setIsFocused(true)}
                    onBlur={() => setIsFocused(false)}
                    {...otherProps} />;
    } else {
        return <input 
                    ref={inputRef}
                    type="text" 
                    style={isFocused ? {...textAreaStyle, ...textAreaStyleFocus} : textAreaStyle}
                    maxLength={maxLength} 
                    onChange={onChange} 
                    onFocus={() => setIsFocused(true)}
                    onBlur={() => setIsFocused(false)}
                    {...otherProps} />;
    }
};

export default function PPStyledInput({ title, maxLength, placeholder, className, inputRef, textValueState, color}) {
    const [textValue, setTextValue] = textValueState;
    const [currentLength, setCurrentLength] = useState(0);
    if (textValue != null && currentLength !== textValue.length) {
        setCurrentLength(textValue.length);
    }
    let countFontColor = currentLength >= maxLength ? "#DC3F3F" : color;

    return (
        <div className={className}>
            <div style={{ display: `flex`, justifyContent: `space-between`}}>
                <span style={{ fontSize: `3.5vw`, color: color }}>{title}</span>
                <span style={{ fontSize: `3.2vw`, color: countFontColor }}>
                    {currentLength}/{maxLength}
                </span>
            </div>
            <InputField 
                inputRef={inputRef}
                maxLength={maxLength} 
                placeholder={placeholder} 
                onChange={
                    (e) => {
                        setCurrentLength(e.target.value.length);
                        setTextValue(e.target.value);
                    }
                }
                value={textValue}
            />
        </div>
    );
}

