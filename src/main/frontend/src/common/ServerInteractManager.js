import axios from "axios";
import { getAccessToken, getRefreshToken } from "./SessionManager";

export const serverDomain = 'http://ec2-54-180-92-252.ap-northeast-2.compute.amazonaws.com:8080';

export async function get(route, params={}, accessToken="") {
    return await createAxiosInstance(accessToken).get(
        serverDomain + route,
        { params : params }
    )
};

export async function post(route, data) {
    return await createAxiosInstance().post(
        serverDomain + route, 
        data,
    )
};

export async function put(route, data={}) {
    return await createAxiosInstance().put(
        serverDomain + route, 
        data,
    )
};

export async function del(route, data={}) {
    return await createAxiosInstance().delete(
        serverDomain + route, 
        data
    )
};

export async function putNoBaseUrl(route, data) {
    return await axios.put(
        route, 
        data,
    )
}

export async function getNoBaseUrl(route, data) {
    return await axios.get(
        route, 
        data,
    )
}

function createAxiosInstance(accessToken="") {
    if (accessToken === "") accessToken = getAccessToken();
    return axios.create({
        baseURL: serverDomain,
        withCredentials: true,
        headers: {
            Authorization: "bearer " + accessToken,
        },
    })
}