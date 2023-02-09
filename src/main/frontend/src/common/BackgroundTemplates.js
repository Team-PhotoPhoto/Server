import React, { useEffect } from 'react';
import styled from 'styled-components';
import { getLayoutSet } from './LayoutResourceManager';

const BackgroundTemplateBlock = styled.div`
  width: "calc(var(--vw, 1vw) * 100)";
  min-height: 100vh;

  position: relative;
  box-shadow: 0 0 8px 0 rgba(0, 0, 0, 0.04);

  display: flex;
  flex-direction: column;
`;

function BackgroundTemplate({ backgroundType, bottomType, backgroundImage, color, children }) {
  let backgroundStyle = {};
  let bottomContent;
  if (backgroundImage != null) {
    backgroundStyle.backgroundImage = `url(${backgroundImage})`;
    backgroundStyle.backgroundRepeat = `no-repeat`;
    backgroundStyle.backgroundSize = `100% 100%`;
  } else if (color != null) {
    backgroundStyle.backgroundColor = `${color}`;
  } else {
    let backgroundLayoutSet = getLayoutSet(backgroundType);
    switch (bottomType) {
      case 'bar': 
          bottomContent = <div style={{ backgroundImage: `url(${backgroundLayoutSet.bar})`, width: `100%`, height: `3px`}}/>;
          break;
      case 'bottom-sheet':
          bottomContent = <div style={{ backgroundImage: `url(${backgroundLayoutSet.bottom})`, width: `100%`, height: `70px`}}/>;
          break;
    }
    backgroundStyle.backgroundImage = `url(${backgroundLayoutSet.tile})`;
  }
  useEffect(() => {
      let vh = 0, vw = 0;
      vw = window.innerWidth * 0.01;
      vh = window.innerHeight * 0.01;
      document.documentElement.style.setProperty("--vh", `${vh}px`);
      document.documentElement.style.setProperty("--vw", `${vw}px`);
    }, [window.innerHeight]);

  return <><BackgroundTemplateBlock style = {backgroundStyle}>{children}</BackgroundTemplateBlock><div>{bottomContent}</div></>;
}

export default BackgroundTemplate;