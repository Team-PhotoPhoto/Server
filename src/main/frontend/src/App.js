import './res/fonts/font.css';
import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { createGlobalStyle } from 'styled-components';
import Signup from './signup/signup';
import WelcomePage from './welcome/welcome';
import GalleryPage from './gallery/gallery';
import ProfilePage from './profile/profile';
import OnboardingWritePage from './onboarding-write/onboarding-write';
import WritePage from './write/write';
import PostDonePage from './post_done/post_done';
import MessageBoxPage from './message_box/message_box';
import MessagePage from './message/message';

const GlobalStyle = createGlobalStyle`
    body {
        background: #e9ecef;
        font-family: "neo-dunggeunmo";
    }
`;

function App() {
    return (
        <div>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<WelcomePage />} />
                    <Route path="/oauth2/redirect" element={<WelcomePage />} />
                    <Route path="/signup" element={<Signup />} />
                    <Route path="/gallery/:userId" element={<GalleryPage />} />
                    <Route path="/profile/:userId" element={<ProfilePage />} />
                    <Route path="/posts/onboarding" element={<OnboardingWritePage />} />
                    <Route path="/posts/write" element={<WritePage />} />
                    <Route path="/posts/done" element={<PostDonePage />} />
                    <Route path="/inbox/" element={<MessageBoxPage />} />
                    <Route path="/post/:postId" element={<MessagePage />} />
                </Routes>
            </BrowserRouter>
            <GlobalStyle />
        </div>
    );
}

export default App;
