/**
 * interceptor 사용 이유 :
 * token을 모든 api 호출마다 반복적으로 header에 넣어주고, 만료가 되면 예외 처리를 해야한다.
 * 이걸 일반화시키기 위해 api를 호출할때마다 이 파일에서 설정한 로직을 거친다고 생각하면 된다.
 */

import axios, {AxiosRequestConfig} from 'axios';
import {setToken} from "../store/jwtTokenReducer";
import {history} from "../index";


let store;

//이렇게 store을 index파일에서 주입해주는 식으로 하면 순환오류를 막을수있다.
//https://redux.js.org/faq/code-structure#how-can-i-use-the-redux-store-in-non-component-files
export const injectStore = _store => {
    store = _store;
}

const Api = axios.create({
    baseURL: "http://localhost:8080",
});

//accessToken을 header에 넣어준다
Api.interceptors.request.use(
    (config) => {

        // HTTP Authorization 요청 헤더에 jwt-token을 넣음
        // 서버측 미들웨어에서 이를 확인하고 검증한 후 해당 API에 요청함.
        const accessToken = store.getState().jwtTokenReducer.accessToken;

        try {
            if (!accessToken) {
                config.headers["Authorization"] = null;
            } else if (config.headers && accessToken) {
                config.headers["Authorization"] = `Bearer ${accessToken}`;
            }

        } catch (err) {
            console.error('[_axios.interceptors.request] config : ' + err);
        }

        return config;
    },

    (error) => {
        // 요청 에러 직전 호출됩니다.
        return Promise.reject(error);
    }
);

Api.interceptors.response.use(
    //200번 (성공) 범위에 있는 상태 코드는 이 함수에서 트리거 된다
    function (response) {
        return response;
    },
    //200번이 아닌 응답 오류 작업 핸들링
    async function (err) {

        //accessToken이 만료가 돼서 401이 떴을때
        if (err.response && err.response.status === 401) {
            console.log(`${err.response.data.status} : ` + err.response.data.message);
            switch (err.response.status) {
                /**
                 * 401 : UNAUTORIZED 권한이 없음
                 * 예외 처리 시나리오
                 * 1. accessToken이 만료됐을때
                 */
                case 401: {
                    console.log("401 오류 switch문 접근")
                    const accessToken = store.getState().jwtTokenReducer.accessToken;
                    const jsonObj = {"accessToken": accessToken};
                    console.log(jsonObj)
                    //유저 로그인 상태일때
                    if(accessToken) {
                        //accessToken 만료가 되면 백엔드에 있는 refreshToken으로 accessToken을 다시 받아온다.
                        try {
                            const data = await Api.post("/auth/reissue", jsonObj);
                            const jwtToken = data.data.tokenInfo;

                            if (jwtToken) {
                                store.dispatch(setToken(jwtToken));
                                console.log("reissue 성공")
                                return new Promise(() => {});
                                // return await Api.request(err.config);
                            }

                            //refreshToken도 만료가 됐을때 : 재로그인
                        } catch (err) {
                            console.log("refreshToken이 만료돼서 accesToken을 재발급할수없음")
                            alert("토콘이 만료됐으니 다시 로그인 해주세요")
                            history.push('/login');
                            console.log("재로그인해야함")
                        }
                    }else{
                        console.log("로그인 예외처리..? ")
                        // alert("로그인이 필요한 작업입니다")
                        return Promise.reject(err);
                    }

                    console.log(err)


                    //promise chain을 끊어준다
                    // return new Promise(() => {
                    // });

                }
                case 404: {
                    return new Promise(() => {
                    });
                }

                case 409: {
                    return new Promise(() => {
                    });
                }


            }

            // try {
            //     //accessToken 만료가 되면 백엔드에 있는 refreshToken으로 accessToken을 다시 받아온다.
            //     const data = await Api.post("/auth/reissue", jsonObj);
            //
            //     const jwtToken = data.data.tokenInfo;
            //
            //     if (jwtToken) {
            //         store.dispatch(setToken(jwtToken));
            //         return await Api.request(err.config);
            //     }
            // } catch (err) {
            //     console.log("토큰 갱신 에러");
            // }
            return Promise.reject(err);
        }
        return Promise.reject(err);
    }
);

export default Api;