import { get } from './ServerInteractManager';

export function saveAccessToken(token) {
  localStorage.setItem('accessToken', token);
}

export function saveRefreshToken(token) {
  localStorage.setItem('refreshToken', token);
}

export function getAccessToken() {
  return localStorage.getItem('accessToken');
}

export function getRefreshToken() {
  return localStorage.getItem('refreshToken');
}

export async function getCurrentUserInfo(count = 0, accessToken="") {
  if (count > 3) {
    if (window.location.href !== "http://localhost:3000/") {
      window.location.href = "/";
    }
    return null;
  }

  try{
    const result = await get('/profile/me', {}, accessToken);
    console.log(result);
    return result.data;
  } catch (e) {
    const tokenResult = await get('/auth/refresh', { "refreshToken": getRefreshToken() })
    saveAccessToken(tokenResult.data);
    return getCurrentUserInfo(count + 1, tokenResult.data);
  }
}